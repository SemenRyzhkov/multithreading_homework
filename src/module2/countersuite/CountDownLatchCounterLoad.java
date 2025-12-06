package module2.countersuite;


import utils.Logger;

import java.util.concurrent.CountDownLatch;

public final class CountDownLatchCounterLoad {


    public static void runLoad(Counter counter,
                               int threadsCount,
                               int iterationsPerThread
    ) {
        CountDownLatch latch = new CountDownLatch(threadsCount);

        long start = System.nanoTime();

        for (int i = 0; i < threadsCount; i++) {
            new Thread(new Worker(latch, counter, iterationsPerThread))
                    .start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long finish = System.nanoTime();
        long durationMs = (finish - start) / 1_000_000;


        Logger.log("Impl-%s: expected=%s, actual=%s, timeMs=%s",
                counter.getClass().getSimpleName(),
                threadsCount * iterationsPerThread,
                counter.getCount(),
                durationMs
        );


    }

    record Worker(
            CountDownLatch latch,
            Counter counter,
            int iterations
    ) implements Runnable {

        @Override
        public void run() {
            for (int j = 0; j < iterations; j++) {
                counter.increment();
            }
            latch.countDown();
        }
    }

}
