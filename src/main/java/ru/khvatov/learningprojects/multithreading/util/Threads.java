package ru.khvatov.learningprojects.multithreading.util;

import java.util.Arrays;

public final class Threads {
    public static void start(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }
}
