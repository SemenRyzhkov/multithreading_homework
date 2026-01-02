package module3;

import utils.Logger;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class VirtualThreadAdvantageDemo {

    // Параметры, которые делают I/O-bound нагрузку явной
    public static final int TASK_COUNT = 10_000;
    public static final int IO_DELAY_MS = 100;   // имитация долгого I/O (сеть, файл и т.д.)
    public static final int CPU_WORK_MS = 1;     // лёгкая CPU-нагрузка
    public static final int POOL_SIZE = 100;     // типичный размер пула

    // Блокирующая I/O операция — симулируем задержку
    private static String blockingIoOperation(int taskId) {
        try {
            Thread.sleep(IO_DELAY_MS); // имитация ожидания ответа от БД, сети и т.п.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Result from task " + taskId;
    }

    // CPU-работа — симулируем короткую нагрузку
    private static String cpuIntensiveWork(String input) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < CPU_WORK_MS) {
            // busy loop — имитация вычислений
            Math.sin(Math.random());
        }
        return input + " [processed]";
    }

    public static void main(String[] args) {
        Logger.log("Запуск сравнения: %d задач, I/O = %d мс, CPU = %d мс", TASK_COUNT, IO_DELAY_MS, CPU_WORK_MS);

        // === Тест 1: FixedThreadPool (обычные потоки) ===
        Logger.log("Запуск FixedThreadPool (%d потоков)...", POOL_SIZE);
        ExecutorService fixedPool = Executors.newFixedThreadPool(POOL_SIZE);

        long start1 = System.nanoTime();
        try {
            // Создаём 10k задач — но пул всего из 100 потоков
            IntStream.range(0, TASK_COUNT)
                    .forEach(i -> fixedPool.submit(() -> {
                        String result = blockingIoOperation(i);
                        result = cpuIntensiveWork(result);
                        if (i < 5) {
                            Logger.log("[Fixed] %s on thread: %s", result, Thread.currentThread().getName());
                        }
                    }));
        } finally {
            fixedPool.shutdown();
        }

        try {
            fixedPool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long end1 = System.nanoTime();
        double time1 = (end1 - start1) / 1_000_000_000.0; // в секундах

        Logger.log("✅ FixedThreadPool завершён за %.2f с", time1);

        // === Тест 2: Virtual Threads ===
        Logger.log("\nЗапуск Virtual Threads (10k задач)...");
        long start2 = System.nanoTime();

        try (ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, TASK_COUNT)
                    .forEach(i -> virtualThreads.submit(() -> {
                        String result = blockingIoOperation(i);
                        result = cpuIntensiveWork(result);
                        if (i < 5) {
                            Logger.log("[VT] %s on thread: %s", result, Thread.currentThread().getName());
                        }
                    }));
            // shutdown автоматически при выходе из try-with-resources
        } // awaitTermination не нужен — join() внутри

        long end2 = System.nanoTime();
        double time2 = (end2 - start2) / 1_000_000_000.0; // в секундах

        Logger.log("✅ Virtual Threads завершены за %.2f с", time2);

        // === Сравнение ===
        Logger.log("\n--- ИТОГИ ---");
        Logger.log("Fixed Pool     : %.2f с", time1);
        Logger.log("Virtual Threads: %.2f с", time2);
        if (time1 < time2) {
            double diff = (time2 - time1) / time2 * 100;
            Logger.log("Fixed быстрее на %.1f%% — НЕОЖИДАННО", diff);
        } else {
            double diff = (time1 - time2) / time1 * 100;
            Logger.log("✅ VT быстрее на %.1f%%", diff);
        }

        Logger.log("Вывод: Virtual Threads в разы быстрее при I/O-bound нагрузке и большом числе задач.");
    }
}