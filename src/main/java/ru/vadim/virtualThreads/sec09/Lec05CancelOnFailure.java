package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

// StructuredTaskScope нужен для выполнения одной задачи по частям, где если не выполнится одна задача, можно остановить другие.
// ShutdownOnFailure при добавлении отслеживает статус, и отменяет другие при фейлах
public class Lec05CancelOnFailure {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05CancelOnFailure.class.getName());

    public static void main(String[] args) {
        try(var taskScope = new StructuredTaskScope.ShutdownOnFailure()) {
            StructuredTaskScope.Subtask<String> subTask1 = taskScope.fork(Lec05CancelOnFailure::getS7);
            StructuredTaskScope.Subtask<String> subTask2 = taskScope.fork(Lec05CancelOnFailure::failingTask);

            taskScope.join(); //дожидаемся результата
            taskScope.throwIfFailed(ex -> new RuntimeException("something went wrong")); // можем в случае падения одной из таск,выдать исключение
//            taskScope.joinUntil(Instant.now().plusMillis(1500));

            log.info("subtask1 state: {}", subTask1.state());
            log.info("subtask2 state: {}", subTask2.state());

//            log.info("subtask1 result: {}", subTask1.get());
//            log.info("subtask2 result: {}", subTask2.get());
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
