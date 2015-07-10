/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.util;

import com.google.common.base.Preconditions;
import de.fau.osr.bl.RequirementsRelation;
import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.util.matrix.MatrixIndex;
import de.fau.osr.util.matrix.SymmetricMatrix;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * This class is a customized table generator for Requirement Traceability matrix by requirement relations
 * @author Gayathery Sathya
 */
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

    /*
    @Override
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
