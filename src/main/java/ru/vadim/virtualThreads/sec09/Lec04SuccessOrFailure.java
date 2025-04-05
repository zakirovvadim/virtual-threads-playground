package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;
import ru.vadim.virtualThreads.sec08.Lec08ThenCombine;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

public class Lec04SuccessOrFailure {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec04SuccessOrFailure.class.getName());

    public static void main(String[] args) {
        try(var taskScope = new StructuredTaskScope<>()) {
            StructuredTaskScope.Subtask<String> subTask1 = taskScope.fork(Lec04SuccessOrFailure::getS7);
            StructuredTaskScope.Subtask<String> subTask2 = taskScope.fork(Lec04SuccessOrFailure::failingTask);

            taskScope.join(); //дожидаемся результата
//            taskScope.joinUntil(Instant.now().plusMillis(1500));

            log.info("subtask1 state: {}", subTask1.state());
            log.info("subtask2 state: {}", subTask2.state());

            log.info("subtask1 result: {}", subTask1.get());
            log.info("subtask2 result: {}", subTask2.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getS7() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("S7-{}", random);
        CommonUtils.sleep("S7", Duration.ofSeconds(1));
        return "S7-$" + random;
    }

    private static String getPobeda() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("Pobeda-{}", random);
        CommonUtils.sleep("Pobeda", Duration.ofSeconds(2));
        return "Pobeda-$" + random;
    }

    private static String failingTask() {
        throw new RuntimeException("failing task");
    }
}
