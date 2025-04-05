package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import ru.vadim.virtualThreads.sec08.aggregator.AggregatorService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Lec06AllOf {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec06AllOf.class);

    public static void main(String[] args) {
        // beans / singletons
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        var aggregator = new AggregatorService(executor);

        var futures = IntStream.range(1, 200)
                .mapToObj(id -> CompletableFuture.supplyAsync(() -> aggregator.getProductDto(id), executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join(); // вызываем одновременно и ждем завершения

        var list = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        log.info("list: {}", list);
    }
}
