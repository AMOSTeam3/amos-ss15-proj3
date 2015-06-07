package osr.core;

import java.util.regex.Matcher;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

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
	
	public void exec() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
            	RegistrySettings.repoURL = UIUtility.inputDialog("Repository Path", "../.git");
            	RegistrySettings.repoURL = RegistrySettings.repoURL.replaceAll(Matcher.quoteReplacement("\\"), "/");
            }
        });
    }
	
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
