package ru.vadim.virtualThreads.sec08;

import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Lec03SupplyAsync {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Lec03SupplyAsync.class);

    public static void main(String[] args) {
        log.info("main Starts");

        var cf = slowTask();
        cf.thenAccept(v -> log.info("value={}", v));

        log.info("main ends");
        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    private static CompletableFuture<String> slowTask() {
        log.info("fastTask Starts");
        var cf = CompletableFuture.supplyAsync(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            return "Hi";
        }, Executors.newVirtualThreadPerTaskExecutor());
        log.info("fastTask ends");
        return cf;
    }
}
