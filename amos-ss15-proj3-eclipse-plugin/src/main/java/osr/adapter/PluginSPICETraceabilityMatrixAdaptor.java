package osr.adapter;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.View.TracabilityMatrix_View;

/**
 * @author Gayathery This class uses the backed Traceability Matrix
 *         functionality, the calls are adapted in this class.
 */
public class PluginSPICETraceabilityMatrixAdaptor {

    TracabilityMatrix_View matrixView = new TracabilityMatrix_View();

    public void loadTraceabilityMatrixByImpact() {
        PluginSPICETrackerAdaptor.getInstance();
        class TraceabilityMatrixViewerThread implements Runnable {

            @Override
            public void run() {
                RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact = PluginSPICETrackerAdaptor
                        .getTracker()
                        .generateRequirementsTraceabilityByImpact();

                matrixView.showTraceabilityMatrixByImpactProgressBar(requirementsTraceabilityMatrixByImpact);
                requirementsTraceabilityMatrixByImpact.Process();
                matrixView
                        .showTraceabilityMatrixByImpact(requirementsTraceabilityMatrixByImpact);

            }

        }
        Thread thread = new Thread(new TraceabilityMatrixViewerThread());
        thread.start();

    }

}