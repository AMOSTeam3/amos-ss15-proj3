package osr.adapter;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.View.TraceabilityMatrixByImpactViewHandler;
import de.fau.osr.gui.util.SpiceTraceabilityProgressBar;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * @author Gayathery
 * This class uses the backed Traceability Matrix functionality, the calls are adapted in this class.
 */
public class PluginSPICETraceabilityMatrixAdaptor {
    
    public void loadTraceabilityMatrixByImpact(){
        PluginSPICETrackerAdaptor.getInstance(); 
        class TraceabilityMatrixViewerThread implements Runnable {

            @Override
            public void run() {
                showTraceabilityMatrixByImpactProgressBar();
                TraceabilityMatrixByImpactViewHandler trMatrixByImpact = new TraceabilityMatrixByImpactViewHandler();
                trMatrixByImpact.setRequirementsTraceabilityMatrix(PluginSPICETrackerAdaptor.getTracker().generateRequirementsTraceabilityByImpact());
                trMatrixByImpact.initTable();
                trMatrixByImpact.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                trMatrixByImpact.setVisible(true);
                
            }

        }
        Thread tr = new Thread(new TraceabilityMatrixViewerThread());
        tr.start(); 
       
    }
    
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
