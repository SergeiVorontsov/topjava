package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> InMemoryMealRepository.this.save(meal, 1));
        this.save(new Meal(LocalDateTime.now(), "Чужая еда", 5000), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (get(meal.getId(), userId) != null) {
            meal.setUserId(userId);
            return (repository.replace(meal.getId(), meal) != null) ? meal : null;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal result = repository.get(id);
        return (result != null && result.getUserId() == userId) ? result : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFilteredByUserIdAndPredicate(meal -> true, userId);
    }

    public List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getFilteredByUserIdAndPredicate(meal -> isBetweenHalfOpen(meal.getDate(), startDate, endDate.plusDays(1)), userId);
    }

    private List<Meal> getFilteredByUserIdAndPredicate(Predicate<Meal> filter, int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}