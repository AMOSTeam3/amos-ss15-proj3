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
