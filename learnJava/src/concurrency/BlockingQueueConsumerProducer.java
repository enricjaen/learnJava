package concurrency;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueConsumerProducer {

    static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    
    private static void producer () throws InterruptedException { // adds elements to the queue
        Random r = new Random();
        while (true) {
            queue.put(r.nextInt(100)); // waits until there is space in the queue 
        }
    }
    
    private static void consumer() throws InterruptedException {
        Random r = new Random();
        while (true) {
            Thread.sleep(100);
            if (r.nextInt(10)==0){
                Integer value = queue.take();  // patiently waits until something there is an
                System.out.println("taken value: "+value+ " queue size is "+ queue.size());
            }
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
