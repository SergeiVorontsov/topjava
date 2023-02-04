package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {

    List<Meal> getAll();

    Meal update(Meal meal);

    Meal create(Meal meal);

    void delete(int id);

    Meal get(int id);

    Integer createId();
}