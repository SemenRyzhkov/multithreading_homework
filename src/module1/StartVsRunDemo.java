package module1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class StartVsRunDemo {

    private static final Random RAND = new Random();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static void log(String message) {
        String time = LocalTime.now().format(TIME_FORMAT);
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + time + "] [" + threadName + "] " + message);
    }

    static class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            log("начало (наследование Thread)");
            try {
                Thread.sleep(10 + RAND.nextInt(71)); // 10..80 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log("завершение (наследование Thread)");
        }
    }

    static class MyRunnable implements Runnable {
        private final String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            log("начало (реализация Runnable) [" + name + "]");
            try {
                Thread.sleep(10 + RAND.nextInt(71));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log("завершение (реализация Runnable) [" + name + "]");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread("InheritedThread");
        Thread t2 = new Thread(new MyRunnable("RunnableTask"), "RunnableThread");
        Thread t3 = new Thread(() -> {
            log("начало (лямбда)");
            try {
                Thread.sleep(10 + RAND.nextInt(71));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log("завершение (лямбда)");
        }, "LambdaThread");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

    }
}