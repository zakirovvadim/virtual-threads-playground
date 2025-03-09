package ru.vadim.sec05;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Lec01RaceCondition {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01RaceCondition.class);
    public static final List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        demo(Thread.ofVirtual());
        CommonUtils.sleep(Duration.ofSeconds(2));

        log.info("list size: {}", list.size());
    }

    private static void demo(Thread.Builder builder) {
        for (int i = 0; i < 50; i++) {
            builder.start(() -> {
                log.info("Task started. {}", Thread.currentThread());
                for (int j = 0; j < 200; j++) {
                    inMemoryTask();
                }
                log.info("Task finished. {}", Thread.currentThread());
            });
        }
    }

    private static void inMemoryTask() {
        list.add(1);
    }
}
