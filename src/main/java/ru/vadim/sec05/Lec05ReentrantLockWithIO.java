package ru.vadim.sec05;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Lec05ReentrantLockWithIO {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05ReentrantLockWithIO.class);
    public static final CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
    public static final Lock lock = new ReentrantLock();
    static {
        System.setProperty("jdk.tracePinnedThreads", "short");
    }

    public static void main(String[] args) {
        Runnable r = () -> log.info("*** Test Message ****");
//        demo(Thread.ofPlatform());
//        Thread.ofPlatform().start(r);

        demo(Thread.ofVirtual());
        Thread.ofVirtual().start(r);
        CommonUtils.sleep(Duration.ofSeconds(15));
    }

    private static void demo(Thread.Builder builder) {
        for (int i = 0; i < 50; i++) {
            builder.start(() -> {
                log.info("Task started. {}", Thread.currentThread());
                ioTaskExample();
                log.info("Task finished. {}", Thread.currentThread());
            });
        }
    }

    private static void ioTaskExample() {
        try {
            lock.lock();
            CommonUtils.sleep(Duration.ofSeconds(10));
        } catch (Exception e) {
            log.error("exception", e);
        } finally {
            lock.unlock();
        }
    }
}
