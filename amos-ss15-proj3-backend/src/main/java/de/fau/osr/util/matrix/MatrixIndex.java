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
package de.fau.osr.util.matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * class to represent the index of an element in a matrix
 * @author Gayathery Sathya
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
