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
