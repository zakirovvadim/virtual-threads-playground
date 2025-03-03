package ru.vadim.sec01;

import org.slf4j.Logger;

import java.time.Duration;

public class Task {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Task.class);

    public static void ioIntensive(int i) {
        try {
            log.info("starting I|O task {}", i);
            Thread.sleep(Duration.ofSeconds(60));
            log.info("ending I|O task {}", i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
