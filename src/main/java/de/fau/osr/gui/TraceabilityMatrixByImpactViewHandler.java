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
/**
 * This class is the GUI for requirement traceability by impact value
 * @author Gayathery Sathya
 */
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
        setBounds(100, 100, 999, 686);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JLabel lblRequirements = new JLabel("Requirements");
        lblRequirements.setFont(new Font("Tahoma", Font.BOLD, 11));

        JLabel lblFiles = new JLabel("Files");
        lblFiles.setFont(new Font("Tahoma", Font.BOLD, 11));

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
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblFiles)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(btnToCsv)
										.addGap(18)
										.addComponent(btnToPdf)
										.addGap(158)
										.addComponent(lblRequirements)
										.addGap(260))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 901, Short.MAX_VALUE)
										.addGap(33))))
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
						.addGap(20)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
												.addComponent(btnToCsv, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnToPdf, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(lblRequirements))
										.addGap(18)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(183)
										.addComponent(lblFiles)))
						.addGap(5))
        );
        contentPane.setLayout(gl_contentPane);
    }

    public void setRequirementsTraceabilityMatrix(RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact){
        this.requirementsTraceabilityMatrixByImpact = requirementsTraceabilityMatrixByImpact;
    }

    /**
     * Method which initialiyes the table with data
     */
    public void initTable()
    {

            final RequirementsTraceabilityMatrixByImpact traceabilityMatrixByImpact = this.requirementsTraceabilityMatrixByImpact;
            table = new JTable(new RequirementsTraceabilityByImpactTableModel(traceabilityMatrixByImpact)){
                public String getToolTipText(java.awt.event.MouseEvent event){
                    String toolTipText = null;
                    java.awt.Point p = event.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int columIndex = columnAtPoint(p);

                    try {
                        String requirement = traceabilityMatrixByImpact.getRequirements().get(columIndex);
                        String file = traceabilityMatrixByImpact.getFiles().get(rowIndex);
                        StringBuilder toolTipTextBuilder = new StringBuilder();
                        toolTipTextBuilder.append("<html>");
                        toolTipTextBuilder.append("<font size=\"3\" color=\"green\">");
                        toolTipTextBuilder.append("<b>");
                        toolTipTextBuilder.append("Requirement : "+requirement );
                        toolTipTextBuilder.append("<br>");
                        toolTipTextBuilder.append("File : "+file);
                        toolTipTextBuilder.append("</b>");
                        toolTipTextBuilder.append("</font>");
                        toolTipTextBuilder.append("</html>");
                        toolTipText = toolTipTextBuilder.toString();
                    } catch (RuntimeException e1) {
                        //catch null pointer exception if mouse is over an empty line
                    }

                    return toolTipText;
                }
            };
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
                      File filePath = new File(traceabilityMatrixByImpact.getFiles().get(index));
                    return filePath.getName();
                  }
                };

            JList rowHeader = new JList(listModel);
            rowHeader.setFixedCellWidth(-1);
            rowHeader.setFixedCellHeight(table.getRowHeight());

            rowHeader.setCellRenderer(new RowHeaderRenderer(table));

            scrollPane.setRowHeaderView(rowHeader);


    }


    //todo move to gui utils?
    private File chooseFile(String filename) {
//        final String ext = extension;
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


