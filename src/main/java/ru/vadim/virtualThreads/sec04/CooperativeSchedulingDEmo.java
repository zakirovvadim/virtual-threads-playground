package ru.vadim.virtualThreads.sec04;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;

/*
Demo to understand cooperative scheduling

Примерр кооперативного шедулинга потоков. Когда каждый поток заботится о другом

 private static void demo(int threadNumber) {
        log.info("thread-{} started", threadNumber);
        for (int i = 0; i < 10; i++) {
            log.info("thread-{} is printing {}. Thread: {}", threadNumber, i, Thread.currentThread()); --- 1
            Thread.yield(); ---- 2
        }
        log.info("thread-{} ended", threadNumber);
    }


10:23:49.906 [virtual-26] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-1 started ----- 1 начали выполнение первого потока
10:23:49.928 [virtual-26] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-1 is printing 0. Thread: VirtualThread[#26]/runnable@ForkJoinPool-1-worker-1 ------- выполняем первый поток
10:23:49.929 [virtual-27] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-2 started ------- делаем уступку yield для второго потока
10:23:49.929 [virtual-27] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-2 is printing 0. Thread: VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1 -------- выполняем второй поток и попадаем в уступку снова
10:23:49.930 [virtual-26] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-1 is printing 1. Thread: VirtualThread[#26]/runnable@ForkJoinPool-1-worker-1 -------- переключились заново на первый поток
10:23:49.930 [virtual-27] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-2 is printing 1. Thread: VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1
10:23:49.930 [virtual-26] INFO ru.vadim.virtualThreads.sec04.CooperativeSchedulingDEmo -- thread-1 is printing 2. Thread: VirtualThread[#26]/runnable@ForkJoinPool-1-worker-1
 */
public class CooperativeSchedulingDEmo {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(CooperativeSchedulingDEmo.class);
    static {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
    }

    public static void main(String[] args) {
        var builder = Thread.ofVirtual();
        var t1 = builder.unstarted(() -> demo(1));
        var t2 = builder.unstarted(() -> demo(2));
        var t3 = builder.unstarted(() -> demo(3));

        t1.start();
        t2.start();
        t3.start();

        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    private static void demo(int threadNumber) {
        log.info("thread-{} started", threadNumber);
        for (int i = 0; i < 10; i++) {
            log.info("thread-{} is printing {}. Thread: {}", threadNumber, i, Thread.currentThread());
            if ((threadNumber == 1 && i % 2 == 0) || (threadNumber == 2)) {
                Thread.yield();
            }
        }
        log.info("thread-{} ended", threadNumber);
    }
}
