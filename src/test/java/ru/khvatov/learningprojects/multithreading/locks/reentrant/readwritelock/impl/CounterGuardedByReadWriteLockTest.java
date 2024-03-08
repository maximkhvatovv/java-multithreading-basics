package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.impl;

import org.junit.jupiter.api.Test;
import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.AbstractCounter;
import ru.khvatov.learningprojects.multithreading.util.Threads;

import java.util.concurrent.TimeUnit;

class CounterGuardedByReadWriteLockTest {
    @Test
    void shouldWorkCorrectly() throws InterruptedException {
        final AbstractCounter counter = new CounterGuardedByReadWriteLock(1);
        final Runnable readTask = () -> {
            while (true) {
                final long value = counter.value();
                Threads.printMessageByThread(
                        "getting value: %d".formatted(value)
                );
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        final Thread r1 = new Thread(readTask);
        final Thread r2 = new Thread(readTask);
        final Thread r3 = new Thread(readTask);
        final Thread r4 = new Thread(readTask);

        final Runnable writeTask = () -> {
            while (true) {
                counter.increment();
                Threads.printMessageByThread("incrementing value");
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        final Thread w1 = new Thread(writeTask);

        Threads.start(r1, r2, r3, r4, w1);
        Threads.join(r1, r2, r3, r4, w1);
    }
}