package de.fau.osr.gui.Model.DataElements;

import java.util.Collection;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

public class AnnotatedLine extends DataElement {
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        result = prime * result
                + ((requirements == null) ? 0 : requirements.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnnotatedLine other = (AnnotatedLine) obj;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        if (requirements == null) {
            if (other.requirements != null)
                return false;
        } else if (!requirements.equals(other.requirements))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AnnotatedLine [requirements=" + requirements + ", line=" + line
                + "]";
    }

    public AnnotatedLine(Collection<String> requirements, String line) {
        this.requirements = requirements;
        this.line = line;
    }

    private Collection<String> requirements;

    /**
     * @return all associated requirements
     */
    public Collection<String> getRequirements() {
        return requirements;
    }

    private String line;

    /**
     * @return one line of code
     */
    public String getLine() {
        return line;
    }
    
    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

}
