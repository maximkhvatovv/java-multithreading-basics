package ru.khvatov.learningprojects.multithreading.locks.condition;

import ru.khvatov.learningprojects.multithreading.util.Threads;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class BoundedBufferInMultithreadedEnvironment {
    public static void main(String[] args) {
        final BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(5);

        final Runnable producingTask = () -> {
            Stream.iterate(0, i -> ++i).forEach(i -> {
                boundedBuffer.put(i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        };
        final Thread producingThread = new Thread(producingTask);

        final Runnable consumingTask = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                boundedBuffer.take();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        };
        final Thread consumingThread = new Thread(consumingTask);

        Threads.start(producingThread, consumingThread);

    }
}
