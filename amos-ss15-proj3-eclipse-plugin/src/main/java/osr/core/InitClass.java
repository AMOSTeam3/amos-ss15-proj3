package osr.core;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import osr.adapter.PluginSPICETrackerAdaptor;

/**
 * @author Gayathery 
 * 		   This class implements IStartUp interface and performs the
 *         startup activities for spice traceability. It has a PartListenr which
 *         is added to the current workbench to track the Resources that are to
 *         be monitored like getting Active file attributes.
 * 
 *
 */
public class InitClass implements IStartup {

	
	@Override
	public void earlyStartup() {
	    PluginSPICETrackerAdaptor.resetInstance();
		addListener();
		

	}
	
	/**
	 * Method to add a listener to editor resources of eclipse
	 */
	public void addListener() {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				SPICEListnerImplementation listner = new SPICEListnerImplementation();
				iw.getActivePage().addPartListener(listner);
			}
		});
	}
	
}
