package de.fau.osr.gui.Components.Renderer;

import de.fau.osr.core.vcs.base.CommitFile;

import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.awt.*;
import java.io.File;

/**
 * Displays each CommitFile by file name, cell highlighting and impact percentage
 */
public class CommitFile_ImpactTreeFilenameRenderer extends CommitFile_SimpleTreeFilenameRenderer {


    @Override
    protected void changeView(DefaultTreeCellRenderer element, CommitFile commitFile) {
        
        File f = new File(commitFile.workingCopy, commitFile.newPath.getPath());
        if (!f.exists()) {
            element.setForeground(UIManager
                    .getColor("Label.disabledForeground"));
            element.setBorder(null);
        } else {
            switch (commitFile.commitState) {
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
        }

        element.setText(String.format("%s - %-3.1f%%", commitFile.newPath.toPath().getFileName().toString(), commitFile.impact));
    }
}
