package de.fau.osr.gui.Components.Renderer;

import de.fau.osr.core.vcs.base.CommitFile;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Displays each CommitFile by file name, cell highlighting and impact percentage
 */
public class CommitFile_ImpactTreeFilenameRenderer extends CommitFile_SimpleTreeFilenameRenderer {


    @Override
    protected void changeView(DefaultTreeCellRenderer element, CommitFile commitFile) {

        switch(commitFile.commitState){
            case MODIFIED:
                element.setBackgroundNonSelectionColor(Color.YELLOW);
                break;
            case ADDED:
                element.setBackgroundNonSelectionColor(Color.GREEN);
                break;
            case DELETED:
                element.setBackgroundNonSelectionColor(Color.RED);
                break;
            default:
                element.setBackgroundNonSelectionColor(Color.WHITE);
                break;
        }

        element.setText(String.format("%s - %f", commitFile.newPath.toPath().getFileName().toString(), commitFile.impact));
    }
}
