package de.fau.osr.gui.Components.javafx;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.*;
import de.fau.osr.core.db.dao.impl.RequirementDaoImplementation;
import de.fau.osr.core.db.domain.Requirement;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for requirement management tab
 * Created by Dmitry Gorelenkov on 10.06.2015.
 */


public class ReqManagementController implements Initializable {
    @FXML
    private Button btnSave;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ListView<String> lstReqs;

    @FXML
    private TextField txtTitle;

    private Tracker tracker;
    private RequirementDaoImplementation reqDao;

    public ReqManagementController() {

    }

    /**
     * on click on save button. Save current entered data to the database
     * @param actionEvent
     */
    public void saveReq(ActionEvent actionEvent) {
        Requirement req = reqDao.getRequirementById(lstReqs.getSelectionModel().getSelectedItem());
        req.setTitle(txtTitle.getText());
        req.setDescription(txtDescription.getText());
        if (!reqDao.persist(DBOperation.UPDATE, req)){
            animateButton("Failed", btnSave);
        }

        animateButton("OK", btnSave);
    }

    private void animateButton(String text, Button btn){
        String textb4 = btn.getText();
        btn.setText(text);
        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(100), btn);

        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(20);
        scaleTransition.setToY(20);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
        scaleTransition.setOnFinished(event -> btn.setText(textb4));

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collection<String> allReqs;
        reqDao = new RequirementDaoImplementation();

        try {
            allReqs = getTracker().getAllRequirements();

            ObservableList<String> items = FXCollections.observableArrayList(allReqs);

            lstReqs.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }

        lstReqs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Requirement req = reqDao.getRequirementById(newValue);

                txtTitle.setText(req.getTitle());
                txtDescription.setText(req.getDescription());
            }
        });
    }

    private Tracker getTracker() {
        if (tracker != null) return tracker;

        try {
            File repoFile = new File(AppProperties.GetValue("DefaultRepoPath"));
            GitVcsClient vcs = new GitVcsClient(repoFile.toString());

            Pattern reqPattern = Pattern.compile(AppProperties.GetValue("RequirementPattern"));

            CSVFileDataSource csvDs = new CSVFileDataSource(new File(repoFile.getParentFile(), AppProperties.GetValue("DefaultPathToCSVFile")));
            VCSDataSource vcsDs = new VCSDataSource(vcs, new CommitMessageParser(reqPattern));
            DBDataSource dbDs = new DBDataSource();
            CompositeDataSource ds = new CompositeDataSource(dbDs, csvDs, vcsDs);


            tracker = new Tracker(vcs, ds, repoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tracker;
    }

}