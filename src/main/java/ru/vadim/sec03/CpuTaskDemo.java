package ru.vadim.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.util.CommonUtils;

import java.util.concurrent.CountDownLatch;
/*
### 2. **Разница в поведении при увеличении числа задач**
- При создании большего числа задач платформенные потоки просто увеличивают количество трейдов в системе, что приводит к переключению контекста и более длинному общему времени выполнения при большом числе потоков.
- Виртуальные потоки, напротив, представляют собой задачу, которая выполняется по мере освобождения базового "carrier thread". Если все задачи выполняются последовательно (как в случае CPU-интенсивных задач), то разница во времени будет едва заметной — задачи продолжают выполняться одна за другой.

 */
public class CpuTaskDemo {

    private static final Logger log = LoggerFactory.getLogger(CpuTaskDemo.class);

    public static final int TASKS_COUNT = 12;

    public static void main(String[] args) {
        log.info("Tasck count: {}", TASKS_COUNT);
        for (int i = 0; i < 3; i++) {
            var totalTime = CommonUtils.timer(() -> demo(Thread.ofVirtual()));
            log.info("Total time taken with virtual threads: {} ms.", totalTime);
            totalTime = CommonUtils.timer(() -> demo(Thread.ofPlatform()));
            log.info("Total time taken with platform: {} ms.", totalTime);
        }
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
