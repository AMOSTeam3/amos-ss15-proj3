/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
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
            UIUtility.message("ReqTracker Information",
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
