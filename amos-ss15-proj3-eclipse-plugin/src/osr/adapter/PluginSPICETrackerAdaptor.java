package osr.adapter;

import java.io.File;
import java.util.regex.Pattern;

import osr.core.RegistrySettings;
import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.CompositeDataSource;
import de.fau.osr.core.db.DBDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.db.VCSDataSource;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.parser.CommitMessageParser;

/**
 * @author Gayathery
 *	This is an adaptor class to create all necessary instnace settings to use the backend jar.
 */
public class PluginSPICETrackerAdaptor {
	
	/**
	 * 
	 */
	private static Tracker tracker;
	
	static{
		try{
			Pattern pattern = Pattern.compile(RegistrySettings.requirementPattern);
			File repoFile = new File(RegistrySettings.repoURL);
			
			VcsClient vcs = new GitVcsClient(repoFile.getPath());
	        CSVFileDataSource csvDs = new CSVFileDataSource(new File(repoFile.getParentFile(), "dataSource.csv"));
	        VCSDataSource vcsDs = new VCSDataSource(vcs, new CommitMessageParser(pattern));
	        DBDataSource dbDs = new DBDataSource();
	        DataSource ds = new CompositeDataSource(dbDs, csvDs, vcsDs);
	        
	        tracker = new Tracker(vcs, ds, repoFile);
		}catch(Exception e){
			
		}
	}
	
	public static Tracker getTrackerInstance(){
		return tracker;
	}
	
	/**
	 * This method returns the Requirements and the line number markings for the given file served from the backend jar.
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String[] getRequirementLineLinkForFile(String filePath)throws Exception{
		return tracker.getRequirementsLineLinkageForFile(filePath);
	}
	
	
}
