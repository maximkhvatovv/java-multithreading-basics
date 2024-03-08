package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.impl;

import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.AbstractCounter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterGuardedByReentrantLock extends AbstractCounter {
    public static final int DEFAULT_START_VALUE = 0;
    private final Lock lock;

    public CounterGuardedByReentrantLock() {
        this(DEFAULT_START_VALUE);
    }

    public CounterGuardedByReentrantLock(long value) {
        super(value);
        this.lock = new ReentrantLock();
    }

    @Override
    protected Lock readLock() {
        return getLock();
    }

    @Override
    protected Lock writeLock() {
        return getLock();
    }

    private Lock getLock() {
        return this.lock;
    }
}
