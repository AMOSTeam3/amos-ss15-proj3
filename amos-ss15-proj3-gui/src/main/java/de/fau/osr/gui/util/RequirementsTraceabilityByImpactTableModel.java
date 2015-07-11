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
package de.fau.osr.gui.util;

import de.fau.osr.bl.RequirementFileImpactValue;
import de.fau.osr.bl.RequirementFilePair;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;

import javax.swing.table.DefaultTableModel;
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
            if(impactValue != null){
                float impactPercentage =  impactValue.getImpactPercentage();                
                if(impactPercentage < 0)
                    impactPercentage = (float)0.0;
                
                return (float)((int)(impactPercentage * 100))/100.0;
            }
            else
                return 0.0;
        }

        else
            return null;
    }

}
