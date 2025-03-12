package ru.vadim.executorService.sec07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
Однопоточный исполнитель может быть полезен в тех случаях, когда требуется последовательно выполнять задачи одну за другой.
 */
public class Lec02ExecutorServiceType {

    public static final Logger log = LoggerFactory.getLogger(Lec02ExecutorServiceType.class);

    public static void main(String[] args) {
//        execute(Executors.newSingleThreadExecutor(), 3); // Однопоточный исполнитель может быть полезен в тех случаях, когда требуется последовательно выполнять задачи одну за другой.
//        execute(Executors.newFixedThreadPool(4),20);
//        execute(Executors.newCachedThreadPool(),200); // по умолчанию нет никаких потоков, слхдаются по требованию
//        execute(Executors.newVirtualThreadPerTaskExecutor(),200); //
        scheduled();
    }

    public static void scheduled() {
        try (var executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(() -> {
                log.info("executing task");
            }, 0 ,1, TimeUnit.SECONDS);
            CommonUtils.sleep(Duration.ofSeconds(5));
        }
    }

    private static void execute(ExecutorService executorService, int taskCount) {
        try(executorService) {
            for (int i = 0; i < taskCount; i++) {
                int j = i;
                executorService.submit(() -> task(j));
            }
            log.info("submitted");
        }
    }


    private static void task(int i) {
        log.info("Task started: {}, Thread info: {}", i, Thread.currentThread());
        CommonUtils.sleep(Duration.ofSeconds(5));
        log.info("Task finished: {}, Thread info: {}", i, Thread.currentThread());
    }
}
