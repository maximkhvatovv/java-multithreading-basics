package ru.khvatov.learningprojects.multithreading.util;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

public final class Threads {
    public static void start(Thread... threads) {
        stream(threads).forEach(Thread::start);
    }

    public static void join(Thread... threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void printMessageByThread(String message) {
        out.printf("Thread `%s`: %s\n", currentThread().getName(), message);
    }
}
