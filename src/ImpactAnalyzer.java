import java.io.IOException;
import java.util.List;
import parser.*;
import connection.*;


/**
 * current main component: the shell output of each feature should be another method
 * @author Florian Gerdes
 * @version 0.2
 */
public class ImpactAnalyzer {

	private static final String LOCAL_REPOSITORY_URL ="C:\\Users\\Captain Sparrow\\git\\amos-ss15-proj3\\.git";
	
	public static void main(String[] args){
		getLatestCommitReq();
	}
	
	/**
	 *  @author Florian Gerdes
	 *  @version 0.2
	 *  
	 * currently the whole process is divided in two parts: establishing a Connection and extracting the 
	 * information from git and secondly parsing the commit message
	 * after all the results are displayed on the shell
	 */
	public static void getLatestCommitReq(){
		//intialisation
		Connector connector = new GitConnector();
		Parser parser = new GitCommitMessageParser();
		
		//extracting latest commit message and parsing it
		List<Integer> ReqIdsInCommitMessage = null;
		try {
			ReqIdsInCommitMessage = parser.parse(connector.getLatestCommitMessage(LOCAL_REPOSITORY_URL));
			System.out.println("For the CommitMessage " + connector.getLatestCommitId(LOCAL_REPOSITORY_URL));
		} catch (IOException e) {
			System.err.println("Incorrect Gitpath, Gitrepository unreadable or no Commit found! Read here for further information: " + e.getMessage());
		} catch (IllegalArgumentException e){
			System.err.println("Incorrect Gitpath or Gitrepository unreadable! Read here for further information: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//displaying results
		System.out.println("There are these Requirements:");
		for(Integer reqId : ReqIdsInCommitMessage){ //note that ReqIdsInCommitMessage is currently still null
			System.out.println(reqId);
		}
	}

}
