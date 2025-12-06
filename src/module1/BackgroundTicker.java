package module1;

import utils.Logger;
import utils.ThreadJoinUtils;
import utils.ThreadSleepUtil;

public class BackgroundTicker {


    static Thread startDaemon(String name, int periodMillis) {
        Thread t = new Thread(() -> {
            Logger.log("Demon starting");

            while (true) {
                ThreadSleepUtil.safeSleepWithoutThrow(periodMillis);
                Logger.log("Demon %s makes TIK one time per %d ms ", name, periodMillis);
            }

        }, name);

        t.setDaemon(true);
        t.start();
        return t;
    }

    public static void main(String[] args) {
        BackgroundTicker.startDaemon("POKEMON", 150);

        Thread t1 = new Thread(() -> {
            ThreadSleepUtil.safeSleepWithoutThrow(2500);
            Logger.log("Thread %s starting", Thread.currentThread().getName());
        }, "t1");

        Thread t2 = new Thread(() -> {
            ThreadSleepUtil.safeSleepWithoutThrow(2000);

            Logger.log("Thread %s starting", Thread.currentThread().getName());
        }, "t2");

        Thread t3 = new Thread(() -> {
            ThreadSleepUtil.safeSleepWithoutThrow(1500);
            Logger.log("Thread %s starting", Thread.currentThread().getName());
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        ThreadJoinUtils.safeJoin(t1);
        ThreadJoinUtils.safeJoin(t2);
        ThreadJoinUtils.safeJoin(t3);

        Logger.log("main ended");

    }
}
