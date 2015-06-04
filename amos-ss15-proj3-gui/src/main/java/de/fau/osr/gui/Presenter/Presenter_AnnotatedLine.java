package de.fau.osr.gui.Presenter;

import de.fau.osr.core.vcs.interfaces.VcsClient.AnnotatedLine;

public class Presenter_AnnotatedLine {
    private AnnotatedLine line;

    public AnnotatedLine getLine() {
        return line;
    }

    public void setLine(AnnotatedLine line) {
        this.line = line;
    }
    
    public Presenter_AnnotatedLine(AnnotatedLine line){
        setLine(line);
    }
    
    public boolean isHighlighted(String RequirementID){
        return line.getRequirements().contains(RequirementID);
    }
    
    @Override
    public String toString(){
        return line.getLine();
    }
}
