package ru.khvatov.learningprojects.multithreading.locks.volatilekeyword;

import ru.khvatov.learningprojects.multithreading.util.Threads;

import java.util.concurrent.TimeUnit;

import static ru.khvatov.learningprojects.multithreading.util.Threads.printMessageByThread;

public class PrintingTask implements Runnable {
    private final SharedResource sharedResource;
    private int i;

    public PrintingTask(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        while (sharedResource.isShouldPrint()) {
            if (Thread.currentThread().isInterrupted()) {
                printMessageByThread(System.err, "is interrupted");
                break;
            }
            Threads.printMessageByThread("Print#%d".formatted(i++));
            try {
                TimeUnit.MILLISECONDS.sleep(100L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
