package de.fau.osr.gui.Controller;

import de.fau.osr.gui.Model.DataElements.*;
import de.fau.osr.gui.View.Presenter.*;
import java.util.ArrayList;

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
        return new Presenter_CommitFile(commitFile);
    }
    
    public Presenter toPresenter(Requirement requirement){
        return new Presenter_Requirement(requirement);
    }
    
    public DataElement toDataElement(Presenter_AnnotatedLine line){
        return line.getLine();
    }
    
    public DataElement toDataElement(Presenter_Commit commit){
        return commit.getCommit();
    }
    
    public DataElement toDataElement(Presenter_CommitFile commitFile){
        return commitFile.getCommitFile();
    }
    
    public DataElement toDataElement(Presenter_Requirement requirement){
        return requirement.getRequirement();
    }
    
    public DataElement toDataElement(Presenter_Impact impact){
        return impact.getLine();
    }
}
