package utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Утилита для имитации "HTTP-вызова" без реальной сети:
 */
public final class ExternalCallSimulationUtil {

    private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(8);

    private ExternalCallSimulationUtil() {
    }

    /**
     * Имитирует "вызов" с CPU-нагрузкой, долгой паузой и ещё одной CPU-нагрузкой.
     *
     * @param preCpuMillis  сколько миллисекунд крутить CPU перед "запросом"
     * @param ioDelayMillis сколько миллисекунд "ждать сеть"
     * @param postCpuMillis сколько миллисекунд крутить CPU после "ответа"
     */
    public static void simulateCall(
            int preCpuMillis,
            int ioDelayMillis,
            int postCpuMillis
    ) {
        // CPU нагрузка перед вызовом
        BusyCpuUtil.spinOnCpuMillis(preCpuMillis);
        // Долгая I/O-задержка
        ThreadSleepUtil.safeSleepWithoutThrow(ioDelayMillis);
        // CPU нагрузка после ответа
        BusyCpuUtil.spinOnCpuMillis(postCpuMillis);
    }

    public static void simulateCallWithoutCpuBurn() {
        simulateCall(
                0,
                ThreadLocalRandom.current().nextInt(10, 20),
                0
        );
    }

    public static void simulateRandomCall() {
        simulateCall(
                ThreadLocalRandom.current().nextInt(1,5),
                ThreadLocalRandom.current().nextInt(10, 100),
                ThreadLocalRandom.current().nextInt(1, 5)
        );
    }

    public static CompletableFuture<String> simulateExternalCallAsync(
            String taskName,
            String returnValue
    ) {
        Logger.log("taskName={%s} scheduled", taskName);
        return CompletableFuture.supplyAsync(() -> {
            Logger.log("taskName={%s} started", taskName);
            simulateRandomCall(); // задержка
            Logger.log("taskName={%s} completed", taskName);
            return returnValue;
        }, IO_EXECUTOR);
    }

    public static CompletableFuture<Integer> simulateExternalCallAsync(
            String taskName,
            Integer returnValue
    ) {
        Logger.log("taskName={%s} scheduled", taskName);
        return CompletableFuture.supplyAsync(() -> {
            Logger.log("taskName={%s} started", taskName);
            simulateRandomCall(); // задержка
            Logger.log("taskName={%s} completed", taskName);
            return returnValue;
        }, IO_EXECUTOR);
    }
}
