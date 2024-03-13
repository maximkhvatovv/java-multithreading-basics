package ru.khvatov.learningprojects.multithreading.util;

import lombok.NonNull;

import java.io.PrintStream;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

public final class Threads {
    public static void start(final Thread... threads) {
        stream(threads).forEach(Thread::start);
    }

    public static void join(final Thread... threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void interrupt(final Thread... threads) {
        stream(threads)
                .forEach(Thread::interrupt);
    }

    public static void printMessageByThread(final String message) {
        printMessageByThread(System.out, message);
    }

    public static void printMessageByThread(@NonNull final PrintStream out, @NonNull final String message) {
        out.printf("Thread `%s`: %s\n", currentThread().getName(), message);
    }
}
