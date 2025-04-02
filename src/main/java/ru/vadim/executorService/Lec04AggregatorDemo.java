package ru.vadim.executorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.executorService.aggregator.AggreagatorService;
import ru.vadim.executorService.aggregator.ProductDto;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Lec04AggregatorDemo {

    public static final Logger log = LoggerFactory.getLogger(Lec04AggregatorDemo.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // beans / singletons
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        var aggregator = new AggreagatorService(executor);

        List<Future<ProductDto>> futures = IntStream.range(1, 50)
                .mapToObj(id -> executor.submit(() -> aggregator.getProductDto(id)))
                .toList();
        List<ProductDto> list = futures.stream()
                .map(Lec04AggregatorDemo::toProductDto)
                .toList();
        log.info("list: {}", list);
    }

    private static ProductDto toProductDto(Future<ProductDto> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
