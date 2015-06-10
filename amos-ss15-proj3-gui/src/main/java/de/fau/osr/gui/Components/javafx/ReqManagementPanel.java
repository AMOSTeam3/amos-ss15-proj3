package de.fau.osr.gui.Components.javafx;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Panel to manage requirements, in javaFX
 * Created by Dmitry Gorelenkov on 10.06.2015.
 */
public class ReqManagementPanel extends JFXPanel {
    final ReqManagementPanel thisPanel = this;

    public ReqManagementPanel() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = createScene();
                thisPanel.setScene(scene);
                thisPanel.setVisible(true);
            }
        });
    }

    private Scene createScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ReqManagement.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Scene(root);
    }
}
