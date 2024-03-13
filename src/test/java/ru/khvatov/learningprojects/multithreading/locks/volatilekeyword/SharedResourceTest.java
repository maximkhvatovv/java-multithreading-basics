package ru.khvatov.learningprojects.multithreading.locks.volatilekeyword;

import org.junit.jupiter.api.Test;
import ru.khvatov.learningprojects.multithreading.util.Threads;

import java.util.concurrent.TimeUnit;

class SharedResourceTest {
    @Test
    void testWithVolatile() throws InterruptedException {
        final var sharedResource = new SharedResource();

        final var printingThread = new Thread(
                new PrintingTask(sharedResource), "printing-thread"
        );

        final var stoppingPrintingThread = new Thread(
                new StoppingPrintingTask(sharedResource, TimeUnit.SECONDS, 2), "stopping-thread"
        );

        Threads.start(printingThread, stoppingPrintingThread);
        Threads.join(printingThread, stoppingPrintingThread);
    }
}