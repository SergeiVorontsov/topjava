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

public class MealMemoryStorage implements MealStorage {
    private static final Logger log = getLogger(MealMemoryStorage.class);

    private final AtomicInteger counter = new AtomicInteger(1);

    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    public MealMemoryStorage() {
        List<Meal> meals = Arrays.asList(
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(createId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        meals.forEach(meal -> storage.put(meal.getId(), meal));
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> list = new ArrayList<>(storage.values());
        log.debug("get all meals: {}", list);
        return list;
    }

    @Override
    public Meal update(Meal meal) {
        storage.replace(meal.getId(), meal);
        log.debug("update meal: {}", meal);
        return meal;
    }

    @Override
    public Meal create(Meal meal) {
        storage.put(meal.getId(), meal);
        log.debug("create meal: {}", meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
        log.debug("delete meal id {}", id);
    }

    @Override
    public Meal get(int id) {
        Meal m = storage.get(id);
        log.debug("get meal: {}", m);
        return m;
    }

    public Integer createId() {
        return counter.getAndIncrement();
    }
}