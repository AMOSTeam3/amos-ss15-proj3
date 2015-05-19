package de.fau.osr.gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.util.RequirementsTraceabilityByImpactTableModel;

public class TraceabilityMatrixByImpactViewHandler extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPane;
	private RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TraceabilityMatrixByImpactViewHandler frame = new TraceabilityMatrixByImpactViewHandler();
					frame.initTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TraceabilityMatrixByImpactViewHandler() {
		this.requirementsTraceabilityMatrixByImpact = requirementsTraceabilityMatrixByImpact;
		setTitle("Spice Traceability - Traceability Matrix by Impact");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 647, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(21, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(17, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void setRequirementsTraceabilityMatrix(RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact){
		this.requirementsTraceabilityMatrixByImpact = requirementsTraceabilityMatrixByImpact;
	}
	
	public void initTable()
	{

			final RequirementsTraceabilityMatrixByImpact traceabilityMatrixByImpact = this.requirementsTraceabilityMatrixByImpact;
			table = new JTable(new RequirementsTraceabilityByImpactTableModel(traceabilityMatrixByImpact));
			table.setCellSelectionEnabled(true);
			Font columnHeaderFont = new Font("Arial",Font.BOLD,10);
			table.getTableHeader().setFont(columnHeaderFont);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			scrollPane.setViewportView(table);
			
			ListModel listModel = new AbstractListModel() {
			     
			      public int getSize() {
			        return traceabilityMatrixByImpact.getFiles().size();
			      }

			      public Object getElementAt(int index) {
			        return traceabilityMatrixByImpact.getFiles().get(index);
			      }
			    };
			
			JList rowHeader = new JList(listModel);
		    rowHeader.setFixedCellWidth(200);

		    rowHeader.setFixedCellHeight(table.getRowHeight());

		    rowHeader.setCellRenderer(new RowHeaderRenderer(table));
		    
		    scrollPane.setRowHeaderView(rowHeader);

		
	}
}
