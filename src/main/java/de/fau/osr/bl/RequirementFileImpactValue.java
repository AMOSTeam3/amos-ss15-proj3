package de.fau.osr.bl;
/**
 * This class represents a file requirement relationship based on impact
 * @author Gayathery Sathya
 */
public class RequirementFileImpactValue {

    float impactPercentage = -1;

    /**
     * constructor
     * @param impactPercentage value of percentage of impact
     */
    public RequirementFileImpactValue(float impactPercentage){

        this.impactPercentage = impactPercentage;
    }

    /**
     * @return returns the impact value as percentage
     */
    public float getImpactPercentage(){
        return impactPercentage;
    }

}
