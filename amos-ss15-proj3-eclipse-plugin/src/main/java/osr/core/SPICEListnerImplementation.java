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
package osr.core;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.hibernate.engine.transaction.jta.platform.internal.ResinJtaPlatform;

import osr.plugin.ui.utility.UIUtility;

/**
 * @author Gayathery
 * This is an implementation class for IPartListener.
 * This is to attach the necessary markers for the partBroughtToTop
 *
 */
public class SPICEListnerImplementation implements IPartListener{

	Set<IFile> activeList = new HashSet<IFile>();
	
	@Override
	public void partActivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
	    if(RegistrySettings.isPluginEnabled){
    		RequirementMarker r_marker = new RequirementMarker();
    		r_marker.processActiveFileRequirementMarker(activeList);
	    }
	}

	@Override
	public void partClosed(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void printWorkSpacePath() throws Exception{
		UIUtility.message("Project Path", UIUtility.getProjectPath(true));
	}


}
