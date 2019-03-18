package concurrency;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;


public class DeadLocks {



    public static void main(String[] args) throws InterruptedException {

        BankRunner runner = new BankRunner();
        DeadLocks app = new DeadLocks();
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


class BankRunner {

    private Lock lock1 = new ReentrantLock(); // is can be locked several times 
    private Lock lock2 = new ReentrantLock(); // is can be locked several times 
    
    private Account account1 = new Account();
    private Account account2 = new Account();
    
    private void adquireLocks(Lock lock1, Lock lock2) throws InterruptedException {
        while(true) {
            
            boolean gotLock1 = false;
            boolean gotLock2 = false;
            
            try {
                gotLock1 = lock1.tryLock();
                gotLock2 = lock2.tryLock();
            } finally {
                if (gotLock1 && gotLock2) {
                    return;
                }
                if (gotLock1) {
                    lock1.unlock();
                }
                if (gotLock2) {
                    lock2.unlock();
                }
                Thread.sleep(1); // locks not adquired
            }
        
        }
    }

    public void firstThread() throws InterruptedException {
        
        Random random = new Random();
        IntStream.range(0, 10000).forEach(i-> {
            
            try {
                adquireLocks(lock1, lock2);
                Account.transfer(account1,account2,random.nextInt(100));                
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }); 
        
        
    }

    public void secondThread() throws InterruptedException {
        
        Random random = new Random();
        IntStream.range(0, 10000).forEach(i-> {
            // inverse order lock, causes deadlock 
            //lock2.lock();
            //lock1.lock();

            
            try {
                adquireLocks(lock2, lock1);
                Account.transfer(account2,account1,random.nextInt(100));                
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }); 
        
    }

    public void finished() {
        System.out.println("account1 balance "+account1.getBalance());
        System.out.println("account2 balance "+account2.getBalance());
        System.out.println("total balance = "+(account1.getBalance()+account2.getBalance()));

    }
}

class Account {

    int balance = 10000;
    public static void transfer(Account account1, Account account2, int amount) {
        account1.withdraw(amount);
        account2.deposit(amount);
    }

    private void deposit(int amount) {
        balance += amount;
    }

    private void withdraw(int amount) {
        balance -= amount;
    }
    
    public int getBalance() {
        return balance;
    }
}

