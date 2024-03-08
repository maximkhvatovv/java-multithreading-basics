package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock;

import java.util.concurrent.locks.Lock;

public abstract class AbstractCounter implements Counter {
    private long value;

    public AbstractCounter() {}

    public AbstractCounter(long value) {
        this.value = value;
    }

    public final long value() {
        readLock().lock();
        try {
            return this.value;
        } finally {
            readLock().unlock();
        }
    }

    public final void increment() {
        writeLock().lock();
        try {
            value++;
        } finally {
            writeLock().unlock();
        }
    }

    protected abstract Lock readLock();

    protected abstract Lock writeLock();

}
