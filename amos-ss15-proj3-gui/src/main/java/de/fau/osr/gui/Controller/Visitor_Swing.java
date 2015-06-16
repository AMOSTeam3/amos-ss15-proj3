package de.fau.osr.gui.Controller;
import java.util.Collection;
import de.fau.osr.gui.Model.DataElements.DataElement;

public class Visitor_Swing extends Visitor {
    public Visitor_Swing(Collection<DataElement> requirements){
        setRequirements(requirements);
    }
    
    public Visitor_Swing(){
        
    }
}
