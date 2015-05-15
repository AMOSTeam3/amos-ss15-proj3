package de.fau.osr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatrixIndex {

	Logger logger = LoggerFactory.getLogger(MatrixIndex.class);
	
	int x;
	int y;
	
	public MatrixIndex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
			
	}
	
	

	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "MatrixIndex [x=" + x + ", y=" + y + "]";
	}
	
	
	
	
}
