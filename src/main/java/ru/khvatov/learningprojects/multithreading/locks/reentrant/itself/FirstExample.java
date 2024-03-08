package ru.khvatov.learningprojects.multithreading.locks.reentrant.itself;

import ru.khvatov.learningprojects.multithreading.util.Threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

import static java.util.stream.IntStream.range;
import static ru.khvatov.learningprojects.multithreading.util.Threads.printMessageByThread;

public class FirstExample {

    public static void main(String[] args) throws InterruptedException {
        final EvenNumberGenerator generator = new EvenNumberGenerator();

        final int n = 100;
        final IntConsumer operation = i -> printMessageByThread("generated value=%s".formatted(generator.generate()));
        final Runnable multipleGeneration = performOperationNTimes(operation, n);

        final Thread th1 = new Thread(multipleGeneration);
        final Thread th2 = new Thread(multipleGeneration);
        final Thread th3 = new Thread(multipleGeneration);

        Threads.start(th1, th2, th3);
        Threads.join(th1, th2, th3);
    }

    private static Runnable performOperationNTimes(final IntConsumer operation, final int n) {
        return () -> range(0, n).forEach(operation);
    }

    private static final class EvenNumberGenerator {
        private final Lock lock = new ReentrantLock();
        private int previous;

        public EvenNumberGenerator() {
            this.previous = -2;
        }

        public int generate() {
            return lock.tryLock() ? onSuccessfulAcquiringLock() : onFailInAcquiringLock();
        }

        private int onSuccessfulAcquiringLock() {
            try {
                return previous += 2;
            } finally {
                lock.unlock();
            }
        }

        private int onFailInAcquiringLock() {
            printMessageByThread("Failed to acquire the lock.");
            throw new RuntimeException("wtf");
        }
    }

}
