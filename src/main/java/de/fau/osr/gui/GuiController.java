package de.fau.osr.gui;

import com.google.common.base.Predicate;

import de.fau.osr.core.db.*;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.gui.Components.CommitFilesJTree;
import de.fau.osr.gui.GuiView.HighlightedLine;
import de.fau.osr.gui.GuiViewElementHandler.ButtonState;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;

import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Using a MVC-Pattern for the GUI.
 * The Controller class fetches the data from the Modell and passes it to the View.
 * Additional it eventually sets Events to the GUI-Elements, so that an action can call
 * the appropriate controller method, within which the data is fetched again ...
 */
public class GuiController {
    //Whether the user gets another chance to input correct data
    enum RetryStatus{Retry, Exit, Cancel}
    private static final int MAX_RETRIES = 3;
    private RetryStatus Status;

    GuiView guiView;
    GuiModel guiModel;

    JList<String> requirements_JList;
    JList<String> commitMessages_JList;

    /**
     * <tt>CommitFile</tt>'s in a tree component
     */
    CommitFilesJTree commitFilesJTree;

    JList<String> requirements2Lines_JList;
    JList<HighlightedLine> code_JList;

    // sorting algorithm for commitFilesJTree
    Comparator<CommitFile> commitFileSorting;
    // filtering/finding a specific reqiurementID
    Predicate<String> requirementIDFiltering;


