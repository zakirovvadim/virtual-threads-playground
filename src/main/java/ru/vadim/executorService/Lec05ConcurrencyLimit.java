package ru.vadim.executorService;

import org.slf4j.Logger;
import ru.vadim.executorService.exetranlservice.Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/*
1.
    - Виртуальные потоки обеспечивают невероятную гибкость и масштабируемость, но они не предназначены для переиспользования в пулах потоков.
    - Официальная документация Oracle подчёркивает: виртуальные потоки одноразовые. Они создаются на задачу и не должны быть переработаны (рециклированы) в системе.
    - Если требуется ограничение по конкуренции (например, 3 одновременных вызова), не используйте `FixedThreadPool` с фабрикой виртуальных потоков, а воспользуйтесь инструментами для ограничения количества задач:
        - Например, библиотека `Semaphore` является подходящим решением для регулирования количества одновременных вызовов.
 */
public class Lec05ConcurrencyLimit {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05ConcurrencyLimit.class);

    public static void main(String[] args) {
        ThreadFactory factory = Thread.ofVirtual().name("vadim", 1).factory();
        execute(Executors.newFixedThreadPool(1), 20);
    }

    private static void execute(ExecutorService executorService, int taskCount) {
        try(executorService) {
            for (int i = 1; i <= taskCount; i++) {
                int j = i;
                executorService.submit(() -> printProductInfo(j));
            }
            log.info("submitted");
        }
    }

    private static void printProductInfo(int id) {
        log.info("{} => {}", id, Client.getProduct(id));
    }
}
