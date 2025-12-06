package module1;

import utils.Logger;

public class LoopWorker {

    private String name;
    private Thread thread;

    public LoopWorker(String name) {
        this.name = name;
    }

    void start() {
        thread = new Thread(this::runLoop, name);
        thread.start();
    }

    void stopAsync() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private void runLoop() {
        int count = 0;
        while (!Thread.currentThread().isInterrupted()) {
            Logger.log("Thread %s is running with count=%d", name, count);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Logger.log("Thread %s is interrupted", name);
                Thread.currentThread().interrupt();
                break;
            }
            count++;

        }
    }


    public static void main(String[] args) throws InterruptedException {
        LoopWorker loopWorker = new LoopWorker("worker-1");
        loopWorker.start();
        Thread.sleep(3000);
        loopWorker.stopAsync();
    }
}
