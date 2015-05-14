package de.fau.osr.gui;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.io.IOException;

enum Action {CommitsAndFilesFromRequirement, FilesFromCommit, CodeFromFile, RequirementsAndCommitsFromFile, CommitsFromRequirementAndFile, RequirementsFromFileAndCommit, RequirementsAndFilesFromCommit, CommitsAndCodeFromRequirementAndFile, RequirementToLinkage, CommitToLinkage, RequirmentsFromCode};

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
        try {
            switch (action) {
            case CommitsAndFilesFromRequirement:
                JList<String> theList = (JList<String>) e.getSource();
                String value = (String) theList.getSelectedValue();
                guiController.commitsFromRequirement(value);
                guiController.filesFromRequirement(value);
                break;
            case FilesFromCommit:
                JList<String> theList0 = (JList<String>) e.getSource();
                int value0 = theList0.getSelectedIndex();
                guiController.filesFromCommit(value0);
                break;
            case CodeFromFile:
                JList<String> theList2 = (JList<String>) e.getSource();
                int filesIndex = theList2.getSelectedIndex();
                JList<String> theList12 = (JList<String>) guiController.requirements_JList;
                String value12 = theList12.getSelectedValue();
                guiController.codeFromFile(filesIndex, value12);
                break;
            case RequirementsAndCommitsFromFile:
                JList<String> theList3 = (JList<String>) e.getSource();
                String value1 = (String) theList3.getSelectedValue();
                guiController.requirementsFromFile(value1);
                guiController.commitsFromFile(value1);
                break;
            case CommitsFromRequirementAndFile:
                JList<String> theList4 = (JList<String>) guiController.requirements_JList;
                String value2 = (String) theList4.getSelectedValue();
                JList<String> theList7 = (JList<String>) guiController.commitFileName_JList;
                String value5 = (String) theList7.getSelectedValue();
                guiController.commitsFromRequirementAndFile(value2, value5);
                break;
            case CommitsAndCodeFromRequirementAndFile:
                JList<String> theList8 = (JList<String>) guiController.requirements_JList;
                String value6 = (String) theList8.getSelectedValue();
                JList<String> theList9 = (JList<String>) guiController.commitFileName_JList;
                int value7 = theList9.getSelectedIndex();
                guiController.commitsFromRequirementAndFile(value6, value7);
                guiController.codeFromFile(value7, value6);
                break;
            case RequirementsFromFileAndCommit:
                JList<String> theList5 = (JList<String>) e.getSource();
                int value3 = theList5.getSelectedIndex();
                JList<String> theList10 = (JList<String>) guiController.commitFileName_JList;
                String value8 = (String) theList10.getSelectedValue();
                guiController.requirementsFromFileAndCommit(value3, value8);
                break;
            case RequirementsAndFilesFromCommit:
                JList<String> theList6 = (JList<String>) e.getSource();
                int value4 = theList6.getSelectedIndex();
                guiController.filesFromCommit(value4);
                guiController.requirementsFromCommit(value4);
				break;
			case RequirementToLinkage:
				JList<String> theList11 = (JList<String>) e.getSource();
				String value9 = (String) theList11.getSelectedValue();
				guiController.RequirementToLinkage(value9);
				break;
			case CommitToLinkage:
				JList<String> theList13 = (JList<String>) e.getSource();
				String value10 = (String) theList13.getSelectedValue();
				guiController.CommitToLinkage(value10);
				break;
			case RequirmentsFromCode:
				guiController.requirementsFromCode(guiController.commitFileName_JList.getSelectedIndex(), guiController.code_JList.getSelectedIndex());
				break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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
