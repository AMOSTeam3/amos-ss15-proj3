package osr.core;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Gayathery
 * This is an implementation class for IPartListener.
 * This is to attach the necessary markers for the partBroughtToTop
 *
 */
public class IPartListnerImplementation implements IPartListener{

	Set<IFile> activeList = new HashSet<IFile>();
	
	@Override
	public void partActivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
		RequirementMarker r_marker = new RequirementMarker();
		r_marker.processActiveFileRequirementMarker(activeList);
		
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
