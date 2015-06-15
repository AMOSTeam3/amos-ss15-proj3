package osr.adapter;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.*;
import de.fau.osr.core.vcs.impl.GitVcsClient;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.parser.CommitMessageParser;
import osr.core.RegistrySettings;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Gayathery
 *  This is an adaptor class to create all necessary instance settings to use the backend jar.
 */
public class PluginSPICETrackerAdaptor {
   
    private static Tracker tracker;
    private static PluginSPICETrackerAdaptor instance= null;
    
    /**
     * Constructor of the class
     */
    private PluginSPICETrackerAdaptor(){
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
    
    
    /**
     * @return the instance of the class 
     */
    public static PluginSPICETrackerAdaptor  getInstance(){
        if(instance == null)
            return new PluginSPICETrackerAdaptor();
        else 
            return instance;
    }
    
    /**
     * This method is used to reset the instance, so that the next call of the instance triggera a refreshed object
     */
    public static void resetInstance(){
        instance = null;
    }
    /**
     * This method returns the Requirements and the line number markings for the given file served from the backend jar.
     * @param filePath
     * @return
     * @throws Exception
     */
    public List<Collection<String>> getRequirementLineLinkForFile(String filePath)throws Exception{
        return tracker.getRequirementsLineLinkageForFile(filePath);
    }


    public static Tracker getTracker() {
        return tracker;
    }
    
    
    
}
