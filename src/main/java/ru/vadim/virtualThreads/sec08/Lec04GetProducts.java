package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.executorService.exetranlservice.Client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Lec04GetProducts {

    public static final Logger log = LoggerFactory.getLogger(Lec04GetProducts.class);

    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> product1 = CompletableFuture.supplyAsync(() -> Client.getProduct(1), executor);
            Future<String> product2 = CompletableFuture.supplyAsync(() -> Client.getProduct(2), executor);
            Future<String> product3 = CompletableFuture.supplyAsync(() -> Client.getProduct(3), executor);

            log.info("product-1: {}", product1.get());
            log.info("product-2: {}", product2.get());
            log.info("product-3: {}", product3.get());
        }
    }
}
