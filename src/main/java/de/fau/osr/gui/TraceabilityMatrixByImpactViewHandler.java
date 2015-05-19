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
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

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
		
		JLabel lblRequirements = new JLabel("Requirements");
		lblRequirements.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblNewLabel = new JLabel("Files");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 617, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(41, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(354, Short.MAX_VALUE)
					.addComponent(lblRequirements)
					.addGap(255))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblRequirements)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(26)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 396, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(183)
							.addComponent(lblNewLabel)))
					.addContainerGap(40, Short.MAX_VALUE))
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