    /**
     * Called to start the initially starts the program. Setting up GUI and displaying the initial data:
     * All Requirements from external tool/DB(jira...)
     */
    public GuiController(){
        Status = RetryStatus.Retry;

        EventQueue.invokeLater(new Runnable() {

            public void run() {

                guiView = new GuiView(GuiController.this);

                for(int i = 0; true; i++){
                    File repoFile = null;
                    try {
                        repoFile = guiView.Repo_OpeningDialog();
                    } catch (IOException e1) {
                        System.exit(0);
                    }

                    try {
                        Pattern reqPatternString = Pattern.compile(guiView.Pattern_OpeningDialog(AppProperties.GetValue("RequirementPattern")));
                        guiModel = reInitModel(null, null, repoFile, reqPatternString);
                        break;
                    } catch (PatternSyntaxException | IOException e) {
                        if(i >= MAX_RETRIES){
                            Status = RetryStatus.Exit;
                        }
                        guiView.showErrorDialog(e.getMessage());
                        handleError();
                    }
                }

                guiView.showView();
                try {
                    requirementsFromDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Comparator<CommitFile> getCommitFileSorting() {
        return commitFileSorting;
    }

    public void setCommitFileSorting(Comparator<CommitFile> commitFileSorting) {
        this.commitFileSorting = commitFileSorting;
        // TODO we need a refresh method
        System.out.format("Commit file sorting selected: %s%n", commitFileSorting);
    }

    public Predicate<String> getRequirementIDFiltering() {
        return requirementIDFiltering;
    }

    public void setRequirementIDFiltering(Predicate<String> requirementIDFiltering) {
        this.requirementIDFiltering = requirementIDFiltering;
    }




    /**
     * Navigation: ->Requirements
     * Clear: All
     * Setting: Requirements
     * Using: getAllRequirements
     */
    void requirementsFromDB() throws IOException {
        guiView.clearAll();

        String[] requirements = guiModel.getAllRequirements(requirementIDFiltering);
        requirements_JList = new JList<String>(requirements);
        guiView.showRequirements(requirements_JList);

        guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.CommitsAndFilesFromRequirement));
    }

    /**
     * Navigation: ->Requirements->Commit
     * Clear: Files/Code/ImpactPercentage
     * Setting: Commits
     * Using: getCommitsFromRequirementID
     */
    void commitsFromRequirement(String requirement) throws IOException {
        guiView.clearFiles();
        guiView.clearCode();
        guiView.clearImpactPercentage();

        commitMessages_JList = new JList<String>(guiModel.getCommitsFromRequirementID(requirement));
        guiView.showCommits(commitMessages_JList);

        guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.FilesFromCommit));
    }

    /**
     * Navigation: ->Requirements->File
     * Clear: Code/ImpactPercentage
     * Setting: Files
     * Using: getFilesFromRequirement
     */
    void filesFromRequirement(String requirementID) throws IOException {
        guiView.clearCode();
        guiView.clearImpactPercentage();

        commitFilesJTree = new CommitFilesJTree(guiModel.getFilesFromRequirement(requirementID, commitFileSorting));
        guiView.showFiles(commitFilesJTree);

        commitFilesJTree.addSelectionListener(new CommitFile_SelectionListener(commitFilesJTree, ()->{
            DefaultMutableTreeNode element = (DefaultMutableTreeNode) commitFilesJTree
                    .getLastSelectedPathComponent();
            CommitFile commitFile = (CommitFile) element.getUserObject();
            this.commitsFromRequirementAndFile(this.requirements_JList.getSelectedValue(), commitFile);
            this.codeFromFile(commitFile, this.requirements_JList.getSelectedValue());
        }));

    }

    /**
     * Navigation: ->Requirements->File->Commit
     *             ->File->Requirement->Commit
     * Clear:
     * Setting: Commits
     * Using: commitsFromRequirementAndFile
     */
    void commitsFromRequirementAndFile(String requirementID, CommitFile file) {
        try {
            commitMessages_JList = new JList<String>(guiModel.commitsFromRequirementAndFile(requirementID, file));
        } catch (IOException e) {
            guiView.showErrorDialog("Internal storing Error");
            return;
        }
        guiView.showCommits(commitMessages_JList);
    }

    /**
     * Navigation: ->Files->Code
     * Clear: ImpactPercentage
     * Setting: Code
     * Using: getChangeDataFromFileIndex
     *
     * Just displays latest file content.
     */
    void codeFromFile(CommitFile file) {
        codeFromFile(file, "");
    }

    /**
     * Navigation: ->Files->Code
     * Clear: ImpactPercentage
     * Setting: Code
     * Using: getChangeDataFromFileIndex
     */
    void codeFromFile(CommitFile file, String requirementID) {
        guiView.clearImpactPercentage();

        try {
            code_JList = new JList<HighlightedLine>(guiModel.getBlame(file, requirementID));
            requirements2Lines_JList = new JList<String>(guiModel.getRequirementsForBlame(file));
        }catch(FileNotFoundException e){
            guiView.showInformationDialog("Can only be displayed if file is up-to-date!");
            return;
        } catch (IOException | GitAPIException e) {
            guiView.showErrorDialog("Internal storing Error" + e);
            return;
        }

        //define fixed cell height to keep each code line and req2line entry at same level
        code_JList.setFixedCellHeight(12);
        requirements2Lines_JList.setFixedCellHeight(12);

        guiView.showCode(code_JList);
        guiView.showRequirementIdsByLines(requirements2Lines_JList);

        guiView.addMouseListener(code_JList, new MouseEvent(this, Action.RequirmentsFromCode));
    }

    /**
     * Navigation: ->File->Code->Requirements
     * Clear: Commits
     * Setting: Requirements
     * Using: getRequirementsForBlame
     * @param file
     * @param codeIndex
     */
    void requirementsFromCode(CommitFile file, int codeIndex){
        guiView.clearCommits();

        try{
            requirements_JList = new JList<String>(guiModel.getRequirementsForBlame(codeIndex, file));
        } catch (IOException | GitAPIException e) {
            guiView.showErrorDialog("Internal storing Error" + e);
            return;
        }

        guiView.showRequirements(requirements_JList);
    }

    /**
     * Navigation: ->Files
     * Clear: All
     * Setting: Files
     * Using: getAllFiles
     */
    void filesFromDB() {
        guiView.clearAll();

        commitFilesJTree = new CommitFilesJTree(guiModel.getAllFiles(getCommitFileSorting()));
        guiView.showFiles(commitFilesJTree);
		commitFilesJTree.addSelectionListener(new CommitFile_SelectionListener(commitFilesJTree, ()->{
		    DefaultMutableTreeNode element = (DefaultMutableTreeNode) commitFilesJTree
                    .getLastSelectedPathComponent();
            CommitFile commitFile = (CommitFile) element.getUserObject();
		    this.requirementsFromFile(commitFile);
		    this.commitsFromFile(commitFile);
		    this.codeFromFile(commitFile);
		}));
    }

    /**
     * Navigation: ->File->Requirement
     * Clear:
     * Setting: Requirement
     * Using: getRequirementsFromFile
     */
    void requirementsFromFile(CommitFile file){
        try{
	        requirements_JList = new JList<String>(guiModel.getRequirementsFromFile(file));
	    } catch(IOException e){
	        guiView.showErrorDialog("File not found!");
	    }
        guiView.showRequirements(requirements_JList);

        guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.CommitsFromRequirementAndFile));
    }

    /**
     * Navigation: ->File->Commit
     * Clear:
     * Setting: Commits
     * Using: getCommitsFromFile
     */
    void commitsFromFile(CommitFile file){
        commitMessages_JList = new JList<String>(guiModel.getCommitsFromFile(file));
        guiView.showCommits(commitMessages_JList);

        guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.RequirementsFromFileAndCommit));
    }

    /**
     * Navigation: ->Files->Commit->Requirement
     * Clear:
     * Setting: Requirement
     * Using: getRequirementsFromFileAndCommit
     */
    void requirementsFromFileAndCommit(int commitIndex, CommitFile file) {
        try {
            requirements_JList = new JList<String>(guiModel.getRequirementsFromFileAndCommit(commitIndex, file));
        } catch (FileNotFoundException e) {
            guiView.showErrorDialog("Internal storing Error");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        guiView.showRequirements(requirements_JList);

    }




    /**
     * Navigation: ->Commits
     * Clear: All
     * Setting: Commits
     * Using: getCommits
     */
    void commitsFromDB() {
        guiView.clearAll();

        commitMessages_JList = new JList<String>(guiModel.getCommitsFromDB());
        guiView.showCommits(commitMessages_JList);

        guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.RequirementsAndFilesFromCommit));
    }

    /**
     * Navigation: ->Commit->Files
     * Clear: Code/ImpactPercentage
     * Setting: Files
     * Using: getFilesFromCommit
     */
    void filesFromCommit(int commitIndex) {
        guiView.clearCode();
        guiView.clearImpactPercentage();

        try {
            commitFilesJTree = new CommitFilesJTree(guiModel.getFilesFromCommit(commitIndex, commitFileSorting));
        } catch (FileNotFoundException e) {
            guiView.showErrorDialog("Internal storing Error");
            return;
        }
        guiView.showFiles(commitFilesJTree);

        commitFilesJTree.addSelectionListener(new CommitFile_SelectionListener(commitFilesJTree, ()->{
		    DefaultMutableTreeNode element = (DefaultMutableTreeNode) commitFilesJTree
	                .getLastSelectedPathComponent();
	        CommitFile commitFile = (CommitFile) element.getUserObject();
	        this.codeFromFile(commitFile, this.requirements_JList.getSelectedValue());
		}));
    }

    /**
     * Navigation: ->Commits->Requirements
     * Clear:
     * Setting: Requirements
     * Using: getRequirementsFromCommit
     */
    void requirementsFromCommit(int commitIndex) {
        try {
            requirements_JList = new JList<String>(guiModel.getRequirementsFromCommit(commitIndex));
        } catch (IOException e) {
            guiView.showErrorDialog("Internal storing Error");
            return;
        }
        guiView.showRequirements(requirements_JList);
    }

    /**
     * For button AddLinkage
     */
    void requirementsAndCommitsFromDB() {
        guiView.clearAll();

        String[] requirements;
        try {
            requirements = guiModel.getAllRequirements(requirementIDFiltering);
        } catch (IOException e) {
            guiView.showErrorDialog("Internal storing Error");
            return;
        }
        requirements_JList = new JList<String>(requirements);
        guiView.showRequirements(requirements_JList);
        guiView.addMouseListener(requirements_JList, new MouseEvent(this, Action.RequirementToLinkage));

        commitMessages_JList = new JList<String>(guiModel.getCommitsFromDB());
        guiView.showCommits(commitMessages_JList);
        guiView.addMouseListener(commitMessages_JList, new MouseEvent(this, Action.CommitToLinkage));

        guiView.switchLinkage_Button(ButtonState.Activate);
    }

    void RequirementToLinkage(String requirementID) {
        guiView.showLinkageRequirement(requirementID);
    }

    void CommitToLinkage(String commit) {
        guiView.showLinkageCommit(commit);
    }


    /**
     * For now only terminated the application if the user retried some input to often.
     * Later on should handle all actions that have to be completed before exit.
     */
    void handleError(){
        if(Status == RetryStatus.Exit){
            System.exit(1);
        }
    }

    /**
     *For reconfiguring the repository to a new path while the application is running
     *Once this method is successful, the application refers to the new repository
     */

    void reConfigureRepository() throws IOException {
        GuiModel guiModelTrial = guiModel;
        for(int i = 0; i<=MAX_RETRIES; i++){
            if(i == MAX_RETRIES){
                Status = RetryStatus.Cancel;
                guiView.showErrorDialog("Maximum retries exceeded");
                return;
            }
            File repoFile = null;
            try {
                repoFile = guiView.Repo_OpeningDialog();
            } catch (IOException e1) {

            }
            if(repoFile == null){
                Status = RetryStatus.Cancel;
                return;
            }
            try {
                guiModelTrial = reInitModel(null, null, repoFile, Pattern.compile(guiModel.getCurrentRequirementPatternString()));
                guiView.showInformationDialog("Repository Path modified to " + repoFile.getPath());
                break;
            } catch (IOException | RuntimeException e) {

                guiView.showErrorDialog(e.getMessage());
                handleError();
            }
        }
        guiModel = guiModelTrial;
        requirementsFromDB();
    }
    /**
     * For reconfiguring the requirement pattern to a new pattern while the application is running
     * Once this method is successful, the application refers to the new requirement pattern
     */
    void reConfigureRequirementPattern() throws IOException {
        GuiModel guiModelTrial = guiModel;
        for(int i = 0; true; i++){
            if(i == MAX_RETRIES){
                Status = RetryStatus.Cancel;
                guiView.showErrorDialog("Maximum retries exceeded");
                return;
            }
            Pattern reqPattern;
            try {
                reqPattern = Pattern.compile(guiView.Pattern_OpeningDialog(guiModel.getCurrentRequirementPatternString()));
            }catch (Exception e){
                //todo error message about bad pattern
                Status = RetryStatus.Cancel;
                return;
            }

            try {
                guiModelTrial = reInitModel(null, null, new File(guiModel.getCurrentRepositoryPath()), reqPattern);
                guiView.showInformationDialog("Requirement Pattern modified to " + reqPattern);
                break;
            } catch (RuntimeException | IOException e) {
                guiView.showErrorDialog(e.getMessage());
                handleError();
            }
        }
        guiModel = guiModelTrial;
        requirementsFromDB();

    }

    /**
     * method to divert configuration calls
     */
    void reConfigure() throws IOException {
        switch(guiView.Configure_OptionDialog())
        {
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
     * initialize model again. If any arg is null, the default value will be used
     * @param vcs vcs client
     * @param ds data source
     * @param repoFile path to git repo
     * @param reqPattern pattern to parse req id from commit messages
     * @return model to use
     */
    private GUITrackerToModelAdapter reInitModel(VcsClient vcs, DataSource ds, File repoFile, Pattern reqPattern) throws IOException {

        if (repoFile == null){
            repoFile = new File(AppProperties.GetValue("DefaultRepoPath"));
        }

        if (vcs == null){
            vcs = new GitVcsClient(repoFile.toString());
        }

        if (reqPattern == null){
            reqPattern = Pattern.compile(AppProperties.GetValue("RequirementPattern"));
        }

        if (ds == null) {
            CSVFileDataSource csvDs = new CSVFileDataSource(new File(repoFile.getParentFile(), AppProperties.GetValue("DefaultPathToCSVFile")));
            VCSDataSource vcsDs = new VCSDataSource(vcs, new CommitMessageParser(reqPattern));
            DBDataSource dbDs = new DBDataSource();
            ds = new CompositeDataSource(dbDs, csvDs, vcsDs);
        }

        return new GUITrackerToModelAdapter(vcs, ds, repoFile, reqPattern);
    }
    
    void addLinkage(String requirementID, int commitIndex) {
        try {
            guiModel.addRequirementCommitLinkage(requirementID, commitIndex);
            guiView.showInformationDialog("Successfully Added!");
        } catch (FileNotFoundException e) {
            guiView.showErrorDialog("Internal storing Error");
            return;
        }finally{
            guiView.clearAll();
        }
    }
    
    void getTraceabilityMatrix(){
        try {
            guiView.showTraceabilityMatrix(guiModel.getRequirementsTraceability());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    void getTraceabilityMatrixByImpact(){
        try {
            guiView.showTraceabilityMatrixByImpactProgressBar();
            guiView.showTraceabilityMatrixByImpact(guiModel.getRequirementsTraceabilityByImpact());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
