package module2.countersuite;

public class CountersLoadTesting {
    private static final int THREADS = 8;
    private static final int ITERS_PER_THREAD = 50_000;

    public static void main(String[] args) {
        Counter unsafeCounter = new UnsafeCounter();
        Counter synchronizedCounter = new SynchronizedCounter();
        Counter atomicCounter = new AtomicCounter();
        ReentrantLockCounter reentrantLockCounter = new ReentrantLockCounter();
        LongAdderCounter longAdderCounter = new LongAdderCounter();

//        CyclicBarrierCounterLoad.runLoad(unsafeCounter, THREADS, ITERS_PER_THREAD);
//        CyclicBarrierCounterLoad.runLoad(synchronizedCounter, THREADS, ITERS_PER_THREAD);

        CountDownLatchCounterLoad.runLoad(unsafeCounter, THREADS, ITERS_PER_THREAD);
        CountDownLatchCounterLoad.runLoad(synchronizedCounter, THREADS, ITERS_PER_THREAD);
        CountDownLatchCounterLoad.runLoad(atomicCounter, THREADS, ITERS_PER_THREAD);
        CountDownLatchCounterLoad.runLoad(reentrantLockCounter, THREADS, ITERS_PER_THREAD);
        CountDownLatchCounterLoad.runLoad(longAdderCounter, THREADS, ITERS_PER_THREAD);
    }
}
