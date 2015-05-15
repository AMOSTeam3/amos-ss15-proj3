package de.fau.osr.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SymmetricMatrix<T> {
	
	Map<MatrixIndex,T> matrix= new HashMap<MatrixIndex, T>();
			
	int size;

	public SymmetricMatrix(int size) {
		super();
		this.size = size;
		initMatrix();
	}
	
	public void initMatrix(){
		List<MatrixIndex> element = generateElement(size);
		for(MatrixIndex index: element)
			matrix.put(index, null);
		
	}
    
	public T getMatrixValue(MatrixIndex matrixIndex){
		
		return matrix.get(orderElement(matrixIndex));
	}
	
	public void setMatrixElement(MatrixIndex matrixIndex, T value){
		matrix.put(orderElement(matrixIndex), value);
	}
    
	public boolean findKey(MatrixIndex matrixIndex){
		
		return matrix.containsKey(matrixIndex);
	}
   
	public Map<MatrixIndex, T> getMatrix() {
		return matrix;
	}
	
	private MatrixIndex orderElement(MatrixIndex matrixIndex){
		Integer temp;
		
		if (matrixIndex.getX() > matrixIndex.getY()) {
			temp = matrixIndex.getX();
			matrixIndex.setX(matrixIndex.getY());
			matrixIndex.setY(temp);
		}
		
		return matrixIndex;
		    
	}
	private List<MatrixIndex> generateElement(int size){
		
		List<MatrixIndex> element = new ArrayList<MatrixIndex>();
		int count;
		for(int i = 0 ; i< size ; i++){	
			count = i+1;
			while(count < size){
				element.add(new MatrixIndex(i, count));
				count++;
			}
		}
		return element;
		
	}

}
