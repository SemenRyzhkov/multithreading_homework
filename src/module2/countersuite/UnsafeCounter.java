package module2.countersuite;

public class UnsafeCounter implements Counter {
    private long counter = 0;


    @Override
    public void increment() {
        counter++;
    }

    @Override
    public void incrementBy(long delta) {
        counter += delta;
    }

    @Override
    public long getCount() {
        return counter;
    }
}
