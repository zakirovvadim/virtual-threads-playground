package ru.vadim.executorService;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lec01AutoClosable {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01AutoClosable.class);

    public static void main(String[] args) {
        // Старый вариант,в java 17, без авто закрытия

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.submit(Lec01AutoClosable::task);
//        log.info("Task submitted");
        //executorService.shutdownNow();// закрывает не дожидаясь завершениявыполнения
//        executorService.shutdown();// закрывает дожидаясь завершениявыполнения

        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            executorService.submit(Lec01AutoClosable::task);
            executorService.submit(Lec01AutoClosable::task);
            executorService.submit(Lec01AutoClosable::task);
            log.info("Task submitted");
        }
    }

    private static void task() {
        CommonUtils.sleep(Duration.ofSeconds(1));
        log.info("Task finished");
    }
}
