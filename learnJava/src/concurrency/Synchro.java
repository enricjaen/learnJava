/*
* (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
* These materials are unpublished, proprietary, confidential source code of
* Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
* Nexmo Inc retains all titles to an intellectual property rights in these materials.
*/
package concurrency;

public class Synchro {

    private int count = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Synchro app = new Synchro();
        app.doWork();
    }
    
    public synchronized void increment() {
        count++;
    }
    
    public void doWork() throws InterruptedException {
        Runnable task = () -> { for (int i=0; i<1000; i++) increment(); };
        
        Thread t1 = new Thread ( task );
        Thread t2 = new Thread ( task );

        t1.start();  // main thread
        t2.start();  // main thread
        
        t1.join();  
        t2.join();
        
        System.out.println(count);
    }
}
