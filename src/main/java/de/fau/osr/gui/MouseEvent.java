package de.fau.osr.gui;

import java.awt.event.MouseListener;

import javax.swing.JList;

public class MouseEvent implements MouseListener {
    //where initialization occurs:
    //Register for mouse events on blankArea and the panel.
 Viewer viewer;
 JList list;
public MouseEvent(Viewer viewer, JList list)
{
	this.viewer = viewer;
	this.list = list;
}

@Override
public void mouseClicked(java.awt.event.MouseEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void mousePressed(java.awt.event.MouseEvent e) {
	viewer.ShowDiff(e.getPoint());
	
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
