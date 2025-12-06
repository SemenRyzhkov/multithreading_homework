package module2.deadlocksimulator;

import utils.Logger;
import utils.ThreadSleepUtil;

public class WrongLockableResourceProcessor implements LockableResourceProcessor {

    @Override
    public void processResources(LockableResource r1,
                                 LockableResource r2
    ) throws InterruptedException {
        synchronized (r1.getMonitor()) {
            Logger.log("Thread %s took monitor. Resource %s is locked. Go sleep",
                    Thread.currentThread().getName(), r1.getName());
            ThreadSleepUtil.safeSleepWithoutThrow(150);
            Logger.log("Thread %s tries to take resource %s",
                    Thread.currentThread().getName(), r2.getName());
            synchronized (r2.getMonitor()) {
                Logger.log("Thread %s took monitor. Resource %s is locked. Go sleep",
                        Thread.currentThread().getName(), r2.getName());
                ThreadSleepUtil.safeSleepWithoutThrow(150);
                Logger.log("Thread %s go to exit", Thread.currentThread().getName());
            }
        }
    }
}
