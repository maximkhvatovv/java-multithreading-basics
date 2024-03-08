package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.benchmark.CounterReadsBenchmark;
import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.benchmark.SimpleCounterReadsBenchmark;
import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.impl.CounterGuardedByReentrantLock;
import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.impl.CounterGuardedByReentrantReadWriteLock;

import java.util.concurrent.TimeUnit;

class CounterTest {

    @Test
    void readsCountShouldBeMuchMoreWithReentrantLockThanReentrantReadWriteLock(){

        final int numOfWrites = 6;
        final int numOfReads = 100;
        final TimeUnit benchmarkTimeUnit = TimeUnit.SECONDS;
        final int benchmarkTime = 5;

        final CounterReadsBenchmark counterReadsBenchmarkWithGuardedByLock = new SimpleCounterReadsBenchmark(
                numOfWrites,
                numOfReads,
                CounterGuardedByReentrantLock::new,
                benchmarkTimeUnit,
                benchmarkTime
        );
        final long totalReadCountWithGuardedByLock = counterReadsBenchmarkWithGuardedByLock.totalReadCount();

        final CounterReadsBenchmark counterReadsBenchmarkWithGuardedByReadWriteLock = new SimpleCounterReadsBenchmark(
                numOfWrites,
                numOfReads,
                CounterGuardedByReentrantReadWriteLock::new,
                benchmarkTimeUnit,
                benchmarkTime
        );

        final long totalReadCountWithGuardedByReadWriteLock = counterReadsBenchmarkWithGuardedByReadWriteLock.totalReadCount();
        Assertions.assertTrue(totalReadCountWithGuardedByReadWriteLock < totalReadCountWithGuardedByLock + 50_000);
    }

}