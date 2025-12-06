package module2.countersuite;

public interface Counter {

    void increment();

    void incrementBy(long delta);

    long getCount();
}