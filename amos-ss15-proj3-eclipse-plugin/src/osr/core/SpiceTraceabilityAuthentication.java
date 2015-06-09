package osr.core;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * @author Gayathery
 * Class to configure Spice Traceability DB settings
 */
public class SpiceTraceabilityAuthentication implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    /**
     * The constructor.
     */
    public SpiceTraceabilityAuthentication() {
    }

    /**
     * Method which is used to get user input for authentication of Spice traceability database
     */
    public void run(IAction action) {
       
        if(!new Authentication().Perform()){
            return;
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

