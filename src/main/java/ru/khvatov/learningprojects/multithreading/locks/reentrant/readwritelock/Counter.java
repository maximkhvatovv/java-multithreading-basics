package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock;

public interface Counter {
    long value();

    void increment();
}
