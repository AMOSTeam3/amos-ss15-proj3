package de.fau.osr.gui.Components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Taleh Didover
 * (thankfully reused from https://gist.github.com/pocket7878/7673893)
 */
public class MultiSplitPane extends JPanel {
    private int orientation;
    private boolean isResizeable;
    private boolean needSplit;
    private boolean wasSplit;
    private Component firstChild;
    private MultiSplitPane private_second_child;
    private Collection<Component> components;

    /**
     *
     * @param newOrientation vertical or horizontal (see JSplitPane)
     */
    public MultiSplitPane(int newOrientation) {
        this(newOrientation, true);
    }

    /**
     *
     * @param newOrientation vertical or horizontal (see JSplitPane)
     * @param isResizeable  no SplitPane divider.
     */
    public MultiSplitPane(int newOrientation, boolean isResizeable) {
        this.orientation = newOrientation;
        this.isResizeable = isResizeable;
        this.needSplit = false;
        this.wasSplit = false;
        this.components = new ArrayList<>();
    }


    public MultiSplitPane addComponent(Component comp) {
        this.components.add(comp);

        if(this.wasSplit) {
            private_second_child.addComponent(comp);
        } else if(this.needSplit) {
            //Remove first child.
            this.remove(this.firstChild);
            //Setup SplitPane
            this.setLayout(new BorderLayout());
            JSplitPane splitPane = new JSplitPane(this.orientation);
            this.add(splitPane, BorderLayout.CENTER);
            splitPane.setLeftComponent(this.firstChild);
            this.private_second_child = new MultiSplitPane(this.orientation, this.isResizeable);
            this.private_second_child.addComponent(comp);
            splitPane.setRightComponent(this.private_second_child);
            this.wasSplit = true;

            splitPane.setEnabled(isResizeable);
            if (!isResizeable)
                splitPane.setDividerSize(0);
            else
                splitPane.setDividerSize(5);

        } else {
            //First child
            this.firstChild = comp;
            this.setLayout(new BorderLayout());
            this.add(comp, BorderLayout.CENTER);
            //Set flag.
            this.needSplit = true;
        }
        //Validate to redisplay components.
        this.validate();


        // Following the *Builder pattern*
        return this;
    }

    public Collection<Component> getComps() {
        return this.components;
    }
}


