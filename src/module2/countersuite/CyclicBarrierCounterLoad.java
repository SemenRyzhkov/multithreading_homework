package module2.countersuite;


// 1) создать threadsCount потоков
// 2) синхронизировать старт (CountDownLatch/CyclicBarrier)
// 3) каждый поток делает iterationsPerThread increment()
// 4) засечь время через System.nanoTime()
// 5) дождаться завершения всех потоков
// 6) посчитать expected = threadsCount * iterationsPerThread
// 7) напечатать в одну строку: // "<Impl-X>: expected=<...>, actual=<...>, timeMs=<...>"   }

import utils.Logger;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class CyclicBarrierCounterLoad {


    public static void runLoad(Counter counter,
                               int threadsCount,
                               int iterationsPerThread
    ) {
        CyclicBarrier barrier = new CyclicBarrier(threadsCount, () -> {
            long finish = System.nanoTime();
            long durationMs = (finish - Shared.start) / 1_000_000;

            Logger.log("Impl-%s: expected=%s, actual=%s, timeMs=%s",
                    counter.getClass().getSimpleName(),
                    threadsCount * iterationsPerThread,
                    counter.getCount(),
                    durationMs
            );
        });

        Shared.start = System.nanoTime();

        for (int i = 0; i < threadsCount; i++) {
            new Thread(new Worker(barrier, counter, iterationsPerThread))
                    .start();
        }

    }

    private static void awaitBarrier(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException("Broken barrier");
        }
    }

    static class Worker implements Runnable {
        private final CyclicBarrier barrier;
        private final Counter counter;
        private final int iterations;

        public Worker(CyclicBarrier barrier, Counter counter, int iterations) {
            this.barrier = barrier;
            this.counter = counter;
            this.iterations = iterations;
        }

        @Override
        public void run() {
            for (int j = 0; j < iterations; j++) {
                counter.increment();
            }
            Logger.log("Currently waiting: %d", barrier.getNumberWaiting());

            awaitBarrier(barrier);
            Logger.log("Passed barrier");

        }


    }

    private static class Shared {
        static volatile long start;
    }


}
