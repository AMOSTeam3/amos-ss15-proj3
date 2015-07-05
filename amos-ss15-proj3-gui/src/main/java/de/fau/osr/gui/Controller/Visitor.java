package de.fau.osr.gui.Controller;

import de.fau.osr.gui.Model.DataElements.*;
import de.fau.osr.gui.View.Presenter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class Visitor {
    Collection<Requirement> requirements = null;
    
    public void setRequirements(Collection<DataElement> dataelements){
        requirements = new ArrayList<Requirement>();
        for(DataElement dataelement: dataelements){
            requirements.add((Requirement) dataelement);
        }
    }
    
    public Presenter toPresenter(AnnotatedLine line){
        if(requirements == null){
            return new Presenter_Impact(line);
        }else{
            Presenter_AnnotatedLine presenter = new Presenter_AnnotatedLine(line);
            presenter.setRequirementID(requirements);
            return presenter;
        }
    }
    
    public Presenter toPresenter(Commit commit){
        return new Presenter_Commit(commit);
    }

    public Presenter toPresenter(CommitFile commitFile){
        ArrayList<CommitFile> commitFiles = new ArrayList<CommitFile>();
        commitFiles.add(commitFile);
        return new Presenter_CommitFile(commitFiles);
    }

    public Presenter toPresenter(ImpactDE impact) {
        ArrayList<ImpactDE> impacts = new ArrayList<>();
        impacts.add(impact);
        // TODO@Flo please return corresponding Presenter class.
        // return new Presenter_Impact(impacts);
        return null;
    }

    public Presenter toPresenter(PathDE filePath) {
        ArrayList<PathDE> filePaths = new ArrayList<>();
        filePaths.add(filePath);
        return new Presenter_Path(filePaths);
    }

    public Presenter toPresenter(Requirement requirement){
        return new Presenter_Requirement(requirement);
    }
    
    public Collection<? extends DataElement> toDataElement(Presenter_AnnotatedLine line){
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.add(line.getLine());
        return result;
    }
    
    public Collection<? extends DataElement> toDataElement(Presenter_Commit commit){
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.add(commit.getCommit());
        return result;
    }

    public Collection<? extends DataElement> toDataElement(Presenter_Path filePath){
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.addAll(filePath.getPathDE());
        return result;
    }

    public Collection<? extends DataElement> toDataElement(Presenter_CommitFile commitFile){
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.addAll(commitFile.getCommitFile());
        return result;
    }


    public Collection<? extends DataElement> toDataElement(Presenter_Requirement requirement){
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.add(requirement.getRequirement());
        return result;
    }
    
    public Collection<? extends DataElement> toDataElement(Presenter_Impact impact){
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.add(impact.getLine());
        return result;
    }
}
