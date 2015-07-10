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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

import osr.adapter.PluginSPICETrackerAdaptor;
import osr.plugin.ui.utility.MarkerUtility;
import osr.plugin.ui.utility.UIUtility;

/**
 * @author Gayathery
 *
 */
public class RequirementMarker {
	
	PluginSPICETrackerAdaptor trackerAdaptor;

	/**
	 * This method created Markers displaying the requirement number
	 * for each affected lines of a current active file.
	 * @param activeFiles
	 */
	public void processActiveFileRequirementMarker(Set<IFile> activeFiles) {

		IFile file;
		try {
			file = UIUtility.getActiveFile();
			if (!activeFiles.contains(file)) {
				activeFiles.add(file);
				trackerAdaptor = PluginSPICETrackerAdaptor.getInstance();
				List<Collection<String>> lines = trackerAdaptor.getRequirementLineLinkForFile(UIUtility.getGitFilepath());
				for(int i = 0 ; i < lines.size(); i++){
					for(String req : lines.get(i))
						addResourceMarker(file, req, i-1, true);
				}
			}
		} catch (Exception e) {
		    //TODO proper handling of exception
			UIUtility.message("Info","No Traceability info for the File.");
		}
	}

	/**
	 * This method set the marker on a file at a given line number
	 * @param fileResource
	 * @param message
	 * @param lineNumber
	 * @param isFirst
	 */
	private static void addResourceMarker(IFile fileResource, String message,
			int lineNumber, boolean isFirst) {

		IMarker marker = MarkerUtility.createMarker("org.amos.requirement",fileResource);
		if (marker.exists()) {
			if (!isFirst)
				deleteMarkers(fileResource);
			MarkerUtility.addResourceMarker(marker, message,lineNumber);
		} else {
			// handle null marker
		}

	}

	/**
	 * This method deletes all the given list of markers for a given file.
	 * @param file
	 */
	private static void deleteMarkers(IFile file) {
		List<String> markerTypes = new ArrayList<String>();
		markerTypes.add("org.eclipse.core.resources.textmarker");
		MarkerUtility.deleteMarkers(file, markerTypes);
	}

	
}
