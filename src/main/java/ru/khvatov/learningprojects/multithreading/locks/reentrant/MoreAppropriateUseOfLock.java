package ru.khvatov.learningprojects.multithreading.locks.reentrant;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static ru.khvatov.learningprojects.multithreading.util.Threads.join;
import static ru.khvatov.learningprojects.multithreading.util.Threads.printMessageByThread;
import static ru.khvatov.learningprojects.multithreading.util.Threads.start;

public class MoreAppropriateUseOfLock {
    public static void main(String[] args) throws InterruptedException {
        final Counter counter = new Counter();

        final int incrementCount = 10;
        final Thread incrementingThread = new Thread(doOperationOnCounter(counter, i -> counter.increment(), incrementCount));

        final int decrementCount = 10;
        final Thread decrementingThread = new Thread(doOperationOnCounter(counter, i -> counter.decrement(), decrementCount));

        start(incrementingThread, decrementingThread);


        join(incrementingThread, decrementingThread);

        printMessageByThread(counter.toString());

    }

    public static Runnable doOperationOnCounter(
            final Counter counter,
            final IntConsumer nthOperation,
            final int n
    ) {
        return () -> {
            counter.lock();
            IntStream.range(0, n).forEach(nthOperation);
            counter.unlock();
        };
    }

    private static class Counter {
        private final Lock lock = new ReentrantLock();
        private long value;

        public void lock() {
            lock.lock();
            printMessageByThread("Counter Lock is acquired");
        }

        public void increment() {
            value++;
            printMessageByThread("Counter is incremented: value=%d".formatted(value));
        }

        public void decrement() {
            value--;
            printMessageByThread("Counter is decremented: value=%d".formatted(value));
        }

        public void unlock() {
            printMessageByThread("Counter Lock is released");
            lock.unlock();
        }

        public long getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Counter{" +
                    "value=" + value +
                    '}';
        }
    }
}
