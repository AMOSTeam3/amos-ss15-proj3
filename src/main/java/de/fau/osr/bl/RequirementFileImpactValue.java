package de.fau.osr.bl;
/**
 * @author Gayathery Sathya
 * @desc This class represents a file requirement relationship based on impact
 */
public class RequirementFileImpactValue {

	float impactPercentage;
	
	public RequirementFileImpactValue(float impactPercentage){
		
		this.impactPercentage = impactPercentage;
	}
	
	public float getImpactPercentage(){
		return impactPercentage;
	}
	
}
