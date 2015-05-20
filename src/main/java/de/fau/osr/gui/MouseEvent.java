package de.fau.osr.gui;

import javax.swing.*;

import de.fau.osr.core.vcs.base.CommitFile;

import java.awt.event.MouseListener;
import java.io.IOException;

enum Action {CommitsAndFilesFromRequirement, FilesFromCommit, CodeFromFile, RequirementsAndCommitsAndCodeFromFile, CommitsFromRequirementAndFile, RequirementsFromFileAndCommit, RequirementsAndFilesFromCommit, CommitsAndCodeFromRequirementAndFile, RequirementToLinkage, CommitToLinkage, RequirmentsFromCode};

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
                guiController.codeFromFile(guiController.commitFile_JList.getSelectedValue(), guiController.requirements_JList.getSelectedValue());
                break;
            case RequirementsAndCommitsAndCodeFromFile:
                guiController.requirementsFromFile(guiController.commitFile_JList.getSelectedValue());
                guiController.commitsFromFile(guiController.commitFile_JList.getSelectedValue());
                guiController.codeFromFile(guiController.commitFile_JList.getSelectedValue());
                break;
            case CommitsFromRequirementAndFile:
                JList<String> theList4 = (JList<String>) guiController.requirements_JList;
                String value2 = (String) theList4.getSelectedValue();
                JList<CommitFile> theList7 = (JList<CommitFile>) guiController.commitFile_JList;
                CommitFile value5 = (CommitFile) theList7.getSelectedValue();
                guiController.commitsFromRequirementAndFile(value2, value5);
                break;
            case CommitsAndCodeFromRequirementAndFile:
                guiController.commitsFromRequirementAndFile(guiController.requirements_JList.getSelectedValue(), guiController.commitFile_JList.getSelectedValue());
                guiController.codeFromFile(guiController.commitFile_JList.getSelectedValue(), guiController.requirements_JList.getSelectedValue());
                break;
            case RequirementsFromFileAndCommit:
                JList<String> theList5 = (JList<String>) e.getSource();
                int value3 = theList5.getSelectedIndex();
                guiController.requirementsFromFileAndCommit(value3, guiController.commitFile_JList.getSelectedValue());
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
				guiController.requirementsFromCode(guiController.commitFile_JList.getSelectedValue(), guiController.code_JList.getSelectedIndex());
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
