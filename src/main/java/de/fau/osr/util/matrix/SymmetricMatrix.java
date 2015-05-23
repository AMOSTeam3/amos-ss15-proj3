package de.fau.osr.util.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * class to represent a symmetric matrix with element type T
 * @author Gayathery Sathya
 */
public class SymmetricMatrix<T> {
	
	Map<MatrixIndex,T> matrix= new HashMap<MatrixIndex, T>();
			
	int size;
	boolean hasDiagonalElements;

	public SymmetricMatrix(int size) {
		this(size,false);
	}
	
	public SymmetricMatrix(int size, boolean hasDiagonalElements) {
		super();
		this.size = size;
		initMatrix();
		this.hasDiagonalElements = hasDiagonalElements;
		
	}
	/*
	 * method to initialize the matrix elements (to null)
	 */
	public void initMatrix(){
		List<MatrixIndex> element = generateElement(size);
		for(MatrixIndex index: element)
			matrix.put(index, null);
		
	}
    
	public T getAt(MatrixIndex matrixIndex){
		
		return matrix.get(orderElement(matrixIndex));
	}
	
	public void setAt(MatrixIndex matrixIndex, T value){
		matrix.put(orderElement(matrixIndex), value);
	}
   
	public Map<MatrixIndex, T> getMatrix() {
		return matrix;
	}
	
	/**
	 * method to order the indices of the matrix suitable for a symmetric matrix
	 * @param matrixIndex
	 * @return the ordered matrix indices
	 */
	private MatrixIndex orderElement(MatrixIndex matrixIndex){
		Integer temp;
		if (matrixIndex.getRowIndex() > matrixIndex.getColumnIndex()) {
			temp = matrixIndex.getRowIndex();
			matrixIndex.setRowIndex(matrixIndex.getColumnIndex());
			matrixIndex.setColumnIndex(temp);
		}
		
		return matrixIndex;
		    
	}
	/*
	 * method to generate the elements of the symmetric matrix where data can be stored
	 */
	private List<MatrixIndex> generateElement(int size){
		
		List<MatrixIndex> element = new ArrayList<MatrixIndex>();
		int count;
		for(int i = 0 ; i< size ; i++){	
			for(int j = i+1 ; j< size ; j++){
				if(i==j && !hasDiagonalElements){
					continue;	
				}								
				element.add(new MatrixIndex(i, j));
					
				}
			}
		return element;
		
	}

}
