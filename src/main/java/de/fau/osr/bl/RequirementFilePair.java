package de.fau.osr.bl;
/**
 * @author Gayathery Sathya
 * @desc This class represents file requirement pair
 */
public class RequirementFilePair {

	String fileName;
	String requirement;
	
	public RequirementFilePair(String requirement,String fileName){
		this.fileName = fileName;
		this.requirement = requirement;
	}

	public String getFileName(){
		return fileName;
	}
	
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
