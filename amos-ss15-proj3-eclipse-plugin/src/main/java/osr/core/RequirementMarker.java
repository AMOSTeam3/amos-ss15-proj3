package osr.core;

import java.util.ArrayList;
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
				String[] lines = trackerAdaptor.getRequirementLineLinkForFile(UIUtility.getGitFilepath());
				for(int i = 0 ; i < lines.length; i++){
					if(!lines[i].isEmpty())
						addResourceMarker(file, "REQ-"+lines[i], i+1, true);
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
