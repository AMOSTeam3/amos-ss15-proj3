package de.fau.osr.gui.util;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.google.common.base.Preconditions;

import de.fau.osr.bl.RequirementsRelation;
import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.util.matrix.MatrixIndex;
import de.fau.osr.util.matrix.SymmetricMatrix;


public class MatrixTableModel extends DefaultTableModel{
	
	private SymmetricMatrix<RequirementsRelation>  matrix;
	
	private List<String> columns;
	
	/**
	 * @param requirementsMatrix
	 */
	public MatrixTableModel(RequirementsTraceabilityMatrix requirementsMatrix) {
		super();
	    Preconditions.checkNotNull(requirementsMatrix, "RequirementsTraceabilityMatrix cannot be null.");
		this.matrix = requirementsMatrix.getTraceabilityMatrixForRequirements();
		this.columns= requirementsMatrix.getOrderedRequirementsArrayForTraceability();
	}

	@Override
	public int getRowCount() {
		if(columns != null)
			return columns.size();
		else 
			return 0;
	}

	@Override
	public int getColumnCount() {
		return getRowCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
	
		if(columns != null || !columns.isEmpty())
			return columns.get(columnIndex);
		return null;
	}
/*
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}*/

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(matrix == null)
			return null;	
		MatrixIndex matrixIndex = new MatrixIndex(rowIndex, columnIndex);
		RequirementsRelation relation =  matrix.getAt(matrixIndex);
		if(relation == null)
			return 0;
		return matrix.getAt(matrixIndex).getCommonFilesCount();
	}

	/*@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}
	*/

}
