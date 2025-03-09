package ru.vadim.virtualThreads.sec05;

import org.slf4j.Logger;
import ru.vadim.util.CommonUtils;

import java.time.Duration;
import java.util.concurrent.CopyOnWriteArrayList;

/*
В предыдущей лекции мы столкнулись с необычным поведением виртуальных потоков (virtual threads) в Java. Виртуальные потоки позволяют создавать миллионы легковесных потоков, которые управляются JVM, а не операционной системой. Они монтируются на так называемые "carrier threads", которые выполняют их задачи. Если задача связана с вычислениями, поток выполняется непрерывно. Однако, если задача включает операции ввода-вывода (IO), такие как сетевые запросы или sleep, виртуальный поток демонтируется, и на его место монтируется следующий.
Проблема возникает, когда виртуальный поток выполняет синхронизированный метод или блок кода. В этом случае поток не может быть демонтирован до завершения синхронизированного участка. Это явление называется "pinning" (прикрепление). Оно также может происходить при вызове методов через JNI (Java Native Interface). Это известное ограничение, которое пока не исправлено даже в Java 21, хотя команда Java обещает это исправить в будущем.
Для синхронизации в памяти (например, Collections.synchronizedList) это не проблема, так как такие задачи выполняются быстро. Однако, если синхронизированный блок кода включает операции IO, это может негативно сказаться на масштабируемости.
Есть обходное решение, которое будет рассмотрено позже. Однако, проблема усугубляется, если подобный код используется в сторонних библиотеках, которые могут содержать синхронизированные блоки с IO. В следующей лекции будет обсуждаться, как обнаружить такие проблемы в сторонних библиотеках.
 */
public class Lec03SynchronizationWithIO {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec03SynchronizationWithIO.class);
    public static final CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(); // вместо синхронизации я также могу использовать конкарент лист
    static {
        System.setProperty("jdk.tracePinnedThreads", "chart"); // свойство которое позволяет проверять, нет ли в коде или в библиотеках зависших (pinned) виртуальных потоков, второй аргумент full - вернет более развернутую ошибку, chart  - укороченную
    }

    public static void main(String[] args) {
        Runnable r = () -> log.info("*** Test Message ****");
//        demo(Thread.ofPlatform());
//        Thread.ofPlatform().start(r);

        demo(Thread.ofVirtual());
        Thread.ofVirtual().start(r);
        CommonUtils.sleep(Duration.ofSeconds(15));
    }

    private static void demo(Thread.Builder builder) {
        for (int i = 0; i < 50; i++) {
            builder.start(() -> {
                log.info("Task started. {}", Thread.currentThread());
                ioTaskExample();
                log.info("Task finished. {}", Thread.currentThread());
            });
        }
    }

    private static  void ioTaskExample() {
        list.add(1);
        CommonUtils.sleep(Duration.ofSeconds(10));
    }
}
