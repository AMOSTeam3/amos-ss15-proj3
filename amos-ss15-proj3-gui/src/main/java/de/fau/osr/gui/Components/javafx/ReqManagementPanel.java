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
    }

    //need this method to be able to initialize the content later,
    //because DB connection setup comes later than gui setup.
    public void initialize(ReqManagementController controller) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = createScene(controller);
                thisPanel.setScene(scene);
                thisPanel.setVisible(true);
            }
        });
    }

    private Scene createScene(ReqManagementController controller) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ReqManagement.fxml"));
            fxmlLoader.setController(controller);
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Scene(root);
    }
}
