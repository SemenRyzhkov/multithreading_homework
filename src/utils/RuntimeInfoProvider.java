package utils;

/**
 * Быстрый вывод райнтайм информации по CPU/памяти процесса.
 */
public final class RuntimeInfoProvider {

    private RuntimeInfoProvider() {
    }

    public static void printRuntimeInfo() {
        int cores = Runtime.getRuntime().availableProcessors();
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        Logger.log(
                "cores=%d, memory(max/total/free)=%dMB/%dMB/%dMB",
                cores,
                convertBytesToMB(max),
                convertBytesToMB(total),
                convertBytesToMB(free)
        );
    }

    private static int convertBytesToMB(long bytes) {
        return (int) (bytes >> 20);
    }
}

