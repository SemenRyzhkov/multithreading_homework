package module1;

import utils.Logger;
import utils.ThreadSleepUtil;

public class VolatileFlagDemo {
    private
//    volatile
    boolean running = true;

    void start() {

        Thread t = new Thread(() -> {
            while (running) {

            }

            Logger.log("Goodbye!");

        }, "user-thread");

        t.start();
    }

    void stop() {
        Logger.log("Stopping");
        running = false;
    }


    public static void main(String[] args) throws InterruptedException {
        VolatileFlagDemo demo = new VolatileFlagDemo();

        demo.start();
        ThreadSleepUtil.safeSleepWithoutThrow(1000);

        demo.stop();

        Logger.log("Stopped");
    }

}
