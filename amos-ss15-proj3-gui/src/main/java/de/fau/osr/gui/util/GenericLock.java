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
