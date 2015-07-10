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