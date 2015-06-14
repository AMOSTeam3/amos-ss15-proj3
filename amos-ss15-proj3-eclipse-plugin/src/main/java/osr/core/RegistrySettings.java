package osr.core;

import java.util.regex.Matcher;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import osr.adapter.PluginSPICEAuthenticationAdaptor;
import osr.plugin.ui.RegistrySettingsDialog;
import de.fau.osr.util.AppProperties;

/**
 * @author Gayathery Class to hold values related to the session of the plugin
 *
 */
public class RegistrySettings {

    public static String repoURL = "../.git";
    public static String requirementPattern = AppProperties
            .GetValue("RequirementPattern");
    public static boolean isPluginEnabled = false;

    /**
     * This method opens up a user input dialog and the basic plugin
     * configurations like setting up the repositoryURL , requirement Pattern ,
     * Database connection parameters are made.
     * 
     * @return
     */
    public static boolean configure() {
        Shell activeShell = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getShell();
        RegistrySettingsDialog dialog = new RegistrySettingsDialog(activeShell);
        dialog.create();
        if (dialog.open() == Window.OK) {
            RegistrySettings.repoURL = dialog.getGitURL();
            RegistrySettings.repoURL = RegistrySettings.repoURL.replaceAll(
                    Matcher.quoteReplacement("\\"), "/");
            RegistrySettings.requirementPattern = dialog
                    .getRequirementPattern();
        }
        if (!new PluginSPICEAuthenticationAdaptor().authenticate(
                dialog.getDbUsername(), dialog.getDbPassword()))
            return false;
        return true;
    }
}
