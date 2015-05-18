package de.fau.osr.gui.util;

import javax.swing.table.DefaultTableModel;

import de.fau.osr.bl.RequirementFilePair;
import de.fau.osr.bl.RequirementsRelation;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.util.matrix.MatrixIndex;

public class RequirementsTraceabilityByImpactTableModel extends DefaultTableModel{

	RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact;
	public RequirementsTraceabilityByImpactTableModel(RequirementsTraceabilityMatrixByImpact requirementsTraceabilityMatrixByImpact){
		this.requirementsTraceabilityMatrixByImpact = requirementsTraceabilityMatrixByImpact;
	}
	@Override
	public int getRowCount() {
		if(requirementsTraceabilityMatrixByImpact != null)
			return requirementsTraceabilityMatrixByImpact.getFiles().size();
		else 
			return 0;
	}

	@Override
	public int getColumnCount() {
		if(requirementsTraceabilityMatrixByImpact != null)
			return requirementsTraceabilityMatrixByImpact.getRequirements().size();
		else 
			return 0;
	}

	@Override
	public String getColumnName(int columnIndex) {
	
		if(requirementsTraceabilityMatrixByImpact != null)
			return requirementsTraceabilityMatrixByImpact.getRequirements().get(columnIndex);
		else 
			return null;
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(requirementsTraceabilityMatrixByImpact != null)
			return requirementsTraceabilityMatrixByImpact.getImpactValue(new RequirementFilePair(requirementsTraceabilityMatrixByImpact.getFiles().get(rowIndex), requirementsTraceabilityMatrixByImpact.getRequirements().get(columnIndex))).getImpactPercentage();
		else 
			return null;
	}

}
