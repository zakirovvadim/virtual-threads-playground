package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

// ShutdownOnSuccess - если один завершится быстрее, остальные отменяем. Если одна зафейлитс, будем ждать первого успешного. Если все, то исключение
public class Lec05FirstSuccess {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05FirstSuccess.class.getName());

    public static void main(String[] args) {
        try(var taskScope = new StructuredTaskScope.ShutdownOnSuccess<>()) {
            StructuredTaskScope.Subtask<String> subTask1 = taskScope.fork(Lec05FirstSuccess::failingTask);
            StructuredTaskScope.Subtask<String> subTask2 = taskScope.fork(Lec05FirstSuccess::failingTask);

            taskScope.join(); //дожидаемся результата

            log.info("subtask1 state: {}", subTask1.state());
            log.info("subtask2 state: {}", subTask2.state());

            log.info("subtask1 result: {}", taskScope.result()); // даст нам первый ответ
//            log.info("subtask2 result: {}", subTask2.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getS7() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("S7-{}", random);
        CommonUtils.sleep("S7", Duration.ofSeconds(3));
        return "S7-$" + random;
    }

    private static String getPobeda() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("Pobeda-{}", random);
        CommonUtils.sleep("Pobeda", Duration.ofSeconds(2));
        return "Pobeda-$" + random;
    }

    private static String failingTask() {
        throw new RuntimeException("oops failing task");
    }
}
