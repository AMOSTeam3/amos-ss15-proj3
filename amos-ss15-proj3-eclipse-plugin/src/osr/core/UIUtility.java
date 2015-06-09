package osr.core;

import java.util.regex.Matcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * @author Gayathery
 * Utility class for the functionalities of the plugin
 * 
 */
public class UIUtility {
	
	/**
	 * This is generic JFace input dialogue for String Input.
	 * @param heading
	 * @param presetText
	 * @return
	 */
	public static String inputDialog(String heading, String presetText,String descText) {
		String input=null;
		Display display = PlatformUI.getWorkbench().getDisplay();
		InputDialog dlg = new InputDialog(display.getActiveShell(), heading,
		        descText, presetText, null);
		if (dlg.open() == Window.OK) 
			input = dlg.getValue();
		return input;
	}
	
	/**
	 * This is a generic Message Dialog in eclipse plugin.
	 * @param message1
	 * @param message2
	 */
	public static void message(String message1, String message2) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					MessageDialog.openInformation(window.getShell(), message1,
							message2);

				}
			}
		});

	}
	
	/**
	 * This method returns the current active page 
	 * @return IWorkbenchPage
	 * @throws Exception 
	 */
	public static IWorkbenchPage getActivePage() throws Exception{
		IWorkbenchPage iWorkBenchPage = null;
		try{
			iWorkBenchPage= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		}catch(Exception e){
			throw new Exception ( " Exception in get active page");
		}
		return iWorkBenchPage;
	}
	
	/**
	 * Return the active file in the active eclipse editor.
	 * @return IFile
	 * @throws Exception 
	 */
	public static IFile getActiveFile() throws Exception{
		
       //	String name = getActivePage().getActiveEditor().getEditorInput().getName();
		IFile iFile = null;
		try{
			iFile = (IFile)getActivePage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		}catch(Exception e){
			throw new Exception("Exception in getting active file ");
		}
		return iFile;
	}
	
	/**
	 * This method returns the current workspace path
	 * @return
	 * @throws Exception
	 */
	public static String getWorkspacePath() throws Exception{
		try{
			return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		}catch(Exception e){
			throw new Exception("Exception in workspace path ");
		}
	}
	
	/**
	 * This method returns the current active Project path.
	 * @param isAppend
	 * @return
	 * @throws Exception
	 */
	public static String getProjectPath(boolean isAppendGitDir) throws Exception{
		try{
			String projectPath = null;
			//String fullFilePath = (String) getActiveFile().getRawLocation().toString();
			IProject iProject = ((IResource) getActiveFile()).getProject();
			if(iProject != null){
				IProjectDescription desc = iProject.getDescription();
				if(desc != null)
					projectPath = desc.getLocationURI() != null ? desc.getLocationURI().toString() : appendProjectToWorkspace(iProject);
						
			}
			if(isAppendGitDir)
				return appendProjectToGit(projectPath);
			return projectPath;
			//return (String) ((IResource) getActiveFile()).getProject().getDescription().getLocationURI().toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Exception in workspace project path ");
		}
	}
	
	private static String appendProjectToWorkspace(IProject iProject) throws CoreException, Exception{
		return getWorkspacePath()+ "/"+iProject.getDescription().getName();
	}
	
	private static String appendProjectToGit(String path) {
		return path + "/"+".git";
	}
	
	/**
	 * This method returns the git file path relative to the repository location.
	 * @return
	 * @throws Exception
	 */
	public static String getGitFilepath() throws Exception{
		String fullFilePath = (String) getActiveFile().getRawLocation().toString();
		String gitURL =RegistrySettings.repoURL;
		gitURL = gitURL.substring(0, gitURL.lastIndexOf('/')+1);
		/*String projectPath = getProjectPath(false);	
		projectPath=projectPath.replace("file:/", "");*/
		String gitpath = fullFilePath.replace(gitURL,"");
		String unixFormatedFilePath = gitpath.replaceAll(Matcher.quoteReplacement("\\"), "/");
		return unixFormatedFilePath;
		
	}
}
