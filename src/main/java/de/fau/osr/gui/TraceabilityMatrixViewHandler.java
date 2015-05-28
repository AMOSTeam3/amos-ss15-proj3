package de.fau.osr.gui;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.gui.util.MatrixTableModel;
import de.fau.osr.util.matrix.MatrixIndex;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;
/**
 * This class is the GUI for requirement traceability by requirement relations
 * @author Gayathery Sathya
 */
public class TraceabilityMatrixViewHandler extends JFrame {


    private JPanel contentPane;
    private JTable table;
    private JScrollPane scrollPane;
    private RequirementsTraceabilityMatrix requirementsTraceabilityMatrix;



    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TraceabilityMatrixViewHandler frame = new TraceabilityMatrixViewHandler();
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
     * @throws IOException
     */
    public TraceabilityMatrixViewHandler() throws IOException {
        setTitle("SpiceTraceability-Requirement Traceability Matrix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1157, 679);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        scrollPane = new JScrollPane();
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 1094, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(16, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 578, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE))
        );


        contentPane.setLayout(gl_contentPane);
    }

    public void setRequirementsTraceabilityMatrix(RequirementsTraceabilityMatrix requirementsTraceabilityMatrix){
        this.requirementsTraceabilityMatrix = requirementsTraceabilityMatrix;
    }
    void initTable()
    {

            final RequirementsTraceabilityMatrix traceabilityMatrix = this.requirementsTraceabilityMatrix;
            table = new JTable(new MatrixTableModel(traceabilityMatrix)){
                public String getToolTipText(java.awt.event.MouseEvent event){
                    String toolTipText = null;
                    java.awt.Point p = event.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int columIndex = columnAtPoint(p);

                    try {
                        List<String> fileStringList = traceabilityMatrix.getTraceabilityMatrixForRequirements().getAt(new MatrixIndex(rowIndex, columIndex)).getFiles();
                        StringBuilder toolTipTextBuilder = new StringBuilder();
                        toolTipTextBuilder.append("<html>");
                        for(String file:fileStringList){
                            toolTipTextBuilder.append(file);
                            toolTipTextBuilder.append("<br>");

                        }
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

            ListModel<String> listModel = new AbstractListModel<String>() {

                  public int getSize() {
                    return traceabilityMatrix.getOrderedRequirementsArrayForTraceability().size();
                  }

                  public String getElementAt(int index) {
                    return traceabilityMatrix.getOrderedRequirementsArrayForTraceability().get(index);
                  }
                };

            JList<String> rowHeader = new JList<>(listModel);
            rowHeader.setFixedCellWidth(50);

            rowHeader.setFixedCellHeight(table.getRowHeight());
            rowHeader.setCellRenderer(new RowHeaderRenderer<String>(table));
            scrollPane.setRowHeaderView(rowHeader);


    }
}

