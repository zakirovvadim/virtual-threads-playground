package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class Lec01SimpleCompletableFuture {

    public static final Logger log = LoggerFactory.getLogger(Lec01SimpleCompletableFuture.class);

    public static void main(String[] args) {
        log.info("main Starts");

        var cf = slowTask();;
//        log.info("value={}", cf.join()); // блокирующий, как и get
        cf.thenAccept(v -> log.info("value={}", v)); // блокирующий, как и get

        log.info("main ends");
        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    private static CompletableFuture<String> fastTask() {
        log.info("fastTask Starts");
        var cf = new CompletableFuture<String>();
        cf.complete("Hi");
        log.info("fastTask ends");
        return cf;
    }

    private static CompletableFuture<String> slowTask() {
        log.info("fastTask Starts");
        var cf = new CompletableFuture<String>();
        Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            cf.complete("Hi");
        });
        log.info("fastTask ends");
        return cf;
    }
}
