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
