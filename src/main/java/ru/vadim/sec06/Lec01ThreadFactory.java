package ru.vadim.sec06;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;

public class Lec01ThreadFactory {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01ThreadFactory.class);

    public static void main(String[] args) {
        demo(Thread.ofVirtual().name("vins", 1).factory());

        CommonUtils.sleep(Duration.ofSeconds(3));
    }

    /*
    Создание новых потоков.
    Каждый поток создаст дочерний поток.
    Это простое демо для ThreadFactory, в реальной жизни используется ExecutorService.
     */
    private static void demo(ThreadFactory factory) {
        for (int i = 0; i < 30; i++) {
            var t = factory.newThread(() -> {
                log.info("Task started. {}", Thread.currentThread());
                var ct = factory.newThread(() -> {
                    log.info("Child task started. {}", Thread.currentThread());
                    CommonUtils.sleep(Duration.ofSeconds(2));
                    log.info("Child task finished. {}", Thread.currentThread());
                });
                ct.start();
                log.info("Task finished. {}", Thread.currentThread());
            });
            t.start();
        }
    }
}
