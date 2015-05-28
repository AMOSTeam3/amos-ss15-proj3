package de.fau.osr.bl;
/**
 * This class represents file requirement pair
 * @author Gayathery Sathya
 */
public class RequirementFilePair {

    String fileName;
    String requirement;

    /**
     * @param requirement Requirement ID
     * @param fileName filename with path
     */
    public RequirementFilePair(String requirement,String fileName){
        this.fileName = fileName;
        this.requirement = requirement;
    }

    /**
     * @return returns the filename of the pair
     */
    public String getFileName(){
        return fileName;
    }

    /**
     * @return returns the requirement of the pait
     */
    public String getRequirement(){
        return requirement;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result
                + ((requirement == null) ? 0 : requirement.hashCode());
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
        RequirementFilePair other = (RequirementFilePair) obj;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (requirement == null) {
            if (other.requirement != null)
                return false;
        } else if (!requirement.equals(other.requirement))
            return false;
        return true;
    }

}
