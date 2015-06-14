package osr.core;

import java.util.regex.Matcher;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import osr.adapter.PluginSPICEAuthenticationAdaptor;
import osr.adapter.PluginSPICETrackerAdaptor;
import osr.plugin.ui.RegistrySettingsDialog;

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
		exec();
		addListener();
		

	}
	
	/**
	 * Initial startup method of the plugin
	 */
	public void exec() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                PluginSPICETrackerAdaptor.resetInstance();
                if(!RegistrySettings.configure())
                    return;
                }
        });
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
				IPartListnerImplementation listner = new IPartListnerImplementation();
				iw.getActivePage().addPartListener(listner);
			}
		});
	}
	
}
