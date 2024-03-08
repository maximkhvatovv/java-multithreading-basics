package ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.benchmark;

import ru.khvatov.learningprojects.multithreading.locks.reentrant.readwritelock.Counter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimpleCounterReadsBenchmark
        implements CounterReadsBenchmark {
    private final Counter counter;
    private final int numOfReaders;
    private final int numOfWriters;

    private final int benchmarkTime;
    private final TimeUnit benchmarkTimeUnit;
    private int writerThreadIdSeq;
    private int readerThreadIdSeq;

    public SimpleCounterReadsBenchmark(final int numOfWriters,
                                       final int numOfReaders,
                                       final Supplier<? extends Counter> counterSupplier,
                                       final TimeUnit benchmarkTimeUnit,
                                       final int benchmarkTime
    ) {
        this.counter = counterSupplier.get();
        this.numOfReaders = numOfReaders;
        this.numOfWriters = numOfWriters;
        this.benchmarkTimeUnit = benchmarkTimeUnit;
        this.benchmarkTime = benchmarkTime;
        this.writerThreadIdSeq = this.readerThreadIdSeq = 1;
    }

    @Override
    public long totalReadCount() {
        final List<ReadCountingTask> readCountingTasks = IntStream.range(0, numOfReaders)
                .mapToObj(i -> new ReadCountingTask(this.counter))
                .toList();

        final List<Thread> readThreads = readCountingTasks.stream()
                .map(this::createReadThread)
                .toList();

        final List<Thread> writeThreads = IntStream.range(0, numOfWriters)
                .mapToObj(i -> createWriterThread())
                .toList();

        Stream.concat(readThreads.stream(), writeThreads.stream())
                .forEach(Thread::start);

        try {
            benchmarkTimeUnit.sleep(this.benchmarkTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread `%s` is interrupted".formatted(Thread.currentThread()), e);
        }

        Stream.concat(readThreads.stream(), writeThreads.stream())
                .forEach(Thread::interrupt);

        readThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return readCountingTasks.stream()
                .mapToLong(ReadCountingTask::readCount)
                .sum();
    }


    private Thread createWriterThread() {
        return new Thread(
                new IncrementingTask(this.counter),
                "writeThread#%d".formatted(this.writerThreadIdSeq++)
        );
    }

    private record IncrementingTask(Counter counter)
            implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                counter.increment();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private Thread createReadThread(final ReadCountingTask task) {
        return new Thread(
                new ReadCountingTask(task.counter),
                "readThread#%d".formatted(this.readerThreadIdSeq++)
        );
    }

    private final static class ReadCountingTask
            implements Runnable {
        private final Counter counter;

        private int readCount;

        private ReadCountingTask(final Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                final long value = counter.value();
                readCount++;
            }
        }

        public int readCount() {
            return this.readCount;
        }

    }

}
