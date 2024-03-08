package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.impl;

import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.AbstractCounter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CounterGuardedByReentrantReadWriteLock extends AbstractCounter {
    public static final int DEFAULT_START_VALUE = 0;
    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    public CounterGuardedByReentrantReadWriteLock() {
        this(DEFAULT_START_VALUE);
    }

    public CounterGuardedByReentrantReadWriteLock(final long value) {
        super(value);

        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();

    }

    @Override
    protected Lock readLock() {
        return this.readLock;
    }

    @Override
    protected Lock writeLock() {
        return this.writeLock;
    }
}
