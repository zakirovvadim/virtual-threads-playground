package ru.vadim.executorService.sec07;

import org.slf4j.Logger;
import ru.vadim.executorService.sec07.exetranlservice.Client;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/*
Для виртуальных потоков нет Executor типа schedule, поэтому единственный выходи, использовать обычные шедулер для старта,
а его исполнение отдать виртуальным потокам.
 */
public class Lec07ScheduledExecutorWithVirtualThreads {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec07ScheduledExecutorWithVirtualThreads.class);

    public static void main(String[] args) {
        scheduled();
    }

    public static void scheduled() {
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        try (scheduler; executor) {
            scheduler.scheduleAtFixedRate(() -> {
                executor.submit(() -> printProductInfo(1));
            }, 0 ,3, TimeUnit.SECONDS);
            CommonUtils.sleep(Duration.ofSeconds(15));
        }
    }

    private static void printProductInfo(int id) {
        log.info("{} => {}", id, Client.getProduct(id));
    }
}
