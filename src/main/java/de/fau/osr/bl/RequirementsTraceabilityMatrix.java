package de.fau.osr.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fau.osr.util.matrix.MatrixIndex;
import de.fau.osr.util.matrix.SymmetricMatrix;
/**
 * @author Gayathery Sathya
 * @desc This class represents a traceability matrix for requirements
 */
public class RequirementsTraceabilityMatrix {
	
	Logger logger = LoggerFactory.getLogger(RequirementsTraceabilityMatrix.class);
	
	ArrayList<String> requirements;
	
	SymmetricMatrix<RequirementsRelation> traceabilityMatrix;
	
	private  Object lockObject = new Object();
	
	public RequirementsTraceabilityMatrix(Collection<String> requirements) {
		
		super();
		this.requirements = new ArrayList<String>(requirements);
		traceabilityMatrix = new SymmetricMatrix<RequirementsRelation>(requirements.size());

		
	}
	/*
	 * method to get the requirements for traceability in an ordered form
	 */
	public List<String> getOrderedRequirementsArrayForTraceability(){
		final ArrayList<String> OrderedRequirementsArrayForTraceability = requirements;
		return OrderedRequirementsArrayForTraceability;
	}
	/*
	 * method to get the actual traceability matrix with relationships
	 */
	public SymmetricMatrix<RequirementsRelation> getTraceabilityMatrixForRequirements(){
		final SymmetricMatrix<RequirementsRelation> finalTraceabilityMatrix = traceabilityMatrix;
		return finalTraceabilityMatrix;
	}

	/*
	 * method to populate the symmetric matrix with the elements of type "RequirementsRelation"
	 * based on relationship between Requirements
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
	/*
	 * Gets the index of the requirement from the main requirement Arraylist 'requirements'
	 */
	private int getRequirementIndex(String requirement){
		
		
		int index =  requirements.indexOf(requirement);
		if (index < 0)
			logger.error("Requirement "+ requirement + " not in master requirement.");
		
    	return index;
		
	}
	
	

	
	
	
}
