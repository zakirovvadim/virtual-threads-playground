package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;


public class Lec07ScopedValueWithStructuredTaskScope {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec07ScopedValueWithStructuredTaskScope.class.getName());
    public static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();


    public static void main(String[] args) {
        ScopedValue.runWhere(SESSION_TOKEN, "token-123", Lec07ScopedValueWithStructuredTaskScope::task); // можем передавать в наши StructuredTaskScope контекстные данные в виде ScopedValue
    }

    private static void task() {
        try(var taskScope = new StructuredTaskScope<>()) {
            log.info("token: {}", SESSION_TOKEN.get());

            StructuredTaskScope.Subtask<String> subTask1 = taskScope.fork(Lec07ScopedValueWithStructuredTaskScope::getS7); // fork создает дочерние потоки
            StructuredTaskScope.Subtask<String> subTask2 = taskScope.fork(Lec07ScopedValueWithStructuredTaskScope::getPobeda);

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
        log.info("token: {}", SESSION_TOKEN.get());
        CommonUtils.sleep("S7", Duration.ofSeconds(1));
        return "S7-$" + random;
    }

    private static String getPobeda() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("Pobeda-{}", random);
        log.info("token: {}", SESSION_TOKEN.get());
        CommonUtils.sleep("Pobeda", Duration.ofSeconds(2));
        return "Pobeda-$" + random;
    }

    private static String failingTask() {
        throw new RuntimeException("failing task");
    }
}
