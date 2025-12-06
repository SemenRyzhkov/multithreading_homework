package module2.countersuite;

public class SynchronizedCounter implements Counter {
    private long counter = 0;

    @Override
    public synchronized void increment() {
        counter++;
    }

    @Override
    public synchronized void incrementBy(long delta) {
        counter += delta;
    }

    @Override
    public long getCount() {
        return counter;
    }
}
