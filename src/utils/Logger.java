package utils;

/**
 * Компактный logger, который в каждом сообщении печатает имя потока и метку времени
 */
public final class Logger {

    private Logger() {}

    public static void log(
            String fmt,
            Object... args
    ) {
        String message = args == null || args.length == 0
                ? fmt
                : String.format(fmt, args);

        Thread current = Thread.currentThread();
        String logFormat = "[%tT.%1$tL][thread:%s] %s%n";

        System.out.printf(
                logFormat,
                System.currentTimeMillis(),
                current.getName(),
                message
        );
    }
}
