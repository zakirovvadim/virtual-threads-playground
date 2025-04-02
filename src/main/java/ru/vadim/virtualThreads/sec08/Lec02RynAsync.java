package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Lec02RynAsync {

    public static final Logger log = LoggerFactory.getLogger(Lec02RynAsync.class);

    public static void main(String[] args) {
        log.info("main Starts");
        runAsync()
                .thenRun(() -> log.info("it is done")) // thenRun позволяет нам не блокируя получить подтверждение, что фьюче выполнен
                .exceptionally(throwable -> { // в случае ошибки используем оператор отлова исключения
                    log.error("error - {}", throwable.getMessage());
                    return null;
                });

        log.info("main ends");
        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    private static CompletableFuture<Void> runAsync() {
        log.info("method Starts");
        CompletableFuture<Void> rc = CompletableFuture.runAsync(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            log.info("task completed");
        }, Executors.newVirtualThreadPerTaskExecutor());// заменяем потоки из форк джоин коммон пул на виртуальные
        log.info("method ends");
        return rc;
    }
}
