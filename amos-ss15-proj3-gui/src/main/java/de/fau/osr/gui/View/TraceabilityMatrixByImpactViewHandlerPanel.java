package de.fau.osr.gui.View;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.View.Renderer.RowHeaderRenderer;
import de.fau.osr.gui.util.RequirementsTraceabilityByImpactTableModel;
import de.fau.osr.gui.util.UiTools;
import de.fau.osr.util.matrix.MatrixTools;

/**
 * This class is the Panel for showing the requirement traceability by impact value
 * This panel can be reused with different frames and panels
 * @author Gayathery
 *
 */
public class TraceabilityMatrixByImpactViewHandlerPanel extends JPanel {


    
 
    private JTable table;
    private JScrollPane scrollPane;
    private RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact;
    private JButton matrixButton;
    JButton btnToCsv;
    JButton btnToPdf;


    /**
     * Launch the application.
     */
   /* public static void main(String[] args) {
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
*/
    /**
     * Create the panel.
     */
    public TraceabilityMatrixByImpactViewHandlerPanel() {

        final String title = "ReqTracker - Traceability Matrix by Impact";

        setBounds(100, 100, 999, 686);
        setBorder(new EmptyBorder(5, 5, 5, 5));


        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JLabel lblRequirements = new JLabel("Requirements");
        lblRequirements.setFont(new Font("Tahoma", Font.BOLD, 11));

        JLabel lblFiles = new JLabel("Files");
        lblFiles.setFont(new Font("Tahoma", Font.BOLD, 11));

        btnToCsv = new JButton("To CSV");
        btnToCsv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    String filename = getFileName(MatrixTools.ExportType.CSV);
                    boolean result = MatrixTools.SaveMatrixToCsv(requirementsTraceabilityMatrixByImpact,
                            UiTools.chooseFile(filename));
                    UiTools.dialogStatusMessage(result, filename);
            }
        });

        btnToPdf = new JButton("To PDF");
        btnToPdf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filename = getFileName(MatrixTools.ExportType.PDF);
                boolean result = MatrixTools.SaveMatrixToPdf(requirementsTraceabilityMatrixByImpact, 
                                 UiTools.chooseFile(filename),title);
                UiTools.dialogStatusMessage(result, filename);
            }
        });
        matrixButton = new JButton("Generate Traceability Matrix");

        GroupLayout gl_contentPane = new GroupLayout(this);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblFiles)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(matrixButton)
                            .addGap(18)
                            .addComponent(btnToCsv)
                            .addGap(18)
                            .addComponent(btnToPdf)
                            .addGap(158)
                            .addComponent(lblRequirements)
                            .addContainerGap())
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
                                .addComponent(matrixButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        setLayout(gl_contentPane);

    }

    public void setInternalGenerationVisibility(Boolean value){
        matrixButton.setVisible(value);
    }
    
    public void eraseTable(){
        if(table != null)
        {
        table.setVisible(false);
        table.getTableHeader().setVisible(false);
        scrollPane.getRowHeader().setVisible(false);
        }
    }
    
    public void setInternalGenerationEnable(Boolean value){

        matrixButton.setEnabled(value);
    }
    
    public void setExportEnable(Boolean value){

            btnToCsv.setEnabled(value);
            btnToPdf.setEnabled(value);
    }
    
    public void setRequirementsTraceabilityMatrix(RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact){
        this.requirementsTraceabilityMatrixByImpact = requirementsTraceabilityMatrixByImpact;
    }

    /**
     * Method which initialises the table with data
     */
    public void initTable()
    {

            final RequirementsTraceabilityMatrixByImpact traceabilityMatrixByImpact = this.requirementsTraceabilityMatrixByImpact;
            table = new JTable(new RequirementsTraceabilityByImpactTableModel(traceabilityMatrixByImpact)){
                @Override
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

            ListModel<String> listModel = new AbstractListModel<String>() {

                  public int getSize() {
                    return traceabilityMatrixByImpact.getFiles().size();
                  }

                  public String getElementAt(int index) {
                      File filePath = new File(traceabilityMatrixByImpact.getFiles().get(index));
                    return filePath.getName();
                  }
                };

            JList<String> rowHeader = new JList<>(listModel);
            rowHeader.setFixedCellWidth(-1);
            rowHeader.setFixedCellHeight(table.getRowHeight());

            rowHeader.setCellRenderer(new RowHeaderRenderer<String>(table));
           
            scrollPane.setRowHeaderView(rowHeader);


    }


    public String getFileName(MatrixTools.ExportType exportType){
        String prefix = "TraceabilityMatrix";
        return MatrixTools.generateFileName(exportType,prefix,
                    requirementsTraceabilityMatrixByImpact.getRepositoryName(),'_');
    }
    
    public void setImpactAction(Runnable action) {
        if(matrixButton.getActionListeners().length == 0){
            matrixButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread tr = new Thread(action);
                tr.start();
            }
        });
        }
    }


}
