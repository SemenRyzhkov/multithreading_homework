package utils;

/**
 * Утилита искусственной загрузки CPU (busy-wait) на заданное время.
 */
public final class BusyCpuUtil {

    private BusyCpuUtil() {}

    public static void spinOnCpuMillis(long ms) {
        long end = System.nanoTime() + ms * 1_000_000L;
        long acc = 0;
        while (System.nanoTime() < end) {
            // немного бесполезной арифметики, чтобы нагрузить ALU
            acc += (acc * 31 + 7);
        }
        if (acc == 42) System.out.print(""); // не даёт JIT выкинуть цикл
    }

}

