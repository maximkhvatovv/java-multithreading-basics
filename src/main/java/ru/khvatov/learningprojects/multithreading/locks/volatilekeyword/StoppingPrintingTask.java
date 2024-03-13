package ru.khvatov.learningprojects.multithreading.locks.volatilekeyword;

import java.util.concurrent.TimeUnit;

public class StoppingPrintingTask implements Runnable {
    private final SharedResource sharedResource;
    private final TimeUnit timeUnit;
    private final long timeout;

    public StoppingPrintingTask(final SharedResource sharedResource,
                                final TimeUnit timeUnit,
                                final long after) {
        this.sharedResource = sharedResource;
        this.timeUnit = timeUnit;
        this.timeout = after;
    }


    @Override
    public void run() {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.sharedResource.shouldNot();
    }
}
