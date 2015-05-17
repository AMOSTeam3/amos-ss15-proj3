package de.fau.osr.util.matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * @author Gayathery Sathya
 * @desc class to represent the index of an element in a matrix
 */
public class MatrixIndex{

	Logger logger = LoggerFactory.getLogger(MatrixIndex.class);
	
	int rowIndex;
	int columnIndex;
	
	public MatrixIndex(int x, int y) {
		super();
		this.rowIndex = x;
		this.columnIndex = y;
			
	}
	
	public MatrixIndex(MatrixIndex mIndex) {
		super();
		this.rowIndex = mIndex.getRowIndex();
		this.columnIndex = mIndex.getColumnIndex();
			
	}

	public int getRowIndex() {
		return rowIndex;
	}



	public void setRowIndex(int rowIndex) throws IndexOutOfBoundsException{
		if(rowIndex<0)
			throw new IndexOutOfBoundsException("Matrix index cannot be negative");
		this.rowIndex = rowIndex;
	}
	
	public void set(int rowIndex,int columnIndex) {
		setRowIndex(rowIndex);
		setColumnIndex(columnIndex);
	}



	public int getColumnIndex() {
		return columnIndex;
	}



	public void setColumnIndex(int columnIndex) throws IndexOutOfBoundsException{
		if(columnIndex<0)
			throw new IndexOutOfBoundsException("Matrix index cannot be negative");
		this.columnIndex = columnIndex;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rowIndex;
		result = prime * result + columnIndex;
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
		MatrixIndex other = (MatrixIndex) obj;
		if (rowIndex != other.rowIndex)
			return false;
		if (columnIndex != other.columnIndex)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "MatrixIndex [x=" + rowIndex + ", y=" + columnIndex + "]";
	}
	
	
	
	
}
