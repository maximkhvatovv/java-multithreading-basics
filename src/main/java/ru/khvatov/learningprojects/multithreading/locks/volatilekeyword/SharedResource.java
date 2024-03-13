package ru.khvatov.learningprojects.multithreading.locks.volatilekeyword;

import lombok.Getter;
import ru.khvatov.learningprojects.multithreading.util.Threads;

@Getter
public class SharedResource {
    private volatile boolean shouldPrint;

    public SharedResource() {
        this.shouldPrint = true;
    }

    public void shouldNot() {
        shouldPrint = false;
        Threads.printMessageByThread(System.out, "shouldPrint set to false");
    }
}
