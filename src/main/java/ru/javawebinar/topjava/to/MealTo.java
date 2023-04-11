package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import ru.javawebinar.topjava.web.json.LocalDateTimeDeserializer;
import ru.javawebinar.topjava.web.json.LocalDateTimeSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;

@Validated
public class MealTo extends BaseTo {


    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;

    @NotBlank
    @Size(min = 2, max = 120)
    private String description;

    @NotNull
    @Range(min = 10, max = 5000)
    private Integer calories;

    private boolean excess;

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    public MealTo() {
    };

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return excess == mealTo.excess
                && dateTime.equals(mealTo.dateTime)
                && description.equals(mealTo.description)
                && calories.equals(mealTo.calories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
