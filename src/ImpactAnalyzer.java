import java.util.List;

import parser.*;
import connection.*;

/**
 * 
 */


/**
 * @author Captain Sparrow
 *
 */
public class ImpactAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connector connector = new GitConnector();
		Parser parser = new GitCommitMessageParser();
		List<Integer> CommitToReqListOfPairs = parser.parse(connector.getLatestCommitMessage());
		
		System.out.println("For the CommitMessage " + connector.getLatestCommitId());
		System.out.println("There are these Requirements:");
		for(Integer reqId : CommitToReqListOfPairs){
			System.out.println(reqId);
		}
	}

}
