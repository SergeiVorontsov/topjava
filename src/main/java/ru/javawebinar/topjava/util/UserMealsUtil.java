package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        List<UserMealWithExcess> mealsTo2 = filteredByCycles2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            excessMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            LocalDate mealsDate = userMeal.getDateTime().toLocalDate();
            LocalTime mealTime = userMeal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                addUserMeal(filteredMeals, userMeal, new UserMealWithExcess.IsExcess(excessMap.get(mealsDate) > caloriesPerDay));
            }
        }
        return filteredMeals;
    }

    public static List<UserMealWithExcess> filteredByCycles2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessMapInt = new HashMap<>();
        Map<LocalDate, UserMealWithExcess.IsExcess> excessMapBool = new HashMap<>();
        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            excessMapInt.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
            UserMealWithExcess.IsExcess excess;
            if (!excessMapBool.containsKey(userMeal.getDate())) {
                excess = new UserMealWithExcess.IsExcess();
                if (excessMapInt.get(userMeal.getDate()) > caloriesPerDay) {
                    excessMapBool.put(userMeal.getDate(), excess);
                    excess.setResult(true);
                } else {
                    excessMapBool.put(userMeal.getDate(), excess);
                    excess.setResult(false);
                }
            } else {
                excessMapBool.get(userMeal.getDate()).setResult(excessMapInt.get(userMeal.getDate()) > caloriesPerDay);
            }
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                addUserMeal(filteredMeals, userMeal, excessMapBool.get(userMeal.getDate()));
            }
        }
        return filteredMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessMap = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));
        return meals.parallelStream()
                .unordered()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map((userMeal) -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), new UserMealWithExcess.IsExcess(hasExcess(excessMap, userMeal.getDate(), caloriesPerDay))))
                .collect(Collectors.toList());
    }

    private static void addUserMeal(List<UserMealWithExcess> searchMeals, UserMeal
            userMeal, UserMealWithExcess.IsExcess excess) {
        UserMealWithExcess userMealWithExcess = new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
        searchMeals.add(userMealWithExcess);
    }

    private static boolean hasExcess(Map<LocalDate, Integer> excessMap, LocalDate date, int caloriesPerDay) {
        return (excessMap.get(date) > caloriesPerDay);
    }
}
