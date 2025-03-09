package ru.vadim.virtualThreads.sec05;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lec04ReentranLock {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec04ReentranLock.class);
    public static final List<Integer> list = new ArrayList<>(); // вместо синхронизации я также могу использовать конкарент лист
    public static final Lock lock = new ReentrantLock();
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
        try {
            lock.lock();
            list.add(1);
        } catch (Exception e) {
            log.error("exception", e);
        } finally {
            lock.unlock();
        }
    }
}
