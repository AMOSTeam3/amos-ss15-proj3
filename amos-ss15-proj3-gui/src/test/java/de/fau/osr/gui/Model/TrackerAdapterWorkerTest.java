/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package de.fau.osr.gui.Model;


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.db.DBTestHelper;



/**
 * @author z002a0wj
 *
 */
public class TrackerAdapterWorkerTest {

    static VcsClient client;
 
    
    static Tracker mockedTracker;
    static GitVcsClient mockedClient;
    static DataSource mockedSource;
    static TrackerAdapter mockedTrackerAdapter;
    static TrackerAdapterWorker mockedTrackerAdapterWorker;
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void prepare() throws IOException {

        
        mockedClient = mock(GitVcsClient.class);
        mockedSource = mock(DataSource.class);
        mockedTracker = mock(Tracker.class);
       // mockedTracker = Mockito.spy(new Tracker(mockedClient, mockedSource, null, DBTestHelper.createH2SessionFactory()));
        mockedTrackerAdapter = Mockito.spy(new TrackerAdapter(mockedTracker, false));
        mockedTrackerAdapterWorker = Mockito.spy(new TrackerAdapterWorker(mockedTrackerAdapter));
        
    }
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

         
    }

    /**
     * Test method for {@link de.fau.osr.gui.Model.TrackerAdapterWorker#getAllRequirements()}.
     */
    @Test
    public void testGetAllRequirements() {     

        

            Mockito.doReturn(getSampleRequirements()).when(mockedTrackerAdapter).getAllRequirements();

        Calendar calc = Calendar.getInstance();
        mockedTrackerAdapterWorker.globalChangeTime = calc.getTime();
        mockedTrackerAdapterWorker.getAllRequirements = mockedTrackerAdapterWorker.globalChangeTime ;
        mockedTrackerAdapterWorker.requirements = new ArrayList<Requirement>();        
        Collection<Requirement> requirements = mockedTrackerAdapterWorker.getAllRequirements();
        assertTrue(requirements.size() == 0);
        Calendar calc2 = Calendar.getInstance();
        calc2.setTime(mockedTrackerAdapterWorker.globalChangeTime);
        calc2.add(Calendar.HOUR, 1);
        mockedTrackerAdapterWorker.globalChangeTime = calc2.getTime();
        
         requirements = mockedTrackerAdapterWorker.getAllRequirements();
        assertTrue(requirements.size() > 0);
        
      
    }
    
    public Collection<de.fau.osr.core.Requirement> getSampleRequirements(){
        Collection<de.fau.osr.core.Requirement> requirements = new ArrayList<de.fau.osr.core.Requirement>();
        requirements.add(new de.fau.osr.core.Requirement("1","1","1",null,1));
        return requirements;
    }

}
