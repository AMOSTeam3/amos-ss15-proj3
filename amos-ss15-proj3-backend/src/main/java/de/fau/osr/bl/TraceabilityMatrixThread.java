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
package de.fau.osr.bl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * class for Thread of Traceability Matrix processing
 */
class TraceabilityMatrixThread implements Runnable{
    static Tracker tracker;
    String filePath;
    static RequirementsTraceabilityMatrix requirementsTraceabilityMatrixWorker;
    static void setRequirementTraceabilityMatrix(Tracker trackerObject){
        try {
            tracker = trackerObject;
            requirementsTraceabilityMatrixWorker = new RequirementsTraceabilityMatrix(tracker.getRequirementIds());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static RequirementsTraceabilityMatrix getRequirementTraceabilityMatrix(){
        return requirementsTraceabilityMatrixWorker;
    }
    TraceabilityMatrixThread(String filePath){
        this.filePath = filePath;
    }

    @Override
    public void run() throws IndexOutOfBoundsException{
        List<String> fileRequirements;
        try {
            String unixFormatedFilePath = filePath.replaceAll(Matcher.quoteReplacement("\\"), "/");
            fileRequirements = new ArrayList<String>( tracker.getRequirementIdsForFile(unixFormatedFilePath));
            if(!fileRequirements.isEmpty())
                requirementsTraceabilityMatrixWorker.populateMatrix(fileRequirements,unixFormatedFilePath);
        } catch(IndexOutOfBoundsException e){
            throw e;
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
