package osr.core;


import java.util.regex.Matcher;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import osr.adapter.PluginSPICETrackerAdaptor;


/**
 * @author Gayathery
 * Class to configure Spice Traceability application settings in Eclipse
 */
public class SpiceTraceabilityConfiguration implements IWorkbenchWindowActionDelegate {
   
    private IWorkbenchWindow window;

    /**
     * The constructor.
     */
    public SpiceTraceabilityConfiguration() {
    }

    /**
     * Method which is used to get user input for data pertaining to the registry configuration of the plugin
     */
    public void run(IAction action) {
       RegistrySettings.configure();
       
    }

    /**
     * 
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * 
     */
    public void dispose() {
    }

    /**
     * 
     */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}

