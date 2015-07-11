/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.View;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.View.Renderer.RowHeaderRenderer;
import de.fau.osr.gui.util.RequirementsTraceabilityByImpactTableModel;
import de.fau.osr.gui.util.UiTools;
import de.fau.osr.util.matrix.MatrixTools;
/**
 * This class is the GUI for showing the requirement traceability by impact value
 * @author Gayathery Sathya
 */
public class TraceabilityMatrixByImpactViewHandler extends JFrame {
    
    public TraceabilityMatrixByImpactViewHandlerPanel contentPane;
    private JTable table;
    private JScrollPane scrollPane;
    private RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact;
    private JLabel iconLabel;
    
    public TraceabilityMatrixByImpactViewHandler() {
        final String title = "ReqTracker - Traceability Matrix by Impact";
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 999, 686);
        contentPane = new TraceabilityMatrixByImpactViewHandlerPanel();
        contentPane.setInternalGenerationVisibility(false);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        
        setIcon();
    }
    
    private void setIcon(){
        ImageIcon img = new ImageIcon(GuiViewElementHandler.class.getResource("/icons/ReqTrackerLogo.png"));
        setIconImage(img.getImage());
        //iconLabel.setIcon(img);
    }
    
  

}







