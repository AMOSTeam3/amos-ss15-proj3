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
/**
 * This class represents file requirement pair
 * @author Gayathery Sathya
 */
public class RequirementFilePair {

    String fileName;
    String requirement;

    /**
     * @param requirement Requirement ID
     * @param fileName filename with path
     */
    public RequirementFilePair(String requirement,String fileName){
        this.fileName = fileName;
        this.requirement = requirement;
    }

    /**
     * @return returns the filename of the pair
     */
    public String getFileName(){
        return fileName;
    }

    /**
     * @return returns the requirement of the pait
     */
    public String getRequirement(){
        return requirement;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result
                + ((requirement == null) ? 0 : requirement.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RequirementFilePair other = (RequirementFilePair) obj;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (requirement == null) {
            if (other.requirement != null)
                return false;
        } else if (!requirement.equals(other.requirement))
            return false;
        return true;
    }

}
