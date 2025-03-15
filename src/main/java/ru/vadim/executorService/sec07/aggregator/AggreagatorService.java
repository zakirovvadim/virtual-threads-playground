package ru.vadim.executorService.sec07.aggregator;

import ru.vadim.executorService.sec07.exetranlservice.Client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class AggreagatorService {

    private final ExecutorService executorService;

    public AggreagatorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ProductDto getProductDto(int id) throws ExecutionException, InterruptedException {
        var product = executorService.submit(() -> Client.getProduct(id));
        var rating = executorService.submit(() -> Client.getRating(id));
        return new ProductDto(id, product.get(), rating.get());
    }
}
