package de.fau.osr.gui.util;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.nio.file.Path;

/**
 * Some useful functions for UI
 * Created by Dmitry Gorelenkov on 24.05.2015.
 */
public class UiTools {

    /**
     * Recursive adds an object to <tt>DefaultMutableTreeNode</tt> by specified <tt>path</tt>
     * <br/>
     * if some subdirectory of <tt>path</tt> does not exists it will be created
     * <br/>
     * the last part of path (usually filename) will be appended as a leaf, and bounded with <tt>object</tt>
     * @param root root node
     * @param path path will be created/used to insert the object, last part (filename) inclusive
     * @param object element will be bounded with the last <tt>path</tt> part (filename)
     */
    public static void AddToTreeByPath(DefaultMutableTreeNode root, Path path, Object object) {
        //if only one part in the path, so its the filename, add the object directly
        if (path.getNameCount() == 1){
            DefaultMutableTreeNode commitFileLeaf = new DefaultMutableTreeNode(object);
            commitFileLeaf.setAllowsChildren(false); //make it leaf
            root.add(commitFileLeaf);
            return;
        }

        //need to create/find folder, and add the rest
        Path firstPathPart = path.subpath(0, 1);
        //subpath uses only dirs, last dir index: path.getNameCount()-2
        Path restPath = path.subpath(1, path.getNameCount());

        //test if the node has already the firstPathPart dir
        DefaultMutableTreeNode dir = null;
        Path currentDirPath;
        Object currentChild;
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode currentDir = (DefaultMutableTreeNode)root.getChildAt(i);
            currentChild = currentDir.getUserObject();
            //if the content is not a path, ignore it
            if (!(currentChild instanceof Path)){
                continue;
            }

            currentDirPath = (Path)currentChild;
            //if the paths are the same, use this node
            if (currentDirPath.equals(firstPathPart)){
                dir = currentDir;
                break;
            }
        }

        //if nothing found, create new node
        if (dir == null) {
            dir = new DefaultMutableTreeNode(firstPathPart);
            root.add(dir);
        }

        //recursively add the node
        AddToTreeByPath(dir, restPath, object);
    }
    
    /**
     * This method is a utility method to open a File chooser.
     * @param filename
     * @return
     */
    public static File chooseFile(String filename) {
        JFileChooser fileChooser = new JFileChooser();
          fileChooser.setDialogTitle("Spice Traceability -Save As");
          fileChooser.setSelectedFile(new File(filename));

          int userSelection = fileChooser.showSaveDialog(null);

          if (userSelection == JFileChooser.APPROVE_OPTION) {
              return fileChooser.getSelectedFile();
          }
          return null;
    }

    public static void dialogStatusMessage(Boolean result, String text){
        String message;
        if(result)
            message= text + " saved successfully";
        else
            message= text + " save failed!";
        JOptionPane.showMessageDialog(null, message);
    }
}

