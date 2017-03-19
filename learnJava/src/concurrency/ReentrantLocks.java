/*
 * (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
 * These materials are unpublished, proprietary, confidential source code of
 * Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
 * Nexmo Inc retains all titles to an intellectual property rights in these materials.
 */
package concurrency;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;


public class ReentrantLocks {



    public static void main(String[] args) throws InterruptedException {

        Runner runner = new Runner();
        ReentrantLocks app = new ReentrantLocks();
        Runnable first = () -> {
            try {
                runner.firstThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable second = () -> {
            try {
                runner.secondThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread t1 = new Thread(first);
        Thread t2 = new Thread(second);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        runner.finished();
    }

}


class Runner {

    private int count = 0;
    private Lock lock = new ReentrantLock(); // is can be locked several times 
    private Condition cond = lock.newCondition();
    
    private void increment () {
        IntStream.range(0, 10000).forEach(i-> count++); 
    }

    public void firstThread() throws InterruptedException {
        lock.lock();
        System.out.println("waiting");
        cond.await();  // hands over the lock (releases lock and waits)
        System.out.println("woken up!");
              
        try {
            increment();
        } finally {
            lock.unlock();
        }
    }

    public void secondThread() throws InterruptedException {
        Thread.sleep(1000);
        lock.lock();
        System.out.println("press return");
        new Scanner(System.in).nextLine();
        cond.signal();
        try {
            increment();
        } finally {
            lock.unlock();
        }

    }

    public void finished() {
        System.out.println("count "+count);
    }
}
