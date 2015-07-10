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
package de.fau.osr.gui.Controller;

import de.fau.osr.gui.Model.DataElements.*;
import de.fau.osr.gui.View.Presenter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class Visitor {
    Collection<Requirement> requirements = null;
    ArrayList<PathDE> filePaths = null;
    
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
        if(filePaths != null){
            return new Presenter_PathImpact(filePaths, impact);            
        }else{
            return new Presenter_ImpactDE(impact);            
        }
    }

    public Presenter toPresenter(PathDE filePath) {
        filePaths = new ArrayList<>();
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
    
    public Collection<? extends DataElement> toDataElement(Presenter_PathImpact filePath){
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

    public Collection<? extends DataElement> toDataElement(
            Presenter_ImpactDE presenter_ImpactDE) {
        ArrayList<DataElement> result = new ArrayList<DataElement>();
        result.add(presenter_ImpactDE.getImpact());
        return result;
    }
}
