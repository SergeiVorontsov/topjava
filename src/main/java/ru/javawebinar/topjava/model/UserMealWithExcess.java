package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final IsExcess excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, IsExcess excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess.getResult() +
                '}';
    }

    public static class IsExcess {
        private boolean result;

        public IsExcess() {
        }

        public IsExcess(boolean b) {
            result = b;
        }

        public boolean getResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }
}
