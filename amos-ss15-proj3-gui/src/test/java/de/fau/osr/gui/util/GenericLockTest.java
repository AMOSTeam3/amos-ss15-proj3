package de.fau.osr.gui.util;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GenericLockTest {

    @Test
    public void testLock() {
        GenericLock lock = new GenericLock();
        class TestThread extends Thread {

            public void run() {
                lock.lock();
                lock.unlock();
            }
        }
        try { 
        lock.lock();
        Thread testThread = new Thread(new TestThread());
        testThread.setName("TestThread");
        testThread.start();        
        
            Thread.sleep(500);
            testThread.interrupt();            
            
            lock.unlock();
            lock.lock();
            lock.unlock();
            
        } catch (InterruptedException e) {
            assertTrue(false);
        }
        catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(true);
        
    }

}
