/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
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
