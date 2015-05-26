package de.fau.osr.bl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.db.DataSourceTest;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

public class RequirementsTraceabilityMatrixByImpactTest {

	List<String> files;
	static Tracker tracker;
	static RequirementsTraceabilityMatrixByImpact matrix;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		VcsClient client =  VcsClient.connect(VcsEnvironment.GIT, PublicTestData.getGitTestRepo());
		tracker = new Tracker(client, null, null);
		matrix  = new RequirementsTraceabilityMatrixByImpact(tracker);
	}

	@Test
	public void testProcess() {
		matrix.Process();
		Collection files  = tracker.getAllFilesAsString();
		for(Object file : files) {
			
			RequirementFileImpactValue value = matrix.getImpactValue(new RequirementFilePair("1",file.toString()));
			if(value!=null){
				if(file.toString().equals("TestFile4") && value.impactPercentage==100.0){
					assertTrue(true);
					return;
				}
					
			}
			
			
		}
		assertTrue(false);
		
	}

	
}
