/*
* (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
* These materials are unpublished, proprietary, confidential source code of
* Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
* Nexmo Inc retains all titles to an intellectual property rights in these materials.
*/
package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {
    int id;
    
    public Processor(int id) {
        this.id = id;
    }

    public void run() {
        System.out.println("starting "+id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ending "+id);
    }
}

public class ThreadPools {

    public static void main(String[] args) throws InterruptedException {
        
        ExecutorService executor = Executors.newFixedThreadPool(2); //creates 2 workers
        
        for(int i=0; i<5; i++) {
            executor.submit(new Processor(i));
        }
        
        executor.shutdown(); // won't wait for all threads to terminate
        
        System.out.println("All tasks subitted");
        
        executor.awaitTermination(1, TimeUnit.DAYS);
        
        System.out.println("all tasks completed");
    }
}
