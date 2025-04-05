package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.UUID;

public class Lec01ThreadLocal {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01ThreadLocal.class);
    public static final ThreadLocal<String> SESSION_TOKEN = new ThreadLocal<>();

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
        callExternalService();
    }

    private static void callExternalService() {
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }
}
