package ru.vadim.virtualThreads.sec08.aggregator;

import ru.vadim.virtualThreads.sec08.exetranlservice.Client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class AggregatorService {

    private final ExecutorService executorService;

    public AggregatorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ProductDto getProductDto(int id) throws ExecutionException, InterruptedException {
        var product = CompletableFuture.supplyAsync(() -> Client.getProduct(id), executorService)
                .exceptionally(ex -> "product-not-found"); //Мы можем обрабатывать неисправности, связанные с продуктом, отдельно и оценивать проблемы, связанные с ним, отдельно без использования блока try-catch.
        var rating = CompletableFuture.supplyAsync(() -> Client.getRating(id), executorService)
                .exceptionally(ex -> -1);
        return new ProductDto(id, product.get(), rating.get());
    }
}
