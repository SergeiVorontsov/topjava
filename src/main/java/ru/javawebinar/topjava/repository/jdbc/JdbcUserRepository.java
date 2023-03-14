package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            if (insertRoles(newKey.intValue(), user.getRoles()) == null) {
                return null;
            }
        } else if ((namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, registered=:registered,
                   enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0)
                || (upsertRoles(user.id(), user.getRoles()) == null)) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    @Transactional
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        if (users.size() == 1) {
            List<Role> userRoles = getUserRoles(id);
            users.get(0).setRoles(userRoles);
        }
        return DataAccessUtils.singleResult(users);
    }

    @Override
    @Transactional
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        if (users.size() == 1) {
            User user = users.get(0);
            List<Role> userRoles = getUserRoles(user.id());
            user.setRoles(userRoles);
        }
        return DataAccessUtils.singleResult(users);
    }

    @Override
    @Transactional
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        HashMap<Integer, List<Role>> roles = getAllRoles();
        users.forEach(user -> user.setRoles(roles.get(user.id())));
        return users;
    }

    private List<Role> getUserRoles(int userId) {
        return jdbcTemplate.queryForList("SELECT role FROM user_role WHERE user_id=?", Role.class, userId);
    }

    private HashMap<Integer, List<Role>> getAllRoles() {
        return jdbcTemplate.query("SELECT * FROM user_role", rs -> {
            HashMap<Integer, List<Role>> result = new HashMap<>();
            while (rs.next()) {
                Integer key = rs.getInt("user_id");
                List<Role> value = new ArrayList<>();
                value.add(Role.valueOf(rs.getString("role")));
                result.compute(key, (k, v) -> (v == null) ? value : Stream.concat(v.stream(), value.stream()).toList());
            }
            return result;
        });
    }

    private Set<Role> upsertRoles(int userId, Set<Role> roles) {
        return (deleteRoles(userId) && (insertRoles(userId, roles) != null)) ? roles : null;
    }

    private boolean deleteRoles(int userId) {
        return jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", userId) != 0;
    }

    private Set<Role> insertRoles(int userId, Set<Role> roles) {
        int[] result = jdbcTemplate.batchUpdate("INSERT INTO user_role VALUES (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(2, roles.iterator().next().toString());
                ps.setInt(1, userId);
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
        return result.length == roles.size() ? roles : null;
    }
}
