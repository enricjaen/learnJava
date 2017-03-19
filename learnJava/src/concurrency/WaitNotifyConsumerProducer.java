/*
 * (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
 * These materials are unpublished, proprietary, confidential source code of
 * Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
 * Nexmo Inc retains all titles to an intellectual property rights in these materials.
 */
package concurrency;

import java.util.LinkedList;
import java.util.Random;

public class WaitNotifyConsumerProducer {

    static LinkedList<Integer> list = new LinkedList<>();
    private static Object lock = new Object();
    static final int LIMIT = 10;

    private static void producer () throws InterruptedException { // adds elements to the queue
        Random r = new Random();
        int value = 0;
        while (true) {
            synchronized (lock) {

                while(list.size() == LIMIT) {
                    lock.wait();
                }
                list.add(value++);
                lock.notify();
            }
        }
    }

    private static void consumer() throws InterruptedException {
        Random r = new Random();
        while (true) {

            synchronized (lock) {

                while(list.size() == 0) {
                    lock.wait();
                }
                System.out.println("list size = "+list.size());
                int value = list.removeFirst();
                System.out.println("value is "+value);
                lock.notify();
            }

           // Thread.sleep(r.nextInt(1000));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable consumerTask = () -> {
            try {
                consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable producerTask = () -> {
            try {
                producer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread t1 = new Thread(consumerTask);
        Thread t2 = new Thread(producerTask);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }
}
