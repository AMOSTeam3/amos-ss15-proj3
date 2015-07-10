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

import de.fau.osr.util.matrix.MatrixIndex;
import de.fau.osr.util.matrix.SymmetricMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * This class represents a traceability matrix for requirements
 * @author Gayathery Sathya
 */
public class RequirementsTraceabilityMatrix {

    Logger logger = LoggerFactory.getLogger(RequirementsTraceabilityMatrix.class);

    ArrayList<String> requirements;

    SymmetricMatrix<RequirementsRelation> traceabilityMatrix;

    private final Object lockObject = new Object();

    public RequirementsTraceabilityMatrix(Collection<String> requirements) {

        super();
        this.requirements = new ArrayList<String>(requirements);
        traceabilityMatrix = new SymmetricMatrix<RequirementsRelation>(requirements.size());


    }

    /**
     * method to get the requirements for traceability in an ordered form
     * @return
     */
    public List<String> getOrderedRequirementsArrayForTraceability(){
        final ArrayList<String> OrderedRequirementsArrayForTraceability = requirements;
        return OrderedRequirementsArrayForTraceability;
    }

    /**
     * method to get the actual traceability matrix with relationships
     * @return
     */
    public SymmetricMatrix<RequirementsRelation> getTraceabilityMatrixForRequirements(){
        final SymmetricMatrix<RequirementsRelation> finalTraceabilityMatrix = traceabilityMatrix;
        return finalTraceabilityMatrix;
    }


    /**
     * method to populate the symmetric matrix with the elements of type "RequirementsRelation"
     * based on relationship between Requirements
     * @param fileRequirementList
     * @param filePath path of the file for which traceability is processed
     * @throws IndexOutOfBoundsException
     */
    public void populateMatrix(List<String> fileRequirementList, String filePath) throws IndexOutOfBoundsException{

        logger.debug(" Process populatematrix():"+fileRequirementList.size());
        ArrayList<String> relatedFileList = new ArrayList<String>();
        relatedFileList.add(filePath);
        try {

            ArrayList<String> dependantRequirements = (ArrayList<String>) fileRequirementList;
            int size=dependantRequirements.size();
            MatrixIndex mIndex = new MatrixIndex(-1, -1);
            for(int i = 0 ; i< size ; i++){
                mIndex.setRowIndex(getRequirementIndex(dependantRequirements.get(i)));
                for(int j=i+1;j<size;j++){
                    mIndex.setColumnIndex(getRequirementIndex(dependantRequirements.get(j)));
                    synchronized(lockObject){
                        MatrixIndex mIndexForPassing = new MatrixIndex(mIndex);
                        RequirementsRelation requirementsRelation = traceabilityMatrix.getAt(mIndexForPassing);
                        if (requirementsRelation == null)
                            requirementsRelation = new RequirementsRelation();
                        requirementsRelation.incrementRelations(1, relatedFileList);
                        traceabilityMatrix.setAt(mIndexForPassing, requirementsRelation);
                    }
                }
            }
        }catch(IndexOutOfBoundsException e){
            throw e;
        }
        catch(Exception e){
            logger.error(e.getMessage());
        }

    }

    /**
     * Gets the index of the requirement from the main requirement Arraylist 'requirements'
     * @param requirement requirement id
     * @return it returns the index of the requirement id in the list
     */
    private int getRequirementIndex(String requirement){


        int index =  requirements.indexOf(requirement);
        if (index < 0)
            logger.error("Requirement "+ requirement + " not in master requirement.");

        return index;

    }






}
