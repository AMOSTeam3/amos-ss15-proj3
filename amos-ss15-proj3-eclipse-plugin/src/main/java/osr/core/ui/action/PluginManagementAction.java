/**
 * 
 */
package osr.core.ui.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import osr.core.RegistrySettings;

/**
 * @author Gayathery
 *
 */
public class PluginManagementAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /*
     * (non-Javadoc) This method is used to call the configuration settings
     * dialog once the Enable Plugin actions is made.
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @Override
    public void run(IAction action) {
        if (!RegistrySettings.isPluginEnabled
                && action.getText().equals("Enable SPICE")) {
            RegistrySettings.isPluginEnabled = true;

            action.setText("Disable SPICE");
            RegistrySettings.configure();
        } else {
            RegistrySettings.isPluginEnabled = false;
            action.setText("Enable SPICE");
        }

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
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

}
