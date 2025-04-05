package ru.vadim.virtualThreads.sec09;

import org.slf4j.Logger;

import java.util.UUID;

public class Lec03ScopedValues {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec03ScopedValues.class.getName());
    public static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    public static void main(String[] args) {

        log.info("isBound={}", SESSION_TOKEN.isBound()); // сначала нужно проверить, есть ли элемент
        log.info("value={}", SESSION_TOKEN.orElse("default value"));

        processIncomingRequest();

    }

    private static void processIncomingRequest() {
        String token = authenticate();
        ScopedValue.runWhere(SESSION_TOKEN, token, () -> controller()); // так мы передаем значение scopedValue, если в мтеоде controller() будет вызван .get(), давай значение token
        //        SESSION_TOKEN.get() так будет ошибка, потому что скоуп видимости установлен для controller
//        controller();
    }

    private static String authenticate() {
        var tokent = UUID.randomUUID().toString();
        log.info("Token: {}", tokent);
        return tokent;
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
