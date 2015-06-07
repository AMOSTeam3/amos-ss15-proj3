package de.fau.osr.gui.Controller;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.ElementHandler.ElementHandler;
import de.fau.osr.gui.View.Presenter.Presenter;

public class Transformer {
    
    public static Presenter[] transformDataElementsToPresenters(Collection<? extends DataElement> dataElements){
        Presenter[] result = new Presenter[dataElements.size()];
        int i = 0;
        for(DataElement dataElement: dataElements){
            result[i] = dataElement.visit(new Visitor_Swing());
            i++;
        }
        
        return result;
    }
    
    public static void process(ElementHandler elementHandler, Runnable buttonAction, Supplier<Collection<? extends DataElement>> fetching){
        Collection<? extends DataElement> dataElements = fetching.get();
        Presenter[] presenter = transformDataElementsToPresenters(dataElements);
        elementHandler.setScrollPane_Content(presenter);
        elementHandler.setButtonAction(buttonAction);
    }
    
    public static Collection<Requirement> castToRequirement(Collection<DataElement> dataElement){
        return (Collection)dataElement;
    }
}
