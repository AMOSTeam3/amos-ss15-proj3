package de.fau.osr.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fau.osr.util.MatrixIndex;
import de.fau.osr.util.SymmetricMatrix;

public class RequirementsTraceabilityMatrix {
	
	Logger logger = LoggerFactory.getLogger(RequirementsTraceabilityMatrix.class);
	
	List<String> requirements;
	
	SymmetricMatrix<RequirementsRelation> symmetricMatrix;
	
	public RequirementsTraceabilityMatrix(List<String> requirements) {
		
		super();
		this.requirements = requirements;
		symmetricMatrix = new SymmetricMatrix<RequirementsRelation>(requirements.size());
		
	}

	public void populateMatrix(List<String> traceList){
		
		logger.info(" Process populatematrix():"+traceList.size());
		
		
		try {
			
			List<Integer> requirementsIndex = getIndexList(traceList);
			
			List<MatrixIndex> indexForRequirements = generateElement(requirementsIndex);
			
			logger.debug(" Process generateElement():"+indexForRequirements.size());
			
			for(MatrixIndex index:indexForRequirements){
				
				if(symmetricMatrix.findKey(index)){
					
					RequirementsRelation requirementsRelation = symmetricMatrix.getMatrixValue(index);
					
					if ( requirementsRelation == null )
						requirementsRelation = new RequirementsRelation();
					
					requirementsRelation.setCommonFilesCount(requirementsRelation.getCommonFilesCount()+ 1);
					
					symmetricMatrix.setMatrixElement(index, requirementsRelation);
					
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	
	
	private List<Integer> getIndexList(List<String> requirements){
		
		List<Integer> requirementsIndex = new ArrayList<Integer>();
		
		for(String requirement: requirements){
			requirementsIndex.add(getRequirementIndex(requirement));
		}
		
		return requirementsIndex;
	}
	
	
	private int getRequirementIndex(String requirement){
		
		
		int index =  requirements.indexOf(requirement);
		if (index < 0)
			logger.info("Requirement "+ requirement + " not in master requirement.");
		
    	return index;
		
	}
	
	 public List<MatrixIndex> generateElement(List<Integer> indexList){
			
			List<MatrixIndex> element = new ArrayList<MatrixIndex>();
			int count;
			int size = indexList.size();
			for(int i = 0 ; i< size ; i++){	
				count = i+1;
				while(count < size){
					element.add(new MatrixIndex(indexList.get(i), indexList.get(count)));
					count++;
				}
			}
			return element;
			
	}

	public Map<MatrixIndex,RequirementsRelation>  getRequirementsTraceabilityMatrix() {
		return symmetricMatrix.getMatrix();
	}

	
	
	
}
