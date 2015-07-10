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
package de.fau.osr.gui.View;

import de.fau.osr.gui.View.ElementHandler.ElementHandler;

public class Cleaner {
    private GuiViewElementHandler elementhandler;
    
    public Cleaner(GuiViewElementHandler elementhandler){
        this.elementhandler = elementhandler;
    }
    
    /**
     * Clearing all scrollpanes. Containing the Code_ScrollPane. And clearing all Textfields.
     * Deactivating Linkage_Button
     * Color is set to the initial white.
     */
    public void clearAll(){
        for(ElementHandler handler: elementhandler.getElementHandlers()){
            handler.clear();
        }
    }

    public void clearFiles() {
        elementhandler.getPathDE_ElementHandler().clear();
    }


    public void clearRequirements() {
        elementhandler.getRequirement_ElementHandler().clear();
    }

    public void clearCommits() {
        elementhandler.getCommit_ElementHandler().clear();
    }

    public void clearCode() {
        elementhandler.getCode_ElementHandler().clear();
        elementhandler.getImpact_ElementHandler().clear();
    }

}
