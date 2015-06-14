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
