package de.fau.osr.gui;

import java.awt.event.MouseListener;

import javax.swing.JList;

enum Action {commitsFromRequirement, FilesFromCommit, CodeFromFile};

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
			JList<String> theList1 = (JList<String>) e.getSource();
			int commitIndex = theList1.getSelectedIndex();
			guiController.filesFromCommit(commitIndex);
			break;
		case CodeFromFile:
			JList<String> theList2 = (JList<String>) e.getSource();
			int filesIndex = theList2.getSelectedIndex();
			guiController.codeFromFile(filesIndex);
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
