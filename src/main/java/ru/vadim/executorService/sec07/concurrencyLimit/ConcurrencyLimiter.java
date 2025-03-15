package ru.vadim.executorService.sec07.concurrencyLimit;

import org.slf4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

/**
 * The ConcurrencyLimiter class is designed to manage and limit the level of concurrency in a given
 * ExecutorService. It ensures that the maximum number of tasks executing concurrently does not exceed
 * a specified limit by using a Semaphore for control.
 *
 * This class implements the AutoCloseable interface, making it convenient to use in try-with-resources blocks.
 */
public class ConcurrencyLimiter implements AutoCloseable {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(ConcurrencyLimiter.class);

    private final ExecutorService executorService;
    private final Semaphore semaphore;

    public ConcurrencyLimiter(ExecutorService executorService, int limit) {
        this.executorService = executorService;
        this.semaphore = new Semaphore(limit);
    }

    /**
     * Submits a Callable task for execution in the associated ExecutorService.
     * The task is wrapped to ensure that concurrency control provided by a Semaphore is applied.
     *
     * @param <T> the result type returned by the Callable's call method
     * @param callable the Callable task to be executed
     * @return a Future representing the pending result of the Callable task
     */
    public <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(() -> wrapCallable(callable));
    }

    /**
     * Wraps a given {@link Callable} to ensure controlled access using a semaphore. This method
     * acquires a permit from the semaphore before executing the callable and releases it afterward,
     * ensuring that the maximum concurrency level is respected. If an exception occurs during the
     * callable execution, it is logged, and the method ensures proper semaphore release.
     *
     * @param <T> the result type of the callable
     * @param callable the {@link Callable} to be executed within the concurrency-limited context
     * @return the result of the callable's execution, or null if an exception occurs
     */
    private <T> T wrapCallable(Callable<T> callable) {
        try {
            semaphore.acquire();
            return callable.call();
        } catch (Exception e) {
            log.error("Error", e);
        } finally {
            semaphore.release();
        }
        return null;
    }

    /**
     * Closes the underlying ExecutorService associated with the ConcurrencyLimiter.
     * This method should be called to release resources used by the ExecutorService.
     * Implements the AutoCloseable interface to allow usage in try-with-resources blocks.
     *
     * @throws Exception if an error occurs during the closing of the ExecutorService.
     */
    @Override
    public void close() throws Exception {
        this.executorService.close();
    }
}
