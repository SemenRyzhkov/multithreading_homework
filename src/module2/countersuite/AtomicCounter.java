package module2.countersuite;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter implements Counter {
    AtomicLong counter = new AtomicLong(0);

    @Override
    public void increment() {
        counter.incrementAndGet();
    }

    @Override
    public void incrementBy(long delta) {
        counter.addAndGet(delta);
    }

    @Override
    public long getCount() {
        return counter.get();
    }
}
