/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package osr.core;

import de.fau.osr.util.AppProperties;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.PlatformUI;

import osr.adapter.PluginSPICEAuthenticationAdaptor;
import osr.plugin.ui.RegistrySettingsDialog;
import osr.plugin.ui.utility.UIUtility;

import java.util.regex.Matcher;

/**
 * @author Gayathery Class to hold values related to the session of the plugin
 *
 */
public class RegistrySettings {

    public static String repoURL = "../.git";
    public static String requirementPattern = AppProperties
            .GetValue("RequirementPattern");
    public static boolean isPluginEnabled = false;
    public static IPartListener listner;

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
        UIUtility.message("Configuration", "User Preferences successfully saved.");
        if (!new PluginSPICEAuthenticationAdaptor().authenticate(
                dialog.getDbUsername(), dialog.getDbPassword()))
            return false;
        return true;
    }
}
