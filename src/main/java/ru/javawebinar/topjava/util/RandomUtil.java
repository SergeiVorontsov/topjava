package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class RandomUtil {

    private static final AtomicInteger ATOMIC_COUNTER = new AtomicInteger(1);

    public static Integer createId() {
        return ATOMIC_COUNTER.getAndIncrement();
    }
}