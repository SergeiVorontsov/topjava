package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapMealStorage implements Storage {
    protected static final Map<Integer, Meal> mapMealStorage = new ConcurrentHashMap<>();

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mapMealStorage.values());
    }

    @Override
    public void update(Meal meal) {
        mapMealStorage.replace(meal.getId(), meal);
    }

    @Override
    public void save(Meal meal) {
        mapMealStorage.put(meal.getId(), meal);
    }

    @Override
    public void delete(Integer id) {
        mapMealStorage.remove(id);
    }

    @Override
    public Meal get(Integer id) {
        return mapMealStorage.get(id);
    }
}