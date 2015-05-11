package de.fau.osr.gui;

import java.awt.event.MouseListener;

import javax.swing.JList;

enum Action {commitsFromRequirement, FilesFromCommit, CodeFromFile, RequirementsFromFile, CommitsFromRequirementAndFile, RequirementsFromFileAndCommit};

public class MouseEvent implements MouseListener {

	GuiController guiController;
	Action action;

	public MouseEvent(GuiController guiController, Action action) {
		this.guiController = guiController;
		this.action = action;
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		switch (action) {
		case commitsFromRequirement:
			JList<String> theList = (JList<String>) e.getSource();
			String value = (String) theList.getSelectedValue();
			guiController.commitsFromRequirement(value);
			break;
		case FilesFromCommit:
			JList<String> theList0 = (JList<String>) e.getSource();
			int value0 = theList0.getSelectedIndex();
			guiController.filesFromCommit(value0);
			break;
		case CodeFromFile:
			JList<String> theList2 = (JList<String>) e.getSource();
			int filesIndex = theList2.getSelectedIndex();
			guiController.codeFromFile(filesIndex);
			break;
		case RequirementsFromFile:
			JList<String> theList3 = (JList<String>) e.getSource();
			String value1 = (String) theList3.getSelectedValue();
			guiController.requirementsFromFile(value1);
			guiController.commitsFromFile(value1);
			break;
		case CommitsFromRequirementAndFile:
			JList<String> theList4 = (JList<String>) e.getSource();
			String value2 = (String) theList4.getSelectedValue();
			guiController.commitsFromRequirementAndFile(value2);
			break;
		case RequirementsFromFileAndCommit:
			JList<String> theList5 = (JList<String>) e.getSource();
			String value3 = (String) theList5.getSelectedValue();
			guiController.requirementsFromFileAndCommit(value3);
			break;
		}
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
