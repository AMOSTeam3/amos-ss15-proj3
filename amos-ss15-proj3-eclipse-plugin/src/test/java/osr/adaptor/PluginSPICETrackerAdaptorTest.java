/**
 * 
 */
package osr.adaptor;

import static org.junit.Assert.*;

import org.junit.Test;

import osr.adapter.PluginSPICETrackerAdaptor;

/**
 * @author Gayathery
 *
 */
public class PluginSPICETrackerAdaptorTest {
    
    

    /**
     * Test method for {@link osr.adapter.PluginSPICETrackerAdaptor#getInstance()}.
     */
   
    @Test
    public void testGetInstance() {
        PluginSPICETrackerAdaptor adaptor = PluginSPICETrackerAdaptor.getInstance();
        assertNotNull(adaptor);
    }

    /**
     * Test method for {@link osr.adapter.PluginSPICETrackerAdaptor#resetInstance()}.
     */
    @Test
    public void testResetInstance() {
        PluginSPICETrackerAdaptor adaptor = PluginSPICETrackerAdaptor.getInstance();
        assertNotNull(adaptor);
        PluginSPICETrackerAdaptor.resetInstance();
        adaptor = PluginSPICETrackerAdaptor.getInstance();
        assertNotNull(adaptor);
    }

    

}
