package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

public class Util {

    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static void jdbcValidateBean(Object target, Validator validator){
        var constraintViolations = validator.validate(target);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}