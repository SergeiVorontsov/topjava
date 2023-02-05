package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealStorage {

    List<Meal> getAll();

    Meal update(int id, LocalDateTime dateTime, String description, int calories);

    Meal create(LocalDateTime dateTime, String description, int calories);

    void delete(int id);

    Meal get(int id);
}