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
     * 
     */
    public void run(IAction action) {
        if(RegistrySettings.repoURL==null || RegistrySettings.repoURL.isEmpty()){
            RegistrySettings.repoURL = UIUtility.inputDialog("SPICE Traceability - Preferences : Requirement Path", "../.git","Enter the Git Repository Path");
            RegistrySettings.repoURL = RegistrySettings.repoURL.replaceAll(Matcher.quoteReplacement("\\"), "/");
            PluginSPICETrackerAdaptor.resetInstance();
        }
        else{
            RegistrySettings.repoURL = UIUtility.inputDialog("SPICE Traceability - Preferences : Requirement Path", RegistrySettings.repoURL,"Enter the Git Repository Path");
            RegistrySettings.repoURL = RegistrySettings.repoURL.replaceAll(Matcher.quoteReplacement("\\"), "/");
            PluginSPICETrackerAdaptor.resetInstance();
        }
        
        if(RegistrySettings.requirementPattern==null || RegistrySettings.requirementPattern.isEmpty()){
            RegistrySettings.requirementPattern = UIUtility.inputDialog("SPICE Traceability - Preferences : Requirement Pattern", "../.git","Enter the Requirement Pattern");
            PluginSPICETrackerAdaptor.resetInstance();
        }
        else{
            RegistrySettings.requirementPattern = UIUtility.inputDialog("SPICE Traceability - Preferences : Requirement Pattern", RegistrySettings.requirementPattern,"Enter the Requirement Pattern");
            PluginSPICETrackerAdaptor.resetInstance();
        }
            
       
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

