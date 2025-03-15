package ru.vadim.executorService.sec07;

import org.slf4j.Logger;
import ru.vadim.executorService.sec07.concurrencyLimit.ConcurrencyLimiter;
import ru.vadim.executorService.sec07.exetranlservice.Client;

import java.util.concurrent.Executors;

/*
Согласно документации оракл, для виртуальных потоков лучше использовать семафоры, так как они не блокируют поток, а паркуют его,
что согласуется с архитектурой виртуальных потоков.
 */
public class Lec06ConcurrencyLimitWithSemaphore {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec06ConcurrencyLimitWithSemaphore.class);

    public static void main(String[] args) throws Exception {
        ConcurrencyLimiter limiter = new ConcurrencyLimiter(Executors.newVirtualThreadPerTaskExecutor(), 3);
        execute(limiter, 20);
    }

    private static void execute(ConcurrencyLimiter concurrencyLimiter, int taskCount) throws Exception {
        try(concurrencyLimiter) {
            for (int i = 1; i <= taskCount; i++) {
                int j = i;
                concurrencyLimiter.submit(() -> printProductInfo(j));
            }
            log.info("submitted");
        }
    }

    private static String printProductInfo(int id) {
        String product = Client.getProduct(id);
        log.info("{} => {}", id, product);
        return product;
    }
}
