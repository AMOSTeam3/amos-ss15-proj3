/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.View;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.util.SpiceTraceabilityProgressBar;

import javax.swing.*;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
     * @param tr 
     */
    public void showTraceabilityMatrixByImpactProgressBar(RequirementsTraceabilityMatrixByImpact tr){

        final FutureTask<SpiceTraceabilityProgressBar> progressBarTask = new FutureTask<>( () -> {
        	SpiceTraceabilityProgressBar result = new SpiceTraceabilityProgressBar();
            result.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            result.setProgressBarContent("Generating Traceability Matrix (by Impact)");
            result.setVisible(true);
            return result;
        }); 

        EventQueue.invokeLater(progressBarTask);
		try {
			tr.setProgressBar(progressBarTask.get());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
    }
}
