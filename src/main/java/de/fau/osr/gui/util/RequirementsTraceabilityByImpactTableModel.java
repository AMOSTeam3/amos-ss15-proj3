package de.fau.osr.gui.util;

import javax.swing.table.DefaultTableModel;

import de.fau.osr.bl.RequirementFileImpactValue;
import de.fau.osr.bl.RequirementFilePair;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
/**
 * This class is a customized table generator for Requirement Traceability matrix by impact values
 * @author Gayathery Sathya
 */
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
		
		if(requirementsTraceabilityMatrixByImpact != null){
			RequirementFileImpactValue impactValue = requirementsTraceabilityMatrixByImpact.getImpactValue(new RequirementFilePair( requirementsTraceabilityMatrixByImpact.getRequirements().get(columnIndex),requirementsTraceabilityMatrixByImpact.getFiles().get(rowIndex)));
			if(impactValue != null)
				return impactValue.getImpactPercentage();
			else 
				return 0.0;
		}
			
		else 
			return null;
	}

}
