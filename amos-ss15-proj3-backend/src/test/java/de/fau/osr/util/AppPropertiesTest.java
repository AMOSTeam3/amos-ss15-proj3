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
package de.fau.osr.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.DBTestHelper;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.interfaces.VcsClient;

public class AppPropertiesTest {
    
    static AppProperties mocked_appProperties;
    
    @BeforeClass
    public static void prepare() throws IOException {      
        
        mocked_appProperties = mock(AppProperties.class);     
    }

    @Test
    public void testGetValue() {
        String key = "TraceabilityMatrixProcessingThreadPoolCount";
        assertNotNull( AppProperties.GetValue(key));
    }

    @Test
    public void testGetValueTestNull() {
        String key = "noKey";
        assertNull( AppProperties.GetValue(key));
    }
    
   
  
    @Test
    public void testGetValueAsInt() {
        String key = "TraceabilityMatrixProcessingThreadPoolCount";
        AppProperties.GetValue(key);
        Integer value = AppProperties.GetValueAsInt(key);
        assertNotNull( value);
        assertTrue(value==15);
    }

}
