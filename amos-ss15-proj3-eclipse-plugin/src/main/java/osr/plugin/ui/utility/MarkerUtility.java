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
package osr.plugin.ui.utility;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Gayathery 
 * This class serves as aUtility class for Resource Markers.
 *
 */
public class MarkerUtility {

	/**
	 * This method creates a marker for the file.
	 * @param markerId
	 * @param fileResource
	 * @return
	 */
	public static IMarker createMarker(String markerId, IFile fileResource) {
		IResource resourse = (IResource) fileResource;
		IMarker marker = null;
		try {
			marker = resourse.createMarker(markerId);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			marker.setAttribute(IMarker.TRANSIENT, true);
		} catch (CoreException e) {

			e.printStackTrace();
		}
		return marker;
	}

	/**
	 * This method sets the messsage and line number properties for the marker.
	 * @param marker
	 * @param message
	 * @param lineNumber
	 */
	public static void addResourceMarker(IMarker marker, String message,
			int lineNumber) {

		try {
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			
		} catch (CoreException e) {
			
			e.printStackTrace();
		}

		
	}

	/**
	 * @author Gayathery 
	 * This method deletes all the given Markers for the File.
	 * @param file
	 * @param markerTypes
	 */
	public static void deleteMarkers(IFile file, List<String> markerTypes) {
		if (markerTypes != null) {
			for (String type : markerTypes) {
				try {
					IMarker[] markers = file.findMarkers(type, true,
							IResource.DEPTH_INFINITE);
					for (IMarker marker : markers)
						marker.delete();

				} catch (CoreException ce) {
					ce.printStackTrace();
				}
			}
		}

	}

}
