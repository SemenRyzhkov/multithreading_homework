package utils;

/**
 * Утилита для безопасного ожидания завершения потоков без выброса InterruptedException.
 * <p>
 * Методы не бросают исключения наружу и восстанавливают флаг прерывания потока,
 * если тот был установлен во время ожидания {@code join()}.
 */
public final class ThreadJoinUtils {

    private ThreadJoinUtils() {
    }

    /**
     * Безопасно ожидает завершения указанного потока.
     * <p>
     * Если поток равен {@code null}, ничего не делает.
     * Если текущий поток был прерван, восстанавливает флаг прерывания.
     *
     * @param thread поток, который нужно дождаться
     */
    public static void safeJoin(Thread thread) {
        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt(); // восстанавливаем флаг
        } catch (Exception ignored) {}
    }

    /**
     * Безопасно ожидает завершения всех указанных потоков.
     * <p>
     * Если массив потоков равен {@code null} или пуст, ничего не делает.
     * Для каждого потока вызывается {@link #safeJoin(Thread)}.
     *
     * @param threads список потоков для ожидания
     */
    public static void safeJoin(Thread... threads) {
        if (threads == null) {
            return;
        }
        for (Thread t : threads) {
            safeJoin(t);
        }
    }
}
