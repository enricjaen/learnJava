/*
* (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
* These materials are unpublished, proprietary, confidential source code of
* Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
* Nexmo Inc retains all titles to an intellectual property rights in these materials.
*/
package concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

class MyProcessor implements Runnable {
    private CountDownLatch latch;  // thread safe class
    
    public MyProcessor(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        System.out.println("started");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
               e.printStackTrace();

        }
        latch.countDown();
    }
}

public class CountdownLatches {

    public static void main(String[] args) {
        
        CountDownLatch latch = new CountDownLatch(3);
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        IntStream.range(0,3).forEach(i -> executor.submit(new MyProcessor(latch)));
        
        try {
            latch.await(); // wait to latch 0
        } catch (InterruptedException e) {
                e.printStackTrace();
                
        }
        
        System.out.println("completed");
    }
}
