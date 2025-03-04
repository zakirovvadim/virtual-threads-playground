package ru.vadim.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.util.CommonUtils;

import java.util.concurrent.CountDownLatch;

public class CpuTaskDemo {

    private static final Logger log = LoggerFactory.getLogger(CpuTaskDemo.class);

    public static final int TASKS_COUNT = Runtime.getRuntime().availableProcessors() * 3;

    public static void main(String[] args) {
        demo(Thread.ofPlatform());
    }

    private static void demo(Thread.Builder builder) {
        var latch = new CountDownLatch(TASKS_COUNT);
        for (int i = 1; i <= TASKS_COUNT; i++) {
            builder.start(() -> {
                Task.cpuIntensive(45);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
