package module2.deadlocksimulator;

import utils.Logger;
import utils.ThreadSleepUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockLockableResourceProcessor
        implements LockableResourceProcessor {

    @Override
    public void processResources(LockableResource r1,
                                 LockableResource r2
    ) throws InterruptedException {


        while (true) {
            try {
                ReentrantLock lock1 = r1.getLock();
                ReentrantLock lock2 = r2.getLock();

                Logger.log("Thread %s tries to take lock resource %s.",
                        Thread.currentThread().getName(), r1.getName());

                boolean tryLock1 = lock1.tryLock(10, TimeUnit.MILLISECONDS);
                ThreadSleepUtil.safeSleepRandomMillis(50, 150);

                if (!tryLock1) {
                    Logger.log("Thread %s cant take lock resource %s. Retry..",
                            Thread.currentThread().getName(), r1.getName());

                    ThreadSleepUtil.safeSleepRandomMillis(5, 15);

                    continue;
                } else {
                    Logger.log("Thread %s took lock resource %s.",
                            Thread.currentThread().getName(), r1.getName());
                }

                Logger.log("Thread %s tries to take lock resource %s.",
                        Thread.currentThread().getName(), r2.getName());
                boolean tryLock2 = lock2.tryLock(10, TimeUnit.MILLISECONDS);
//                ThreadSleepUtil.safeSleepRandomMillis(50, 150);

                if (!tryLock2) {
                    Logger.log("Thread %s cant take lock resource %s. Retry..",
                            Thread.currentThread().getName(), r2.getName());
                    lock1.unlock();
//                    ThreadSleepUtil.safeSleepRandomMillis(5, 15);
                    continue;
                } else {
                    Logger.log("Thread %s took lock resource %s.",
                            Thread.currentThread().getName(), r2.getName());
                }

                Logger.log("Thread %s happily took lock resources %s and %s. The end",
                        Thread.currentThread().getName(), r1.getName(), r2.getName());
                return;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (r1.getLock().isHeldByCurrentThread()) {
                    r1.getLock().unlock();
                }
                if (r2.getLock().isHeldByCurrentThread()) {
                    r2.getLock().unlock();
                }
            }
        }
    }

}


