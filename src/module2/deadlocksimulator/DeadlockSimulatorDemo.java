package module2.deadlocksimulator;

import utils.Logger;
import utils.ThreadSleepUtil;

public class DeadlockSimulatorDemo {

    public static void main(String[] args) throws InterruptedException {
        final int iterations = 1000;
        final long pauseBetweenLocksMs = 100;
        long tryLockTimeoutMs = 200;

        LockableResource resourceA = new LockableResource("A", 1);
        LockableResource resourceB = new LockableResource("B", 2);
        LockableResourceProcessor wrongLockableResourceProcessor
                = new WrongLockableResourceProcessor();

        LockableResourceProcessor orderedLockableResourceProcessor
                = new OrderedLockableResourceProcessor();

        LockableResourceProcessor tryLockLockableResourceProcessor = new TryLockLockableResourceProcessor();

//        tryToCreateDeadLock(iterations, wrongLockableResourceProcessor, resourceA, resourceB, pauseBetweenLocksMs);
//        tryToCreateDeadLock(iterations, orderedLockableResourceProcessor, resourceA, resourceB, pauseBetweenLocksMs);
        tryToCreateDeadLock(iterations,tryLockLockableResourceProcessor, resourceA, resourceB, pauseBetweenLocksMs);


        long backoffMs = 20;
    }

    private static void tryToCreateDeadLock(int iterations,
                                            LockableResourceProcessor processor,
                                            LockableResource resourceA,
                                            LockableResource resourceB,
                                            long pauseBetweenLocksMs
    ) throws InterruptedException {
        for (int i = 0; i < iterations; i++) {
            Logger.log("Iteration: %d", i);

            Thread t1 = new Thread(() -> {
                try {
                    processor.processResources(resourceA, resourceB);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Vasya");

            ThreadSleepUtil.safeSleepWithoutThrow(pauseBetweenLocksMs);

            Thread t2 = new Thread(() -> {
                try {
                    processor.processResources(resourceB, resourceA);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Petya");

            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }
    }
}
