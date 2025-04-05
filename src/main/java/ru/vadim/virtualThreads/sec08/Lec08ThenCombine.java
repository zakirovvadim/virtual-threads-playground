package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Lec08ThenCombine {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec08ThenCombine.class);

    record Airfare(String airline, int amount) {
    }

    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var cf1 = getDeltaAirdare(executor);
            var cf2 = getAeroflot(executor);
            Airfare bestDeal = cf1.thenCombine(cf2, (a, b) -> a.amount() <= b.amount() ? a : b) // комбинируем фьюче с условием меньшей цены amount
                    .thenApply(airfare -> new Airfare(airfare.airline(), (int) (airfare.amount() * 0.9))) // применяем скидку на прошедшие по условию CompletableFuture
                    .join();

            log.info("best deal={}", bestDeal);
        }
    }

    private static CompletableFuture<Airfare> getDeltaAirdare(ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            var random = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(random));
            log.info("S7-{}", random);
            return new Airfare("S7", random);
        }, executor);
    }

    private static CompletableFuture<Airfare> getAeroflot(ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            var random = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(random));
            log.info("Aeroflot-{}", random);
            return new Airfare("Aeroflot", random);
        }, executor);
    }
}
