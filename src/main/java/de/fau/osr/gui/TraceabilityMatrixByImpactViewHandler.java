package de.fau.osr.gui;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.util.RequirementsTraceabilityByImpactTableModel;
import de.fau.osr.util.matrix.MatrixTools;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
		setTitle("Spice Traceability - Traceability Matrix by Impact");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		
		JButton btnToCsv = new JButton("To CSV");
		btnToCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MatrixTools.SaveMatrixToCsv(requirementsTraceabilityMatrixByImpact, chooseFile("result.csv"));
			}
		});
		
		JButton btnToPdf = new JButton("To PDF");
		btnToPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MatrixTools.SaveMatrixToPdf(requirementsTraceabilityMatrixByImpact, chooseFile("result.pdf"));
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblNewLabel)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(btnToCsv)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_contentPane.createSequentialGroup()
																.addPreferredGap(ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
																.addComponent(lblRequirements)
																.addGap(255))
														.addGroup(gl_contentPane.createSequentialGroup()
																.addGap(18)
																.addComponent(btnToPdf)
																.addContainerGap())))
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 617, GroupLayout.PREFERRED_SIZE)
												.addContainerGap(33, Short.MAX_VALUE))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblRequirements)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnToCsv)
								.addComponent(btnToPdf))
							.addGap(18)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 396, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(183)
							.addComponent(lblNewLabel)))
					.addContainerGap(25, Short.MAX_VALUE))
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


	//todo move to gui utils?
	private File chooseFile(String filename) {
//		final String ext = extension;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");
		fileChooser.setSelectedFile(new File(filename));

		int userSelection = fileChooser.showSaveDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}



}
