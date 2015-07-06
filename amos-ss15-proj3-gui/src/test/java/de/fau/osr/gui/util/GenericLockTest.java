package de.fau.osr.gui.util;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for the util GenericLock to check for deadlock situation
 * @author Gayathery
 *
 */
public class GenericLockTest {
    
    GenericLock lock = new GenericLock();

    @Test
    public void testLock() {
        
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
    
    @Test
    public void testDeadlockDuringException() {
        
        DeadlockDuringException();
        lock.lock();
        lock.unlock();
        assertTrue(true);
        
    }
    public void DeadlockDuringException(){
        lock.lock();
        try{
            throw new Exception("Test Exception");
        }
        catch(Exception ex){
            
        }
        finally{
            lock.unlock();
        }
        
    }

}
