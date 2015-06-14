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
        elementhandler.getCommitFile_ElementHandler().clear();
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
