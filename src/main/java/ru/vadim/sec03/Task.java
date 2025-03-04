package ru.vadim.sec03;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

public class Task {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Task.class);

    public static void cpuIntensive(int i) {
       log.info("starting CPU task {}. Thread Info: {}", i, Thread.currentThread());
       var timeTaken = CommonUtils.timer(() -> findFib(i));
       log.info("ending CPU task. Time taken: {} ms.", timeTaken);
    }

    public static long findFib(long input) {
        if (input < 2) {
            return input;
        }
        return findFib(input - 1) + findFib(input - 2);
    }
}
