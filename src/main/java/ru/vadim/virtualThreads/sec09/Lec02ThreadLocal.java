package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.UUID;

/*
17:48:51.321 [Thread-0] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- Token: 54f95c59-a9c6-42c9-95dd-7002f0979602
17:48:51.338 [Thread-0] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- controller: 54f95c59-a9c6-42c9-95dd-7002f0979602
17:48:51.338 [Thread-0] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- service: 54f95c59-a9c6-42c9-95dd-7002f0979602
17:48:51.321 [Thread-1] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- Token: 26385b6c-9aa5-4e3c-bb0d-377421fcc05b
17:48:51.339 [Thread-1] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- controller: 26385b6c-9aa5-4e3c-bb0d-377421fcc05b
17:48:51.340 [Thread-1] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- service: 26385b6c-9aa5-4e3c-bb0d-377421fcc05b
17:48:51.349 [Thread-2] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- preparing HTTP request with token: null
17:48:51.349 [Thread-3] INFO ru.vadim.virtualThreads.sec09.Lec02ThreadLocal -- preparing HTTP request with token: null

private static void service() {
        log.info("service: {}", SESSION_TOKEN.get());
        Thread.ofPlatform().start(() -> callExternalService());
    }

создавая новый дочерний поток Thread-3 bkb 2, для него переменная  SESSION_TOKEN не видна, поэтому налл.
Поэтому в некоторых случаях нам может потребоваться, чтобы дочерний поток имел доступ к значениям родительского потока
для этого используем InheritableThreadLocal. Если дочерний поток что-то изменяет или удаляет, он удаляет и это значение.
Это повлияет только на дочерний поток, поэтому родительский поток затронут не будет.

Но так как такую переменную можно передать, клонировать, для каждого дочернего потомка, а виртуальных потоков мб миллионы - это проблема.
И джава старается заменить их scoped value
 */
public class Lec02ThreadLocal {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec02ThreadLocal.class);
    public static final ThreadLocal<String> SESSION_TOKEN = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        Thread.ofVirtual().start(() -> processIncomingRequest());
        Thread.ofVirtual().start(() -> processIncomingRequest());

        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    private static void processIncomingRequest() {
        authenticate();
        controller();
    }

    private static void authenticate() {
        var tokent = UUID.randomUUID().toString();
        log.info("Token: {}", tokent);
        SESSION_TOKEN.set(tokent);
    }

    private static void controller() {
        log.info("controller: {}", SESSION_TOKEN.get());
        service();
    }

    private static void service() {
        log.info("service: {}", SESSION_TOKEN.get());
        Thread.ofVirtual().start(() -> callExternalService());
    }

    private static void callExternalService() {
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }
}
