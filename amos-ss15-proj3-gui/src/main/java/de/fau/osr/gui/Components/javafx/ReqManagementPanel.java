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
