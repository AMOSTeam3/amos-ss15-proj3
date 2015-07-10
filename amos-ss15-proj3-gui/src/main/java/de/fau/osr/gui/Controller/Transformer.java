/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.Controller;

import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.ElementHandler.ElementHandler;
import de.fau.osr.gui.View.Presenter.Presenter;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class Transformer {
    private static Visitor visitor = new Visitor_Swing();
    
    public static void setVisitor(Visitor visitortmp){
        visitor = visitortmp;
    }
    
    public static Presenter[] transformDataElementsToPresenters(Collection<? extends DataElement> dataElements){
        Presenter[] result = new Presenter[dataElements.size()];
        int i = 0;
        for(DataElement dataElement: dataElements){
            result[i] = dataElement.visit(visitor);
            i++;
        }
        
        return result;
    }
    
    public static Presenter[] transformDataElementsToPresenters(List<? extends DataElement> dataElements1, List<? extends DataElement> dataElements2){
        Presenter[] result = new Presenter[dataElements1.size()];
        int i = 0;
        for(DataElement dataElement1: dataElements1){
            DataElement dataElement2 = dataElements2.get(i);
            dataElement1.visit(visitor);
            result[i] = dataElement2.visit(visitor);
            i++;
        }
        
        return result;
    }
    
    public static void process(ElementHandler elementHandler, Runnable buttonAction, Supplier<Collection<? extends DataElement>> fetching){
        Collection<? extends DataElement> dataElements = fetching.get();
        Presenter[] presenter = transformDataElementsToPresenters(dataElements);
        elementHandler.setScrollPane_Content(presenter);
        elementHandler.setOnClickAction(buttonAction);
    }
    
    public static void process(ElementHandler elementHandler, Runnable buttonAction, Supplier<List<? extends DataElement>> fetching1, Supplier<List<? extends DataElement>> fetching2){
        List<? extends DataElement> dataElements1 = fetching1.get();
        List<? extends DataElement> dataElements2 = fetching2.get();
        Presenter[] presenter = transformDataElementsToPresenters(dataElements1, dataElements2);
        elementHandler.setScrollPane_Content(presenter);
        elementHandler.setOnClickAction(buttonAction);
    }
    
    public static Collection<Requirement> castToRequirement(Collection<DataElement> dataElement){
        return (Collection)dataElement;
    }
}
