/*
 * (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
 * These materials are unpublished, proprietary, confidential source code of
 * Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
 * Nexmo Inc retains all titles to an intellectual property rights in these materials.
 */
package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultipleSyncBlocks {

    private Random random  = new Random();
    private List<Integer> list1 = new ArrayList<Integer>();
    private List<Integer> list2 = new ArrayList<Integer>();

    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void stage1() {
        synchronized (lock1) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            list1.add(random.nextInt(100));
        }
    }

    public synchronized void stage2() {
        synchronized (lock2) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            list2.add(random.nextInt(100));
        }
    }

    public void process() {
        for(int i=0; i<1000; i++) {
            stage1();
            stage2();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        MultipleSyncBlocks app = new MultipleSyncBlocks();
        long start = System.currentTimeMillis();

        Runnable task = () -> app.process();

        Thread t1 = new Thread(task);
        t1.start();

        Thread t2 = new Thread(task);
        t2.start();

        t1.join();
        t2.join();

        long end = System.currentTimeMillis();

        System.out.println("time take = "+(end - start) );
        System.out.println(app.list1.size() + " "+ app.list2.size() );

    }
}
