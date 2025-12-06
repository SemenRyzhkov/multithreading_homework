package module1;

import utils.Logger;

import java.util.ArrayList;
import java.util.List;

interface Counter {
    void inc();

    long value();
}


public class MutexCounterDemo {
    static class UnsafeCounter implements Counter {
        private long value;

        @Override
        public void inc() {
            value++;
        }

        @Override
        public long value() {
            return value;
        }

        static void runRace(int threads, int itersPerThread) {
            Counter counter = new UnsafeCounter();
            List<Thread> threadList = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                Thread t1 = new Thread(() -> {
                    for (int j = 0; j < itersPerThread; j++) {
                        counter.inc();
                    }
                }, "user-thread-" + i);
                threadList.add(t1);
            }
            threadList.forEach(Thread::start);
            threadList.forEach(t -> {

                try {
                    t.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            });
            Logger.log("actual unsafe counter value: " + counter.value());

        }
    }

    static class SynchronizedCounter implements Counter {
        private long value;

        @Override
        public synchronized void inc() {
            value++;
        }

        @Override
        public long value() {
            return value;
        }

        static void runRace(int threads, int itersPerThread) {
            Counter counter = new SynchronizedCounter();
            List<Thread> threadList = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                Thread t1 = new Thread(() -> {
                    for (int j = 0; j < itersPerThread; j++) {
                        counter.inc();
                    }
                }, "user-thread-" + i);
                threadList.add(t1);
            }
            threadList.forEach(Thread::start);
            threadList.forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Logger.log("actual synchronized counter value: " + counter.value());

        }
    }

    public static void main(String[] args) throws InterruptedException {
        UnsafeCounter.runRace(8, 100_000);
        SynchronizedCounter.runRace(8, 100_000);

    }

}

