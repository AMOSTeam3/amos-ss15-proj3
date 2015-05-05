package de.fau.osr.gui;

import java.awt.event.MouseListener;

import javax.swing.JList;

enum Action {ShowCommits, ShowRequirements, ShowFiles, ShowCode};

public class MouseEvent implements MouseListener {
	
	Action action;
    Viewer viewer;
    JList list;
 
public MouseEvent(Viewer viewer, JList list, Action action)
{
	this.viewer = viewer;
	this.list = list;
	this.action = action;
}

@Override
public void mouseClicked(java.awt.event.MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mousePressed(java.awt.event.MouseEvent e) {
	switch(action){
	case ShowRequirements:
		break;
	case ShowCommits:
		viewer.ShowCommits();
		break;
	case ShowFiles:
		viewer.ShowFiles();
		break;
	case ShowCode:
		viewer.ShowCode();
		break;
	}
//	viewer.ShowDiff(e.getPoint());
	
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
