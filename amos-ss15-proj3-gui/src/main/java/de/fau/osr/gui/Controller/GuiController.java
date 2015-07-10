/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.Controller;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JList;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.CompositeDataSource;
import de.fau.osr.core.db.DBDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.db.HibernateUtil;
import de.fau.osr.core.db.VCSDataSource;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.gui.Authentication.Login;
import de.fau.osr.gui.Components.PathDEsJTree;
import de.fau.osr.gui.Model.Collection_Model_Impl;
import de.fau.osr.gui.Model.I_Collection_Model;
import de.fau.osr.gui.Model.TrackerAdapter;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.Configuration;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.PathDE;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.View.Cleaner;
import de.fau.osr.gui.View.GuiViewElementHandler;
import de.fau.osr.gui.View.PopupManager;
import de.fau.osr.gui.View.TracabilityMatrix_View;
import de.fau.osr.gui.View.TraceabilityMatrixByImpactViewHandlerPanel;
import de.fau.osr.gui.View.ElementHandler.ElementHandler;
import de.fau.osr.gui.View.ElementHandler.Linkage_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.Requirement_Detail_ElementHandler;
import de.fau.osr.gui.View.ElementHandler.Requirement_ElementHandler;
import de.fau.osr.gui.View.Presenter.Presenter;
import de.fau.osr.gui.View.Presenter.Presenter_Commit;
import de.fau.osr.gui.View.Presenter.Presenter_Requirement;
import de.fau.osr.gui.util.filtering.FilterByExactString;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;


/**
 * Using a MVC-Pattern for the GUI. The Controller class fetches the data from
 * the Modell and passes it to the View. Additional it eventually sets Events to
 * the GUI-Elements, so that an action can call the appropriate controller
 * method, within which the data is fetched again ...
 */
public class GuiController {
    // Whether the user gets another chance to input correct data
    enum RetryStatus {
        Retry, Exit, Cancel
    }

    private static final int MAX_RETRIES = 3;
    private RetryStatus Status;

    GuiViewElementHandler elementHandler;
    Cleaner cleaner;
    PopupManager popupManager = new PopupManager();
    TracabilityMatrix_View tracability_view = new TracabilityMatrix_View();

    I_Collection_Model i_Collection_Model;

    JList<Presenter> requirements_JList;
    JList<Presenter> commitMessages_JList;

    /**
     * <tt>PathDE</tt>'s in a tree component
     */
    PathDEsJTree PathDEsJTree;

    JList<Presenter> requirements2Lines_JList;
    JList<Presenter> code_JList;

    // filtering/finding a specific reqiurementID
    Predicate<Requirement> requirementIDFiltering;
    private Configuration configuration = null;

    /**
     * Called to start the initially starts the program. Setting up GUI and
     * displaying the initial data: All Requirements from external
     * tool/DB(jira...)
     */
    public GuiController() {
        Status = RetryStatus.Retry;

        EventQueue.invokeLater(new Runnable() {

            public void run() {

               /* for (int i = 0; true; i++) {
                    if(!popupManager.Authentication()){
                        Status = RetryStatus.Exit;
                        handleError();
                    }

                    File repoFile = null;
                    try {
                        repoFile = popupManager.Repo_OpeningDialog();
                    } catch (IOException e1) {
                        System.exit(0);
                    }

                    try {
                        Pattern reqPatternString = Pattern.compile(popupManager
                                .Pattern_OpeningDialog(AppProperties
                                        .GetValue("RequirementPattern")));
                        i_Collection_Model = reInitModel(null, null, repoFile,
                                reqPatternString);
                        break;
                    } catch (PatternSyntaxException | IOException e) {
                        if (i >= MAX_RETRIES) {
                            Status = RetryStatus.Exit;
                        }
                        popupManager.showErrorDialog(e.getMessage());
                        handleError();
                    } catch (Exception e) {
                        Status = RetryStatus.Exit;
                        popupManager.showErrorDialog("Fatal Error:\n"
                                + e.getMessage());
                        handleError();
                    }
                }*/

                elementHandler = new GuiViewElementHandler(GuiController.this);
                cleaner = new Cleaner(elementHandler);
                
            }
        });
    }
    
