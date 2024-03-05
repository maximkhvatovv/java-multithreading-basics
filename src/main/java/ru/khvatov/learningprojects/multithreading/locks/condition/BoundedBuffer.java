package ru.khvatov.learningprojects.multithreading.locks.condition;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;

public class BoundedBuffer<T> {
    private final Lock lock;
    private final Condition condition;
    private final T[] elements;
    private int insertionIndex;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(final int capacity) {
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.elements = (T[]) new Object[capacity];
    }

    public int size() {
        this.lock.lock();
        try {
            return insertionIndex;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isFull() {
        this.lock.lock();
        try {
            return this.size() == elements.length;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isEmpty() {
        this.lock.lock();
        try {
            return this.size() == 0;
        } finally {
            this.lock.unlock();
        }
    }

    public void put(final T elem) {
        this.lock.lock();
        try {
            while (this.isFull()) {
                this.condition.await();
            }
            elements[insertionIndex] = elem;
            insertionIndex++;
            out.printf(
                    "%s put %s into buffer, result buffer = %s\n",
                    currentThread().getName(), elem, this
            );
            this.condition.signalAll();
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
        } finally {
            this.lock.unlock();
        }
    }

    public T take() {
        this.lock.lock();
        try {
            while (this.isEmpty()) {
                this.condition.await();
            }

            final int lastIndex = insertionIndex - 1;
            final T elem = elements[lastIndex];
            insertionIndex--;
            elements[lastIndex] = null;
            out.printf(
                    "%s take %s from buffer, result buffer = %s\n",
                    currentThread().getName(), elem, this
            );
            condition.signalAll();
            return elem;
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
            throw new RuntimeException(exception);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return "BoundedBuffer{" +
                    "elements=" + Arrays.toString(Arrays.copyOf(this.elements, this.insertionIndex)) +
                    '}';
        } finally {
            lock.unlock();
        }
    }
}
