package utils;

import java.util.function.Supplier;

/**
 * Утилиты для измерений
 */
public final class MeasurementUtils {

    private MeasurementUtils() {
    }

    /**
     * Измеряет и логирует длительность выполнения переданной задачи
     */
    public static <T> T measure(
            String taskName,
            Supplier<T> task
    ) {
        var startNanos = System.nanoTime();
        try {
            return task.get();
        } catch (Exception e) {
            Logger.log("Exception in task-name: %s message:", taskName, e.getMessage());
            throw e;
        } finally {
            var endNanos = System.nanoTime();
            var timeElapsed = (endNanos - startNanos) / 1_000_000;
            Logger.log("Time: task-name: %s time: %sms", taskName, timeElapsed);
        }
    }

    /**
     * Измеряет и логирует длительность выполнения переданной задачи
     */
    public static void measure(
            String taskName,
            Runnable task
    ) {
        var startNanos = System.nanoTime();
        try {
            task.run();
        } catch (Exception e) {
            Logger.log("Exception in task-name: %s message:", taskName, e.getMessage());
            throw e;
        } finally {
            var endNanos = System.nanoTime();
            var timeElapsed = (endNanos - startNanos) / 1_000_000;
            Logger.log("Time: task-name: %s time: %sms", taskName, timeElapsed);
        }
    }
}
