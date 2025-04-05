package ru.vadim.virtualThreads.sec08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vadim.virtualThreads.sec08.aggregator.AggregatorService;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class Lec05AggregatorDemo {
    public static final Logger log = LoggerFactory.getLogger(Lec05AggregatorDemo.class.getName());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        var aggregator = new AggregatorService(executor);

        log.info("product={}", aggregator.getProductDto(52));
    }
}
