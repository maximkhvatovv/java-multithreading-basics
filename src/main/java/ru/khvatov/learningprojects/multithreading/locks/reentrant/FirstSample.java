package ru.khvatov.learningprojects.multithreading.locks.reentrant;

import ru.khvatov.learningprojects.multithreading.util.Threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class FirstSample {

    public static void main(String[] args) {
        final EvenNumberGenerator generator = new EvenNumberGenerator();
        final Runnable multipleGeneration = () -> {
            IntStream.range(0, 100)
                    .forEach(
                            i -> System.out.printf("generated value = %s\n", generator.generate())
                    );
        };

        final Thread th1 = new Thread(multipleGeneration);
        final Thread th2 = new Thread(multipleGeneration);
        final Thread th3 = new Thread(multipleGeneration);

        Threads.start(th1, th2, th3);
    }

    private static final class EvenNumberGenerator {
        private final Lock lock = new ReentrantLock(true);
        private int previous;

        public EvenNumberGenerator() {
            this.previous = -2;
        }

        public int generate() {
            try {
                lock.lock();
                return previous += 2;
            } finally {
                lock.unlock();
            }
        }
    }

}
