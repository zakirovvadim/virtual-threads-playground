package ru.vadim.sec01;

import java.util.concurrent.CountDownLatch;
/*
        To demo some blocking operations with both platform and virtual threads
     */
public class InboundOutboundTaskDemo {

    private static final int MAX_PLATFORM = 10;
    private static final int MAX_VIRTUAL_PLATFORM = 8;

    public static void main(String[] args) throws InterruptedException {
        virtualThreadDemo();
    }

    /*
        To create a simple java platform thread
     */
    private static void platformThreadDemo() {
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = new Thread(() -> Task.ioIntensive(j));
            thread.start();
        }
    }


    /*
        To create platform thread using Thread.Builder
     */
    private static void platformThreadDemo2() {
        var builder = Thread.ofPlatform().daemon().name("vadim", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /*
       To create platform daemon thread using Thread.Builder
    */
    private static void platformThreadDemo3() throws InterruptedException {
        var latch = new CountDownLatch(MAX_PLATFORM);
        var builder = Thread.ofPlatform().daemon().name("daemon", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> {
                Task.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }

    /*
        To create a virtual thread using Thread.Builder
        - virtual threads are daemon by default
        - virtual threads do not have any default name
     */
    private static void virtualThreadDemo() throws InterruptedException {
        var latch = new CountDownLatch(MAX_VIRTUAL_PLATFORM);
        var builder = Thread.ofVirtual().name("virtual-", 1);
        for (int i = 0; i < MAX_VIRTUAL_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> {
                Task.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }
}
