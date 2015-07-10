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
package osr.core.ui.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import osr.adapter.PluginSPICETraceabilityMatrixAdaptor;
import osr.core.RegistrySettings;
import osr.plugin.ui.utility.UIUtility;

public class TraceabilityMatrixAction implements IWorkbenchWindowActionDelegate{

    @Override
    public void run(IAction action) {
        if (RegistrySettings.isPluginEnabled){
            PluginSPICETraceabilityMatrixAdaptor matrix = new PluginSPICETraceabilityMatrixAdaptor();
            matrix.loadTraceabilityMatrixByImpact(); 
        }else
            UIUtility.message("SPICE Inpormation",
                    "Enable Plugin to have this feature");
      
        
    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init(IWorkbenchWindow arg0) {
        // TODO Auto-generated method stub
        
    }

}
