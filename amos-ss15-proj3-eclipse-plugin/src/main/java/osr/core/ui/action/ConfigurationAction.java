package osr.core.ui.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import osr.core.RegistrySettings;
import osr.plugin.ui.utility.UIUtility;

/**
 * @author Gayathery Class to configure Spice Traceability application settings
 *         in Eclipse
 */
public class ConfigurationAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /**
     * The constructor.
     */
    public ConfigurationAction() {
    }

    /**
     * Method which is used to get user input for data pertaining to the
     * registry configuration of the plugin.The operation is performed only
     * when the plugin is enabled.
     */
    public void run(IAction action) {
        if (RegistrySettings.isPluginEnabled)
            RegistrySettings.configure();
        else
            UIUtility.message("SPICE Inpormation",
                    "Enable Plugin to have this feature");

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
