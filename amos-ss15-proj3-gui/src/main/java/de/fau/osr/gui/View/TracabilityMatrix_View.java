package de.fau.osr.gui.View;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.util.SpiceTraceabilityProgressBar;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TracabilityMatrix_View {

    /**
     * shows the traceability matrix window with filled in data
     * @param requirementsTraceabilityMatrix matrix to show
     */
    public void showTraceabilityMatrix(RequirementsTraceabilityMatrix requirementsTraceabilityMatrix){
        try {
            TraceabilityMatrixViewHandler trMatrix = new TraceabilityMatrixViewHandler();
            trMatrix.setRequirementsTraceabilityMatrix(requirementsTraceabilityMatrix);
            trMatrix.initTable();
            trMatrix.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            trMatrix.setVisible(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * shows the traceability matrix (by impact) window with filled in data
     * @param requirementsTraceabilityMatrixByImpact the matrix which contains the info for traceability
     */
    public void showTraceabilityMatrixByImpact(RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact){

            TraceabilityMatrixByImpactViewHandler trMatrixByImpact = new TraceabilityMatrixByImpactViewHandler();
            trMatrixByImpact.setRequirementsTraceabilityMatrix(requirementsTraceabilityMatrixByImpact);
            trMatrixByImpact.initTable();
            trMatrixByImpact.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            trMatrixByImpact.setVisible(true);

    }
    /**
     * method to show progress bar for the processing of traceability matrix by impact values
     */
    public void showTraceabilityMatrixByImpactProgressBar(){

        final SpiceTraceabilityProgressBar progressBar = new SpiceTraceabilityProgressBar();
        progressBar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        progressBar.setProgressBarContent("Generating Traceability Matrix (by Impact)");
        progressBar.setVisible(true);

        class showTraceabilityMatrixByImpactProgressBarThread implements Runnable{

            @Override
            public void run() {
                Boolean completion = false;
                while(RequirementsTraceabilityMatrixByImpact.processProgress<=100){
                    progressBar.setProgressBarValue(RequirementsTraceabilityMatrixByImpact.processProgress);
                    try {
                        Thread.sleep(100);

                        if(completion)
                            break;
                        if(RequirementsTraceabilityMatrixByImpact.processProgress == 100){
                            completion = true;
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                progressBar.dispatchEvent(new WindowEvent(progressBar, WindowEvent.WINDOW_CLOSING));

            }

        }
        Thread tr = new Thread(new showTraceabilityMatrixByImpactProgressBarThread());
        tr.start();
    }
}
