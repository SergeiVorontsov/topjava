package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        create(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        create(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> allMeals = new ArrayList<>(storage.values());
        log.debug("Get all meals. Quantity is {}", allMeals.size());
        return allMeals;
    }

    @Override
    public Meal update(int id, LocalDateTime dateTime, String description, int calories) {
        if (storage.containsKey(id)) {
            Meal meal = new Meal(id, dateTime, description, calories);
            log.debug("Update meal: {}", meal);
            try {
                storage.replace(meal.getId(), meal);
                return meal;
            } catch (Exception e) {
                log.debug("Update meal {} is fail: {}", id, e);
                throw new RuntimeException(e);
            }
        } else {
            log.debug("Update meal id={} fail. Such meal not exist. Trying to create", id);
            create(dateTime, description, calories);
            return null;
        }
    }

    @Override
    public Meal create(LocalDateTime dateTime, String description, int calories) {
        Meal meal = new Meal(createId(), dateTime, description, calories);
        log.debug("Create meal: {}", meal);
        if (!storage.containsKey(meal.getId())) {
            try {
                storage.put(meal.getId(), meal);
                return meal;
            } catch (Exception e) {
                log.debug("Create meal {} is fail: {}", meal.getId(), e);
                throw new RuntimeException(e);
            }
        } else {
            log.debug("Create meal id={} fail. Such id already exist. Trying to create another id", meal.getId());
            create(dateTime, description, calories);
            return null;
        }
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

    private Integer createId() {
        return counter.getAndIncrement();
    }
}