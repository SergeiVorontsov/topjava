package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.RootController;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {
    private final Logger log = LoggerFactory.getLogger(RootController.class);
    private final int userId = SecurityUtil.authUserId();

    @Autowired
    protected MealService service;

    public Meal get(int id) {
        log.info("get meal {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public List<MealTo> getAll() {
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public void delete(int id) {
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }

    public List<MealTo> filter(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                               @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}
