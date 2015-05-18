
package de.fau.osr.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.gui.GuiViewElementHandler.ButtonState;

/*
 * View part of the MVC. This Class is responsible for the setting up the UI and interacting with the 
 * Elements. Whenever the texts or functionality of UI-Elements are changed, this class must be called.
 */
public class GuiView{
	public static class HighlightedLine{
		String line;
		boolean highlighted;
		public HighlightedLine(String line, boolean hightlighted){
			this.line = line;
			this.highlighted = hightlighted;
		}
	}

	
	private class HighlightedLine_Renderer implements ListCellRenderer<HighlightedLine>{
		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends HighlightedLine> list, HighlightedLine value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if(value.highlighted){
				renderer.setForeground(Color.RED);
			}
			renderer.setText(value.line);
			
			return renderer;
		}
	}
	
	private class CommitFile_Renderer implements ListCellRenderer<CommitFile>{
		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends CommitFile> list, CommitFile value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			switch(value.commitState){
			case MODIFIED:
				renderer.setBackground(Color.YELLOW);
				break;
			case ADDED:
				renderer.setBackground(Color.GREEN);
				break;
			case DELETED:
				renderer.setBackground(Color.RED);
				break;
			default:
				renderer.setBackground(Color.WHITE);
				break;
			}
			renderer.setText(value.newPath.getName().toString() + " - " + value.impact);
			
			return renderer;
		}
	}

	//The UI-Elements themselves are handled by the Element Handler. 
	private GuiViewElementHandler elementHandler;
	
	GuiView(GuiController guiController) {
		elementHandler = new GuiViewElementHandler(guiController);
	}
	
	/*
	 * Setting up the initial Dialog to choose the Repository.
	 * @return File to the chosen directory. Keep in mind, that the file is not checked yet.
	 * @throws IOException whenever the user cancels the Dialog.
	 * (This can not be solved)
	 */
	File Repo_OpeningDialog() throws IOException{
		JFileChooser chooser = new JFileChooser("..");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileHidingEnabled(false);

		int returnValue = chooser.showDialog(null,"Auswahl des Repository");
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}else {
			throw new IOException("Fehler bei er Repository Auswahl");
		}
	}
	
	/*
	 * Opening the initial Dialog to choose a Pattern for searching for requirements in commit messages
	 * or external sources.
	 * @return String containing the directly the user input. Not yet checked whether it's a proper pattern
	 */
	String Pattern_OpeningDialog(String currentPattern) {
		String msg = "Gebe \"Requirement-Pattern\" als RegExp ein.\n" +
				"Mehrere \"Requirement-Pattern\" müssen gemäß der RegExp-Syntax\n" +
				"mit | (Pipe) getrennt eingegeben werden.";
		return JOptionPane.showInputDialog(msg, currentPattern);
	}
	
	/*
	 * Method to open a option dialog for configuration where user can select which 
	 * configuration needs to be modified
	 * @return integer containing the option selected for configuration (TODO int to be converted to 
	 * a proper enum)
	 */
	int Configure_OptionDialog(){
		Object[] options = { "Change Repository", "Change Requirement Pattern"};
		return JOptionPane.showOptionDialog(null, "Choose to Configure", "SpiceTraceability Configuration", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	}

	/*
	 * Creating the UI. Frame + Elements
	 */
	void showView() {
		elementHandler.setVisible(true);
	}

	/*
	 * Showing an Dialog to the User. Marked as Error dialog.
	 * @parameter message to be presented to the user
	 */
	void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
	}
	
	/*
	 * Showing a dialog to the user. Marked as Information dialog
	 * @parameter message to be presented to the user
	 */
	void showInformationDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	/*
	 * Clearing all scrollpanes. Containing the Code_ScrollPane. And clearing all Textfields.
	 * Deactivating Linkage_Button
	 * Color is set to the initial white.
	 */
	void clearAll(){
		clearRequirements();
		
		clearCommits();
		
		clearFiles();
		
		clearCode();
		
		clearImpactPercentage();
		
		elementHandler.getCommit_textField().setText("");
		elementHandler.getRequirementID_textField().setText("");
		
		switchLinkage_Button(ButtonState.Deactivate);
	}

	void clearImpactPercentage() {
		JPanel panelimpact = new JPanel(new GridLayout());
		panelimpact.setBackground(Color.WHITE);
		elementHandler.getImpactPercentage_scrollPane().setViewportView(panelimpact);
	}

	void clearFiles() {
		JPanel panelfiles = new JPanel(new GridLayout());
		panelfiles.setBackground(Color.WHITE);
		elementHandler.getFiles_scrollPane().setViewportView(panelfiles);
	}

	
	void clearRequirements() {
		JPanel panelrequirement = new JPanel(new GridLayout());
		panelrequirement.setBackground(Color.WHITE);
		elementHandler.getRequirementID_scrollPane().setViewportView(panelrequirement);
	}

	void clearCommits() {
		JPanel panelcommit = new JPanel(new GridLayout());
		panelcommit.setBackground(Color.WHITE);
		elementHandler.getCommit_scrollPane().setViewportView(panelcommit);
	}

	void clearCode() {
		JPanel panelcode = new JPanel(new GridLayout());
		panelcode.setBackground(Color.WHITE);
		elementHandler.getCode_scrollPane().setViewportView(panelcode);
	}

	/*
	 * Showing all Elements of the JList parameter in the RequirementsID_Scrollpane
	 * @parameter requirements_JList containing the Elements to be displayed
	 */
	void showRequirements(JList<String> requirements_JList) {
		JPanel panel = new JPanel(new GridLayout());
		
		panel.add(requirements_JList);
		elementHandler.getRequirementID_scrollPane().setViewportView(panel);
		
	}

	/*
	 * Showing all Elements of the JList parameter in the Commit_Scrollpane
	 * @parameter commitMessage_JList containing the Elements to be displayed
	 */
	void showCommits(JList<String> commitMessages_JList) {
		JPanel panel = new JPanel(new GridLayout());
		
		panel.add(commitMessages_JList);
		elementHandler.getCommit_scrollPane().setViewportView(panel);
	}

	/*
	 * Showing all Elements of the JList parameter in the Files_Scrollpane
	 * @parameter commitFile_JList containing the Elements to be displayed
	 */
	void showFiles(JList<CommitFile> commitFileName_JList) {
        JPanel panel = new JPanel(new GridLayout());
		
        CommitFile_Renderer CommitFile_Renderer = new CommitFile_Renderer();
        commitFileName_JList.setCellRenderer(CommitFile_Renderer);
        
		panel.add(commitFileName_JList);
		elementHandler.getFiles_scrollPane().setViewportView(panel);
	}

	/*
	 * Showing the parameter String in the Code_Scrollpane.
	 * Until now. There is no further processing of the data. This will probably change
	 * with Req-11
	 * @parameter changeData String to be presented
	 */
	void showCode(JList<HighlightedLine> code_JList) {
		JPanel panel = new JPanel(new GridLayout());

		HighlightedLine_Renderer renderer = new HighlightedLine_Renderer();
		code_JList.setCellRenderer(renderer);

		panel.add(code_JList);
		elementHandler.getCode_scrollPane().setViewportView(panel);
	}
	
	/*
	 * Adding a Mouselistener to the passed component 
	 */
	void addMouseListener(JComponent component, MouseListener actListener){
		component.addMouseListener(actListener);
	}

	void showLinkageRequirement(String requirementID) {
		elementHandler.getRequirementID_textField().setText(requirementID);
	}
	
	void showLinkageCommit(String commit) {
		elementHandler.getCommit_textField().setText(commit);
	}

	void switchLinkage_Button(ButtonState buttonState){
		elementHandler.switchLinkageButton(buttonState);
	}
}

