package de.fau.osr.app;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.HibernateUtil;
import de.fau.osr.core.db.dao.impl.RequirementDaoImplementation;
import de.fau.osr.core.db.domain.Requirement;

/**
 * loads some data to test to the Database
 * Created by Dmitry Gorelenkov on 02.06.2015.
 */
public class DbLoadScript {
    public static void main(String[] args) {
        RequirementDaoImplementation reqDao = new RequirementDaoImplementation();
        Requirement reqToAdd = new Requirement();

        String[][] reqsData = {
                {"1", "Team contract", "5", "We as a team need to create and agree on a team contract, so that the team can agree on the working conditions in this project"},
                {"2", "Github repository", "1", "We as a team need to set up a Github repository so that we can work together on this project"},
                {"3", "Software licence", "0", "We as a team need to include the AGPL licence into our Github repository"},
                {"4", "Git commit parser", "3", "As a user I am able to add a requirement ID to my git commit message so that the system can list pairs of related requirement IDs and commit IDs"},
                {"5", "File to commit mapping", "0", "As a user I am able to get a list of files related to a certain git commit so that I have an overview of all affected files"},
                {"6", "Code to file mapping", "5", "As a user I am able to get a list of code lines related to a certain file that relates to a certain requirement so that I have an overview of all affected code changes relating to my requirements"},
                {"7", "Requirement to code linkage", "3", "As a user I want to see all source code files relating to a specific requirement in one overview window so that I can trace my requirements"},
                {"8", "File to requirment linkage", "3", "As a user I want to see all requirements relating to a specific source file in one overview window so that I can trace my requirements"},
                {"9", "Display in GUI code lines of a file relating to a requirement", "2", "As a user I want to see all source code lines in a specific file relating to a specific requirement in the GUI so that I can trace my requirements"},
                {"10", "Requirement to code GUI linkage", "1", "As a user I want to see all requirements relating to source code lines of a specific source file in the GUI so that I can trace my requirements"},
                {"11", "Highlight code lines", "5", "As a user I want to see code lines relating to a specific requirement highlighted so that I can distinguish them from the others"},
                {"12", "Display list of requirements", "3", "As a user I am able to see all requirement IDs from a specific source (e.g. jira) in one list so that I can have a clear overview of my requirements"},
                {"13", "Add linkage post implementation", "5", "As a user I can add a linkage between requirements and commits after the implementation so that I can improve the traceability even after a project is already finished"},
                {"15", "Change repository", "3", "As a user I want to be able to change the repository that I am working on, so that I can switch between repositories without restarting the application"},
                {"16", "Sort source code files", "3", "As a user I can sort the list of source code files related to a specific requirement, so that i can investigate these results better"},
                {"17", "GUI", "8", "As a user I need a graphical user interface, so that I can work more efficiently"},
                {"18", "Different requirement names/tags", "1", "As a user I can specify different requirement tags that the software should look for in my git commit comments, so that I can give different requirement tags to my requirements"},
                {"19", "Multiple requirement names/tags", "1", "As a user I can specify multiple requirement tags in different formats that the software should look for all of them in my git commit comments, so that I can get results about different requirement tags"},
                {"20", "Notify user before check-in", "0", "As a user I want to get a notification from the system whenever I commit code so that information is provided before the commit is executed."},
                {"21", "Visualize requirements before check-in", "0", "As a user whenever I commit code I want to get a preview of the requirements so that I know what impact my changes might have on those requirements."},
                {"22", "Enable plugin inside IDE", "0", "As a user I can enable requirements tracking as a plugin inside my IDE so that I can do all my work in one application."},
                {"23", "Requirements linked to code lines", "0", "As a user I can see information about requirements inside my IDE so that I know which requirements are linked to each code line."},
                {"24", "Showing Traceability matrix", "8", "As I user I want to be able to generate a requirements traceability matrix so that I have an overview of the dependencies between requirements and files."},
                {"25", "Display the last commit", "0", "As a user I want to see insight of IDE the Commit, that has changed the specified code line"},
                {"26", "Extended info in commit comments", "0", "As a user, I want that Commit comments contain IDs of requirements, which were affected by the Commit, with the degree of affection (medium or high)."},
                {"27", "Impact of requirement on file", "1", "As a user I want a percentage value showing me the impact that a requirement has on a specfic file so that I more information about the impact of my requirements."},
                {"28", "Traceability matrix as PDF", "3", "As a user I want to be able to save the traceability matrix as PDF from the GUI so that I have a digital copy of this visualisation."},
                {"29", "Search for Reqiurement", "1", "As a user I want to have a search field so that I can look for specific requirements without scrolling through the entire list."},
                {"30", "Scrolling bar", "2", "As a user I need a scroll bar so that i know wich panels are scrollable"},
                {"31", "Export Traceability matrix", "1", "As a user I want to be able to export the traceability matrix in format that can be interpreted by excel so that I have a excel document."},
                {"32", "Dynamic update", "0", "As a user I want the system to update its linkage automatically so that all values are correct whenever I check in new commits to my repository."},
                {"33", "Visualize files before check-in", "0", "As a user whenever I commit code I want to get a preview of source files so that I know what impact my changes might have on those files."},
                {"34", "Selecting Files that were deleted", "0", "As a user I don't want to be able to select files that are not there anymore so that I don't get confused."},
                {"35", "Displaying files that can not be selected", "0", "As a user I want to see which files can be selected so that I don't have to find it out by myself."},
                {"36", "Resizing scroll panels", "0", "As a user I want to be able to change the size of scroll panels so that I can operate in a way that is more effective for me."},
                {"37", "Multiselect commits", "0", "As a user want to select multiple commits for each requirement so that I can work more effectively. (Issue 54 - Single Mouse Selection to List Selection)"},
                {"38", "Impact of requirement on file", "0", "As a user I want a percentage value showing me the impact that a requirement has on a specfic file so that I more information about the impact of my requirements."},
                {"39", "Issue 50", "1", "Traceability Matrix - File ascending order display is not working"},
                {"40", "Issue 51", "5", "Core - Impact analysis is inconsistant"},
                {"41", "Tree of code source files", "5", "As a user, I want to see the list of code source files as a tree"},
                {"42", "Req to Line Column", "3", "As a user I want to see all requirements that belong to a code line in a column that is next to the code line so that the linkage is directly visible for me."},
                {"43", "Display requirement content", "0", "As a user, I want to see more information about a requirement so that I can clearly understand the requirement."},
                {"44", "Removing linkage", "0", "As a user, I want to be able to remove linkage between requirement ID and commit"},
                {"45", "Progress bar for traceability matrix", "1", "As a user, I want to see the progress bar while the traceability matrix is generated, so that I can see the process"},
                {"46", "Display of file names and requirementID in traceability matrix", "1", "As a user, I want to see only file names in the matrix header, and I am able to see the requiremenID and full file path for each cell in the matrix"},
                {"47", "The name of PDF/CSV file with traceability matrix", "0", "As a user, I want that PDF/CSV file with stored traceability matrix contain the name of repository and the date when the matrix was generated"},
                {"48", "Adding Linkage to files", "0", "As a user, I want to be able to add linkage between requirement ID and file"},
                {"49", "Removing linkage", "0", "As a user, I want to be able to remove linkage between requirement ID and files"},
                {"50", "Requirement title", "0", "As I user I want the system to store additional information about my requirements, so that I can see more than requirement ID"},
                {"51", "Issue 58", "0", "When the user selects a line of code, all previously displayed requirement IDs disappear."},
                {"52", "Requirement description", "0", "As I user I want the system to store additional information about my requirements, so that I can see more than a requirement ID"},
                {"52", "Issue 59", "0", "As a user, I want to to have a \"right - aligned\" alignment of file names in traceability matrix"},
                {"53", "Display files differetly", "0", "As a user, I whant to see differetly what files were linked automatically by commit and that were linked using post traceability function"},

        };

        for (String[] reqData : reqsData) {
            reqToAdd.setId(reqData[0]);
            reqToAdd.setTitle(reqData[1]);
            reqToAdd.setStoryPoint(Integer.parseInt(reqData[2]));
            reqToAdd.setDescription(reqData[3]);
            //add or update
            reqDao.persist(DBOperation.ADD, reqToAdd);
            reqDao.persist(DBOperation.UPDATE, reqToAdd);
        }

        HibernateUtil.shutdown();

    }

}
