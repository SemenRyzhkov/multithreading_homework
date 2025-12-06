package utils;

import java.security.SecureRandom;

/**
 * Обертка над {@link Thread#sleep(long)}
 */
public final class ThreadSleepUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private ThreadSleepUtil() {
    }

    /**
     * Безопасно спит указанное время и при прерывании возвращает флаг, не проглатывая событие.
     */
    public static void safeSleepWithoutThrow(long ms) {
        if (ms <= 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Безопасно спит указанное время
     * При прерывании возвращает флаг, не проглатывая событие.
     * Добавляет случайную задержку
     */
    public static void safeSleepWithJitter(long ms) {
        if (ms <= 0) {
            return;
        }
        try {
            var jitter = RANDOM.nextLong(ms/2);
            if (RANDOM.nextBoolean()) {
                jitter *= -1;
            }
            Thread.sleep(ms + jitter);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Безопасно спит случайное время в диапазоне
     * При прерывании возвращает флаг, не проглатывая событие.
     */
    public static void safeSleepRandomMillis(
            long lowerBound,
            long upperBound
    ) {
        try {
            var sleepMillis = RANDOM.nextLong(lowerBound, upperBound);
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
