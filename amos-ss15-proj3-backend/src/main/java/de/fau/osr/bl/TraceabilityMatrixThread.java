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
