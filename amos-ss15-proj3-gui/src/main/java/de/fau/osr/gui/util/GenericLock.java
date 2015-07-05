package de.fau.osr.gui.util;

/**
 * Class containing an implementation to provide a basic synchronization mechanism
 * @author Gayathery
 *
 */
public class GenericLock{

    private boolean isLocked = false;

    /**
     * locks the thread and waits until it is notified by a call to come out of wait
     */
    public synchronized void lock(){
    
      while(isLocked){
        try {
            wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      }
      isLocked = true;
    }

    /**
     * unlock the locked thread
     */
    public synchronized void unlock(){
      isLocked = false;
      notify();
    }
  }
