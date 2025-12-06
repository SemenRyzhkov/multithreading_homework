package module2.countersuite;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCounter implements Counter {
    private long counter;
    private final Lock lock = new ReentrantLock(false);


    @Override
    public void increment() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void incrementBy(long delta) {
        lock.lock();
        try {
            counter += delta;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long getCount() {
        return this.counter;
    }
}
