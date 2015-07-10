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
package de.fau.osr.bl;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents the relationship between requirements
 * @author Gayathery Sathya
 */
public class RequirementsRelation {

    String requirementX;
    String requirementY;
    Integer commonFilesCount = 0;
    List<String> commonFiles = new ArrayList<String>();


    /**
     * Constructor of the class
     */
    public RequirementsRelation() {
        super();

    }


    /**
     * overloaded constructor with variable setting at construction
     * @param requirementX requirement ID x
     * @param requirementY requirement ID z
     * @param files list of associated files
     */
    public RequirementsRelation(String requirementX, String requirementY,
            List<String> files) {
        super();
        this.requirementX = requirementX;
        this.requirementY = requirementY;
        this.commonFiles = files;
    }


    /**
     * overloaded constructor with variable setting at construction
     * @param requirementX requirement ID x
     * @param requirementY requirement ID z
     * @param commonFilesCount number of common files for the requirement combination
     */
    public RequirementsRelation(String requirementX, String requirementY,
            Integer commonFilesCount) {
        super();
        this.requirementX = requirementX;
        this.requirementY = requirementY;
        this.commonFilesCount = commonFilesCount;
    }


    public String getRequirementX() {
        return requirementX;
    }


    public void setRequirementX(String requirementX) {
        this.requirementX = requirementX;
    }


    public String getRequirementY() {
        return requirementY;
    }


    public void setRequirementY(String requirementY) {
        this.requirementY = requirementY;
    }


    public Integer getCommonFilesCount() {
        return commonFilesCount;
    }


    public void setCommonFilesCount(Integer commonFilesCount) {
        commonFilesCount = commonFilesCount;
    }


    public List<String> getFiles() {
        return commonFiles;
    }


    public void setFiles(List<String> files) {
        this.commonFiles = files;
    }

    public void incrementRelations(int commonFilesCount, List<String> commonFiles)    {
        this.commonFilesCount += commonFilesCount;
        if(commonFiles != null)
            this.commonFiles.addAll(commonFiles);
    }

    public void decrementRelations(int commonFilesCount, List<String> commonFiles)    {
        this.commonFilesCount -= commonFilesCount;
        if(commonFiles != null)
            this.commonFiles.removeAll(commonFiles);
    }

}
