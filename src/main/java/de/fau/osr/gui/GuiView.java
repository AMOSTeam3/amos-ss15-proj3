package de.fau.osr.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GuiView{
	private GuiViewElementHandler elementHandler = new GuiViewElementHandler();
	
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
	
	String Pattern_OpeningDialog() {
		String returnValue = JOptionPane.showInputDialog("Choose Requirment Pattern", "Req-(\\d+)");
		return returnValue;
	}

	/**
	 * Create the frame.
	 */
	void showView() {
		this.elementHandler = new GuiViewElementHandler();
		elementHandler.setVisible(true);
	}

	void showErrorDialog(String string) {
		JOptionPane.showMessageDialog(null,
				"Sie müssen ein Repository auswählen!",
				"Fehler bei er Repository Auswahl",
				JOptionPane.ERROR_MESSAGE);
	}

	void clearAllScrollPanes(){
		JPanel panelrequirement = new JPanel(new GridLayout());
		panelrequirement.setBackground(Color.WHITE);
		elementHandler.getRequirementID_scrollPane().setViewportView(panelrequirement);
		
		JPanel panelcommit = new JPanel(new GridLayout());
		panelcommit.setBackground(Color.WHITE);
		elementHandler.getCommit_scrollPane().setViewportView(panelcommit);
		
		JPanel panelfiles = new JPanel(new GridLayout());
		panelfiles.setBackground(Color.WHITE);
		elementHandler.getFiles_scrollPane().setViewportView(panelfiles);
		
		JPanel panelcode = new JPanel(new GridLayout());
		panelcode.setBackground(Color.WHITE);
		elementHandler.getCode_scrollPane().setViewportView(panelcode);
		
		JPanel panelimpact = new JPanel(new GridLayout());
		panelimpact.setBackground(Color.WHITE);
		elementHandler.getImpactPercentage_scrollPane().setViewportView(panelimpact);
	}

	void showRequirements(JList<String> requirements_JList) {
		JPanel panel = new JPanel(new GridLayout());
		
		panel.add(requirements_JList);
		elementHandler.getRequirementID_scrollPane().setViewportView(panel);
		
	}

	void showCommits(JList<String> commitMessages_JList) {
		JPanel panel = new JPanel(new GridLayout());
		
		panel.add(commitMessages_JList);
		elementHandler.getCommit_scrollPane().setViewportView(panel);
	}

	void showFiles(JList<String> commitFileName_JList) {
        JPanel panel = new JPanel(new GridLayout());
		
		panel.add(commitFileName_JList);
		elementHandler.getFiles_scrollPane().setViewportView(panel);
	}

	void showCode(String changeData) {
		JPanel panel = new JPanel(new GridLayout());
		
		JTextArea Code_textArea = new JTextArea(changeData);
		panel.add(Code_textArea);
		elementHandler.getCode_scrollPane().setViewportView(panel);
	}
	
	void addMouseListener(JComponent component, MouseListener actListener){
		component.addMouseListener(actListener);
	}

	void initializeButtonActions(GuiController guiController) {
		elementHandler.initializeButtonActions(guiController);
	}
	
	
}