    public I_Collection_Model getI_Collection_Model(){
        return this.i_Collection_Model;
    }

    public Predicate<Requirement> getRequirementIDFiltering() {
        return requirementIDFiltering;
    }

    public void setRequirementIDFiltering(
            FilterByExactString requirementIDFiltering) {
        this.requirementIDFiltering = requirementIDFiltering;
    }

    /**
     * Navigation: ->Requirements Clear: All Setting: Requirements Using:
     * getRequirements
     */
    public void requirementsFromDB(){
        cleaner.clearAll();

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            try{
                return i_Collection_Model.getRequirements(requirementIDFiltering);
            } catch(IOException e){
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getRequirement_ElementHandler();

        Runnable buttonAction = () -> {
            commitsFromRequirement();
            filesFromRequirement();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }
    
    public void requirementsFromDBForRequirementTab(){
        Supplier<Collection<? extends DataElement>> fetching = () -> {
            try{
                return i_Collection_Model.getRequirements(requirementIDFiltering);
            } catch(IOException e){
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        Requirement_Detail_ElementHandler detail_handler = elementHandler.getRequirement_Detail_ElementHandler();
        Requirement_ElementHandler tabAndReqsList_elementHandler = elementHandler.getRequirement_ElementHandlerRequirementTab();

        Runnable buttonAction = () -> {
            Collection<DataElement> dataElements = tabAndReqsList_elementHandler.getSelection(
                    new Visitor_Swing());
            Presenter[] presenters = Transformer.transformDataElementsToPresenters(dataElements);

            detail_handler.setScrollPane_Content(presenters);
        };

        Transformer.process(tabAndReqsList_elementHandler, buttonAction, fetching);

        detail_handler.setListenerOnSaveClick(e1 -> {
                    Collection<DataElement> selectedReqs = tabAndReqsList_elementHandler.getSelection(new Visitor_Swing());
                    if (selectedReqs.size() < 1) {
                        popupManager.showErrorDialog("select a requirement");
                        return;
                    }

                    Requirement lastSelected = (Requirement) Iterables.getLast(selectedReqs);

                    String id = lastSelected.getID();
                    String title = detail_handler.getTitle().getText();
                    String description = detail_handler.getDescription().getText();

                    String resultMsg = "Saved!";
                    if (!getI_Collection_Model().updateRequirement(id, title, description)) {
                        resultMsg = "Failed!";
                    }
                    popupManager.showInformationDialog(resultMsg);
                }


        );
    }
    
    public void requirementsFromDBForManagementTab(){

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            try{
                return i_Collection_Model.getRequirements(requirementIDFiltering);
            } catch(IOException e){
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getRequirement_Handler_ManagementTab();

        Runnable buttonAction = () -> {
            ArrayList<? extends DataElement> dataElements = new ArrayList<DataElement>(specificElementHandler.getSelection(new Visitor_Swing()));
            elementHandler.getLinkage_ElementHandler().setRequirement((Presenter_Requirement) dataElements.get(dataElements.size() - 1).visit(new Visitor_Swing()));
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }
    
    public void commitsFromDBForManagementTab(){
        Supplier<Collection<? extends DataElement>> fetching = () -> {
            return i_Collection_Model.getCommitsFromDB();
        };

        ElementHandler specificElementHandler = elementHandler
                .getCommit_Handler_ManagementTab();

        Runnable buttonAction = () -> {
            ArrayList<? extends DataElement> dataElements = new ArrayList<DataElement>(specificElementHandler.getSelection(new Visitor_Swing()));
            elementHandler.getLinkage_ElementHandler().setCommit((Presenter_Commit) dataElements.get(dataElements.size() - 1).visit(new Visitor_Swing()));
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * initialize linkage management tab
     */
    public void initLinkageManagementTab(){
        requirementsFromDBForManagementTab();
        commitsFromDBForManagementTab();

        Linkage_ElementHandler linkageHandler = elementHandler.getLinkage_ElementHandler();

        linkageHandler.setOnClickAddLinkage(e ->
                        addLinkage(linkageHandler.getRequirement(), linkageHandler.getCommit())
        );
    }

    /**
     * Navigation: ->Requirements->Commit Clear: Files/Code/ImpactPercentage
     * Setting: Commits Using: getCommitsByRequirement
     */
    void commitsFromRequirement(){
        cleaner.clearFiles();
        cleaner.clearCode();

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            Collection<DataElement> dataElements = elementHandler
                    .getRequirement_ElementHandler().getSelection(
                            new Visitor_Swing());
            try {
                return i_Collection_Model.getCommitsByRequirement((Collection) dataElements);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getCommit_ElementHandler();

        Runnable buttonAction = () -> {
            filesByCommitAndRequirement();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * Navigation: ->Requirements->File Clear: Code/ImpactPercentage Setting:
     * Files Using: getFilesFromRequirement
     */
    void filesFromRequirement(){
        cleaner.clearCode();

        Supplier<List<? extends DataElement>> fetching1 = () -> {
            Collection<DataElement> dataElements = elementHandler
                    .getRequirement_ElementHandler().getSelection(
                            new Visitor_Swing());
            try {
                return i_Collection_Model.getFilesByRequirement(
                        (Collection) dataElements);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };
        
        Supplier<List<? extends DataElement>> fetching2 = () -> {
            Collection<DataElement> reqs = elementHandler
                    .getRequirement_ElementHandler().getSelection(
                            new Visitor_Swing());
            try {
                // !!! getFilesByRequirement is called 2nd time :-(
                List<? extends DataElement> paths =  i_Collection_Model.getFilesByRequirement(
                        (Collection) reqs);
                return i_Collection_Model.getImpactForRequirementAndFile((Collection) reqs, (List) paths);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getPathDE_ElementHandler();

        Runnable buttonAction = () -> {
            this.commitsFromRequirementAndFile();
            this.codeFromFile();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching1, fetching2);
    }

    /**
     * Navigation: ->Requirements->File->Commit ->File->Requirement->Commit
     * Clear: Setting: Commits Using: commitsFromRequirementAndFile
     */
    void commitsFromRequirementAndFile() {

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            Collection<DataElement> requirements = elementHandler
                    .getRequirement_ElementHandler().getSelection(
                            new Visitor_Swing());
            Collection<DataElement> files = elementHandler
                    .getPathDE_ElementHandler().getSelection(
                            new Visitor_Swing());
            try {
                return i_Collection_Model.getCommitsByRequirementAndFile(
                        (Collection) requirements, (Collection) files);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal storing Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getCommit_ElementHandler();

        Runnable buttonAction = () -> {
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * Navigation: ->Files->Code Clear: ImpactPercentage Setting: Code Using:
     * getChangeDataFromFileIndex
     *
     * Just displays latest file content.
     */
    void codeFromFile() {
        cleaner.clearCode();

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            
            Collection<DataElement> files = elementHandler
                    .getPathDE_ElementHandler().getSelection(
                            new Visitor_Swing());
            try {
                return i_Collection_Model.getAnnotatedLinesByFile((Collection) files);
            } catch (IOException | GitAPIException e) {
                popupManager.showErrorDialog("Internal storing Error" + e);
                return new ArrayList<DataElement>();
            }
        };
        
        Collection<DataElement> requirements = elementHandler
                .getRequirement_ElementHandler().getSelection(
                        new Visitor_Swing());
        
        Transformer.setVisitor(new Visitor_Swing(requirements));

        ElementHandler specificElementHandler = elementHandler
                .getCode_ElementHandler();

        Runnable buttonAction = () -> {
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);

        specificElementHandler = elementHandler.getImpact_ElementHandler();
        Transformer.setVisitor(new Visitor_Swing());

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }
//
//    /**
//     * Navigation: ->File->Code->Requirements Clear: Commits Setting:
//     * Requirements Using: getRequirementsForBlame
//     * 
//     * @param file
//     * @param codeIndex
//     */
//    void requirementsFromCode() {
//        cleaner.clearCommits();
//
//        Supplier<Collection<? extends DataElement>> fetching = () -> {
//            try {
//                return i_Collection_Model.AnnotatedLinesFromFile(files);
//            } catch (IOException | GitAPIException e) {
//                popupManager.showErrorDialog("Internal storing Error" + e);
//                return new ArrayList<DataElement>();
//            }
//        };
//
//        ElementHandler specificElementHandler = elementHandler
//                .getRequirement_ElementHandler();
//
//        Runnable buttonAction = () -> {
//        };
//
//        Transformer.process(specificElementHandler, buttonAction, fetching);
//    }
//
    /**
     * Navigation: ->Files Clear: All Setting: Files Using: getAllPathDEs
     */
    public void filesFromDB() {

        cleaner.clearAll();

        Supplier<Collection<? extends DataElement>> fetching = i_Collection_Model::getFilePaths;

        ElementHandler specificElementHandler = elementHandler
                .getPathDE_ElementHandler();

        Runnable buttonAction = () -> {
            requirementsFromFile();
            commitsFromFile();
            codeFromFile();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * Navigation: ->File->Requirement Clear: Setting: Requirement Using:
     * getRequirementsByFile
     */
    void requirementsFromFile() {

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            Collection<DataElement> files = elementHandler.getPathDE_ElementHandler().getSelection(new Visitor_Swing());
            //cast collection of DataElements to PathDEs
            ArrayList<PathDE> PathDEs = new ArrayList<>();
            for (DataElement de : files) {
                PathDEs.add((PathDE) de);
            }

            try{
                return i_Collection_Model.getRequirementsByFile(PathDEs);
            } catch (IOException e) {
                popupManager.showErrorDialog("File not found!");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getRequirement_ElementHandler();

        Runnable buttonAction = () -> {
            commitsFromRequirementAndFile();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * Navigation: ->File->Commit Clear: Setting: Commits Using:
     * getCommitsByFile
     */
    void commitsFromFile() {
        
        Supplier<Collection<? extends DataElement>> fetching = () -> {
            Collection<DataElement> files = elementHandler.getPathDE_ElementHandler().getSelection(new Visitor_Swing());
            return i_Collection_Model.getCommitsByFile((Collection) files);
        };

        ElementHandler specificElementHandler = elementHandler
                .getCommit_ElementHandler();

        Runnable buttonAction = () -> {
            requirementsFromFileAndCommit();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * Navigation: ->Files->Commit->Requirement Clear: Setting: Requirement
     * Using: getRequirementsFromFileAndCommit
     */
    void requirementsFromFileAndCommit() {

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            Collection<DataElement> files = elementHandler.getPathDE_ElementHandler().getSelection(new Visitor_Swing());
            Collection<DataElement> commits = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing());
            try{
                return i_Collection_Model.getRequirementsByFileAndCommit((Collection) commits, (Collection) files);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal storing Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getRequirement_ElementHandler();

        Runnable buttonAction = () -> {};

        Transformer.process(specificElementHandler, buttonAction, fetching);

    }

    /**
     * Navigation: ->Commits Clear: All Setting: Commits Using: getCommits
     */
    @Deprecated
    public void commitsFromDB() {
        cleaner.clearAll();
        
        Supplier<Collection<? extends DataElement>> fetching = () -> {
            return i_Collection_Model.getCommitsFromDB();
        };

        ElementHandler specificElementHandler = elementHandler
                .getCommit_ElementHandler();

        Runnable buttonAction = () -> {
            filesFromCommit();
            requirementsFromCommit();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * Navigation: ->Commit->Files Clear: Code/ImpactPercentage Setting: Files
     * Using: getFilesByCommit
     */
    @Deprecated
    void filesFromCommit() {
        cleaner.clearCode();

        Supplier<List<? extends DataElement>> fetching1 = () -> {
            Collection<DataElement> commits = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing());
            try{
                return i_Collection_Model.getFilesByCommit((Collection) commits);
            } catch (FileNotFoundException e) {
                popupManager.showErrorDialog("Internal storing Error");
                return new ArrayList<DataElement>();
            }
        };
        
        Supplier<List<? extends DataElement>> fetching2 = () -> {
            Collection<DataElement> dataElements = elementHandler
                    .getRequirement_ElementHandler().getSelection(
                            new Visitor_Swing());
            Collection<DataElement> commits = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing());
            try{
                List<? extends DataElement> paths =  i_Collection_Model.getFilesByCommit((Collection) commits);
                return i_Collection_Model.getImpactForRequirementAndFile((Collection) dataElements, (List) paths);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getPathDE_ElementHandler();

        Runnable buttonAction = () -> {
            codeFromFile();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching1, fetching2);
    }

    /**
     * Navigation: Req->Commit-> Clear: Code/ImpactPercentage Setting: Files
     * Using: getFilesByCommit
     */
    void filesByCommitAndRequirement() {
        cleaner.clearCode();

        Supplier<List<? extends DataElement>> fetching1 = () -> {
            Collection<DataElement> commits = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing());
            Collection<DataElement> reqs = elementHandler.getRequirement_ElementHandler().getSelection(new Visitor_Swing());
            try{
                return i_Collection_Model.getFilesByCommitAndRequirement((Collection) commits, (Collection) reqs);
            } catch (FileNotFoundException e) {
                popupManager.showErrorDialog("Internal storing Error");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<DataElement>();
        };

        Supplier<List<? extends DataElement>> fetching2 = () -> {
            Collection<DataElement> reqs = elementHandler
                    .getRequirement_ElementHandler().getSelection(
                            new Visitor_Swing());
            Collection<DataElement> commits = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing());
            try{
                List<? extends DataElement> paths = i_Collection_Model.getFilesByCommitAndRequirement((Collection) commits, (Collection) reqs);
                return i_Collection_Model.getImpactForRequirementAndFile((Collection) reqs, (List) paths);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal Storage Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getPathDE_ElementHandler();

        Runnable buttonAction = () -> {
            codeFromFile();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching1, fetching2);
    }

    /**
     * Navigation: ->Commits->Requirements Clear: Setting: Requirements Using:
     * getRequirementsByCommit
     */
    void requirementsFromCommit() {

        Supplier<Collection<? extends DataElement>> fetching = () -> {
            Collection<DataElement> commits = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing());
            try{
                return i_Collection_Model.getRequirementsByCommit((Collection) commits);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal storing Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getRequirement_ElementHandler();

        Runnable buttonAction = () -> {
            codeFromFile();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);
    }

    /**
     * For button AddLinkage
     */
    public void requirementsAndCommitsFromDB() {
        cleaner.clearAll();
        
        Supplier<Collection<? extends DataElement>> fetching = () -> {
            try{
                return i_Collection_Model.getRequirements(requirementIDFiltering);
            } catch (IOException e) {
                popupManager.showErrorDialog("Internal storing Error");
                return new ArrayList<DataElement>();
            }
        };

        ElementHandler specificElementHandler = elementHandler
                .getRequirement_ElementHandler();

        Runnable buttonAction = () -> {
            RequirementToLinkage();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);

        fetching = () -> {
            return i_Collection_Model.getCommitsFromDB();
        };

        specificElementHandler = elementHandler
                .getCommit_ElementHandler();

        buttonAction = () -> {
            CommitToLinkage();
        };

        Transformer.process(specificElementHandler, buttonAction, fetching);

        elementHandler.getLinkage_ElementHandler().switchButtonAction();
        elementHandler.getLinkage_ElementHandler().setDataLayerChanged(true);
    }

    void RequirementToLinkage() {
        DataElement requirement = elementHandler.getRequirement_ElementHandler().getSelection(new Visitor_Swing()).iterator().next();
        elementHandler.getLinkage_ElementHandler().setRequirement((Presenter_Requirement) requirement.visit(new Visitor_Swing()));
    }

    void CommitToLinkage() {
        DataElement commit = elementHandler.getCommit_ElementHandler().getSelection(new Visitor_Swing()).iterator().next();
        elementHandler.getLinkage_ElementHandler().setCommit((Presenter_Commit) commit.visit(new Visitor_Swing()));
    }

    /**
     * For now only terminated the application if the user retried some input to
     * often. Later on should handle all actions that have to be completed
     * before exit.
     */
    void handleError() {
        if (Status == RetryStatus.Exit) {
            System.exit(1);
        }
    }

    /**
     * For reconfiguring the repository to a new path while the application is
     * running Once this method is successful, the application refers to the new
     * repository
     */

    void reConfigureRepository() throws IOException {
        I_Collection_Model guiModelTrial = i_Collection_Model;
        for (int i = 0; i <= MAX_RETRIES; i++) {
            if (i == MAX_RETRIES) {
                Status = RetryStatus.Cancel;
                popupManager.showErrorDialog("Maximum retries exceeded");
                return;
            }
            File repoFile = null;
            try {
                repoFile = popupManager.Repo_OpeningDialog();
            } catch (IOException e1) {

            }
            if (repoFile == null) {
                Status = RetryStatus.Cancel;
                return;
            }
            try {
                guiModelTrial = reInitModel(null, null, repoFile,
                        i_Collection_Model.getCurrentRequirementPattern(),false);
                popupManager.showInformationDialog("Repository Path modified to "
                        + repoFile.getPath());
                break;
            } catch (IOException | RuntimeException e) {

                popupManager.showErrorDialog(e.getMessage());
                handleError();
            }
        }
        i_Collection_Model = guiModelTrial;
        requirementsFromDB();
    }

    /**
     * For reconfiguring the requirement pattern to a new pattern while the
     * application is running Once this method is successful, the application
     * refers to the new requirement pattern
     */
    void reConfigureRequirementPattern() throws IOException {
        I_Collection_Model guiModelTrial = i_Collection_Model;
        for (int i = 0; true; i++) {
            if (i == MAX_RETRIES) {
                Status = RetryStatus.Cancel;
                popupManager.showErrorDialog("Maximum retries exceeded");
                return;
            }
            Pattern reqPattern;
            try {
                reqPattern = Pattern.compile(popupManager
                        .Pattern_OpeningDialog(i_Collection_Model
                                .getCurrentRequirementPattern().toString()));
            } catch (Exception e) {
                // todo error message about bad pattern
                Status = RetryStatus.Cancel;
                return;
            }

            try {
                guiModelTrial = reInitModel(null, null,
                        new File(i_Collection_Model.getCurrentRepositoryPath()),
                        reqPattern,false);
                popupManager.showInformationDialog("Requirement Pattern modified to "
                        + reqPattern);
                break;
            } catch (RuntimeException | IOException e) {
                popupManager.showErrorDialog(e.getMessage());
                handleError();
            }
        }
        i_Collection_Model = guiModelTrial;
        requirementsFromDB();

    }

    /**
     * method to divert configuration calls
     */
    public void reConfigure() throws IOException {
        switch (popupManager.Configure_OptionDialog()) {
        // these values have to be replaced by some enums
        case 0:
            reConfigureRepository();
            break;
        case 1:
            reConfigureRequirementPattern();
            break;
        }

    }

    /**
     * initialize model again. If any arg is null, the default value will be
     * used
     * 
     * @param vcs
     *            vcs client
     * @param ds
     *            data source
     * @param repoFile
     *            path to git repo
     * @param reqPattern
     *            pattern to parse req id from commit messages
     * @return model to use
     */
    private Collection_Model_Impl reInitModel(VcsClient vcs, DataSource ds,
            File repoFile, Pattern reqPattern, boolean isIndexEnabled) throws IOException {

        if (repoFile == null) {
            repoFile = new File(AppProperties.GetValue("DefaultRepoPath"));
        }

        if (vcs == null) {
            vcs = new GitVcsClient(repoFile.toString());
        }

        if (reqPattern == null) {
            reqPattern = Pattern.compile(AppProperties
                    .GetValue("RequirementPattern"));
        }

        if (ds == null) {
            CSVFileDataSource csvDs = new CSVFileDataSource(new File(
                    repoFile.getParentFile(),
                    AppProperties.GetValue("DefaultPathToCSVFile")));
            VCSDataSource vcsDs = new VCSDataSource(vcs,
                    new CommitMessageParser(reqPattern));
            DBDataSource dbDs = new DBDataSource();
            ds = new CompositeDataSource(dbDs, csvDs, vcsDs);
        }
        Collection_Model_Impl model;
        if(isIndexEnabled)
            model = new Collection_Model_Impl(new TrackerAdapter(new Tracker(vcs, ds, repoFile),true));
        else
            model = new Collection_Model_Impl(new TrackerAdapter(new Tracker(vcs, ds, repoFile),false));
        
        model.setCurrentRequirementPattern(reqPattern);
        return model;
    }

    public void addLinkage(Requirement requirement, Commit commit) {
        try {
            i_Collection_Model.addRequirementCommitLinkage(requirement, commit);
            popupManager.showInformationDialog("Successfully Added!");
        } catch (Exception e) {
            popupManager.showErrorDialog(e.getMessage());
            return;
        } finally {
            cleaner.clearAll();
        }
    }

    void getTraceabilityMatrix() {
        try {
            tracability_view.showTraceabilityMatrix(i_Collection_Model
                    .getRequirementsTraceability());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getTraceabilityMatrixByImpact() {
        try {
        	RequirementsTraceabilityMatrixByImpact tr = i_Collection_Model.getRequirementsTraceabilityByImpact();
            tracability_view.showTraceabilityMatrixByImpactProgressBar(tr);
            tr.Process();
            tracability_view.showTraceabilityMatrixByImpact(tr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**This method enables showing of traceability matrix in the tabbed view
     * @param traceabilityMatrixByImpactViewHandlerPanel
     */
    public void showTraceabilityMatrixByImpactInTabbedView(TraceabilityMatrixByImpactViewHandlerPanel traceabilityMatrixByImpactViewHandlerPanel) {
        try {
            RequirementsTraceabilityMatrixByImpact tr = i_Collection_Model.getRequirementsTraceabilityByImpact();
            tracability_view.showTraceabilityMatrixByImpactProgressBar(tr);
            traceabilityMatrixByImpactViewHandlerPanel.setInternalGenerationEnable(false);
            traceabilityMatrixByImpactViewHandlerPanel.setExportEnable(false);
            //traceabilityMatrixByImpactViewHandlerPanel.eraseTable();
            tr.Process();
            tracability_view.showTraceabilityMatrixByImpactInTabbedView(tr, traceabilityMatrixByImpactViewHandlerPanel);
            traceabilityMatrixByImpactViewHandlerPanel.setInternalGenerationEnable(true);
            traceabilityMatrixByImpactViewHandlerPanel.setExportEnable(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            HibernateUtil.shutdown();
        }
        finally {
            super.finalize();
        }
    }
    
    public boolean configureApplication(Configuration configuration ){
        try {
            HibernateUtil.shutdown();

            if(!Login.authenticate(configuration.getDbUsername(), configuration.getDbPassword())) {
                popupManager.showErrorDialog("Cannot connect to database.Please check the database credentials");
            }

            File repoFile = new File(configuration.getRepoPath());
            Pattern reqPatternString = Pattern.compile(configuration.getReqPattern());
            i_Collection_Model = reInitModel(null, null, repoFile,reqPatternString,configuration.isEnableIndex());
            popupManager.showInformationDialog("Configuration successfull");
            
            cleaner = new Cleaner(elementHandler);
            
            elementHandler.doInitialization();
    
            requirementsFromDB();
            requirementsFromDBForRequirementTab();
            initLinkageManagementTab();
            this.configuration = configuration;
        }catch (PatternSyntaxException  | IOException e){
            e.printStackTrace();
            popupManager.showErrorDialog("Some problem in application configuration");
            return false;
        }catch(RuntimeException e){
            popupManager.showErrorDialog("Cannot connect to database.Please check the database credentials");
            return false;
        }
       
       
        return true;
    }
    
    public void refresh() {
    	try {
    		File repoFile = new File(configuration.getRepoPath());
    		Pattern reqPatternString = Pattern.compile(configuration.getReqPattern());
    		i_Collection_Model = reInitModel(null, null, repoFile,reqPatternString,configuration.isEnableIndex());
		} catch (IOException e) {
			popupManager.showErrorDialog("Could not refresh linkage: " + e.getMessage());
			return;
		}
    	elementHandler.doInitialization();
    	requirementsFromDB();
        requirementsFromDBForRequirementTab();
        initLinkageManagementTab();
    }
}
