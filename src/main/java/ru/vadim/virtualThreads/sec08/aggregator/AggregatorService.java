package ru.vadim.virtualThreads.sec08.aggregator;

import ru.vadim.virtualThreads.sec08.exetranlservice.Client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AggregatorService {

    private final ExecutorService executorService;

    public AggregatorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ProductDto getProductDto(int id)  {
        var product = CompletableFuture.supplyAsync(() -> Client.getProduct(id), executorService)
                .orTimeout(3250, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> "product-not-found"); //Мы можем обрабатывать неисправности, связанные с продуктом, отдельно и оценивать проблемы, связанные с ним, отдельно без использования блока try-catch.

        var rating = CompletableFuture.supplyAsync(() -> Client.getRating(id), executorService)
                .exceptionally(ex -> -1) // этот эксепшен относиться к то что произойдет выше
                .orTimeout(3750, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> -2); // как иэтот эксепшен реагирует на исключение по таймауту - в целом это как пайплайн
        return new ProductDto(id, product.join(), rating.join());
    }
}
