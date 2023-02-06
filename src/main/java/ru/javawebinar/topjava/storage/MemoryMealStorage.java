package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MemoryMealStorage implements MealStorage {
    private static final Logger log = getLogger(MemoryMealStorage.class);

    private final AtomicInteger counter = new AtomicInteger(1);

    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    public MemoryMealStorage() {
        List<Meal> meals = Arrays.asList(
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        meals.forEach(MemoryMealStorage.this::save);
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> allMeals = new ArrayList<>(storage.values());
        log.debug("Get all meals. Quantity is {}", allMeals.size());
        return allMeals;
    }

  /*  @Override
    public Meal update(Meal meal) {
        log.debug("Update meal: {}", meal);
        return (storage.replace(meal.getId(), meal) != null) ? meal : null;
    }

    @Override
    public Meal create(Meal meal) {
        log.debug("Create meal: {}", meal);
        meal.setId(createId());
        storage.put(meal.getId(), meal);
        return meal;
    }*/

    @Override
    public Meal save(Meal meal) {
        log.debug("Save meal: {}", meal);
        if (meal.getId() == null) {
            meal.setId(createId());
            storage.put(meal.getId(), meal);
            return meal;
        } else {
            return (storage.replace(meal.getId(), meal) != null) ? meal : null;
        }
    }

    private int createId() {
        return counter.getAndIncrement();
    }

    @Override
    public void delete(int id) {
        log.debug("Delete meal id {}", id);
        storage.remove(id);
    }

    @Override
    public Meal get(int id) {
        Meal m = storage.get(id);
        log.debug("Get meal id ={}: {}", id, m);
        return m;
    }
}