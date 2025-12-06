package module2.countersuite;

import java.util.concurrent.atomic.LongAdder;

public class LongAdderCounter implements Counter {
    LongAdder longAdder = new LongAdder();

    @Override
    public void increment() {
        longAdder.increment();
    }

    @Override
    public void incrementBy(long delta) {
        longAdder.add(delta);
    }

    @Override
    public long getCount() {
        return longAdder.longValue();
    }
}
