package ru.vadim.executorService.sec07.concurrencyLimit;

import org.slf4j.Logger;

import java.util.Queue;
import java.util.concurrent.*;


public class ConcurrencyLimiter implements AutoCloseable {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(ConcurrencyLimiter.class);

    private final ExecutorService executorService;
    private final Semaphore semaphore;
    private final Queue<Callable<?>> queue;

    public ConcurrencyLimiter(ExecutorService executorService, int limit) {
        this.executorService = executorService;
        this.semaphore = new Semaphore(limit);
        this.queue = new ConcurrentLinkedQueue<>();
    }


    public <T> Future<T> submit(Callable<T> callable) {
        this.queue.add(callable); // для соблюдения порядка выполнения задач, (НЕ ПОТОКОВ) мы можем добавить очередь выполнения
        return executorService.submit(() -> executeTask());
    }


    private <T> T executeTask() {
        try {
            semaphore.acquire();
            return (T) this.queue.poll().call();
        } catch (Exception e) {
            log.error("Error", e);
        } finally {
            semaphore.release();
        }
        return null;
    }


    @Override
    public void close() throws Exception {
        this.executorService.close();
    }
}
