package de.fau.osr.gui.Presenter;

import java.awt.Color;
import java.io.File;

import de.fau.osr.core.vcs.base.CommitFile;

public class Presenter_CommitFile {
    private CommitFile commitFile;

    public CommitFile getCommitFile() {
        return commitFile;
    }

    public void setCommitFile(CommitFile commitFile) {
        this.commitFile = commitFile;
    }
    
    public Presenter_CommitFile(CommitFile commitFile){
        setCommitFile(commitFile);
    }
    
    @Override
    public String toString(){
        return String.format("%s - %-3.1f", commitFile.newPath.toPath().getFileName().toString(), commitFile.impact);
    }
    
    public boolean isAvailable(){
        File f = new File(commitFile.newPath.getPath());
        return f.exists();
    }
    
    public Color getColor(){
        switch (commitFile.commitState) {
        case MODIFIED:
            return Color.YELLOW;
        case ADDED:
            return Color.GREEN;
        case DELETED:
            return Color.RED;
        default:
            return Color.WHITE;
        }
    }
    
}
