package osr.adapter;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import osr.core.InitClass;
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

public class PluginSPICETrackerAdaptor {
	
	Tracker tracker;
	
	
	public PluginSPICETrackerAdaptor() throws IOException {
		super();
		Pattern pattern = Pattern.compile("[Rr]eq-0*(\\d+)");
		
		//File repoFile = new File(repoPath);
		File repoFile = new File(RegistrySettings.repoURL);
		
		VcsClient vcs = new GitVcsClient(repoFile.getPath());
        CSVFileDataSource csvDs = new CSVFileDataSource(new File(repoFile.getParentFile(), "dataSource.csv"));
        VCSDataSource vcsDs = new VCSDataSource(vcs, new CommitMessageParser(pattern));
        DBDataSource dbDs = new DBDataSource();
        DataSource ds = new CompositeDataSource(dbDs, csvDs, vcsDs);
        
        tracker = new Tracker(vcs, ds, repoFile);
	}


	public String[] getRequirementLineLinkForFile(String filePath)throws Exception{
		return tracker.getRequirementsLineLinkageForFile(filePath);
	}
	
	
}
