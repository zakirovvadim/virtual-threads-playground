package ru.vadim.sec02;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;

public class Task {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Task.class);

    public static void execute(int i) {
        log.info("starting task {}", i);
        try {
            method1(i);
        } catch (Exception e) {
            log.error("error for {}", i, e);
        }
        log.info("ending task {}", i);
    }

    public static void method1(int i) {
        CommonUtils.sleep(Duration.ofMillis(300));
        try {
            method2(i);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void method2(int i) {
        CommonUtils.sleep(Duration.ofMillis(100));
        method3(i);
    }

    public static void method3(int i) {
        CommonUtils.sleep(Duration.ofMillis(500));
        if (i == 4) {
            throw new IllegalArgumentException("I cannot be 4");
        }
    }
}
