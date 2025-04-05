package ru.vadim.util;

import java.time.Duration;

public class CommonUtils {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CommonUtils.class);

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(String taskName, Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            log.info("{} is cancelled", taskName);
        }
    }

    public static long timer(Runnable runnable) {
        var start = System.currentTimeMillis();
        runnable.run();
        var end = System.currentTimeMillis();
        return (end - start);
    }
}
