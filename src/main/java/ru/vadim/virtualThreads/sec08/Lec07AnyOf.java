package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/*
пример anyOf() - выдаст тот ответ, который первый завершится
 */
public class Lec07AnyOf {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec07AnyOf.class);

    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var cf1 = getDeltaAirdare(executor);
            var cf2 = getAeroflot(executor);
            log.info("airfare={}", CompletableFuture.anyOf(cf1, cf2).join());
        }
    }

    private static CompletableFuture<String> getDeltaAirdare(ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            var random = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(random));
            return "Delta-$" + random;
        }, executor);
    }

    private static CompletableFuture<String> getAeroflot(ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            var random = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(random));
            return "Aeroflot-$" + random;
        }, executor);
    }
}
