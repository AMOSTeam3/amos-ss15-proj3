package de.fau.osr.bl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.experimental.runners.Enclosed;

import de.fau.osr.PublicTestData;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;

/**
 * @author Gayathery
 *
 */
@RunWith(Enclosed.class)
public class VcsInterpreterTest {
	
	static VcsController vcs = new VcsController(VcsEnvironment.GIT);
	static VcsInterpreter interpreter;
	
	public static void setup() {
		vcs.Connect(System.class.getResource("/test2/git/").getPath());
		interpreter = new VcsInterpreter(vcs);
	}
	
	@RunWith(Parameterized.class)
	public static class TestReqToFile {
		Integer req;
		List<String> expected;

		public TestReqToFile(Integer req, List<String> expected) {
			this.req = req;
			this.expected = expected;
		}
		
		@Parameters
		public static Collection<Object[]> data() {
			if(interpreter == null) {
				setup();
			}
			return Arrays.asList(new Object[][]{
					new Object[]{0, Arrays.asList(new File[]{new File("file3")})},
					new Object[]{1, Arrays.asList(new File[]{new File("file3")})},
					new Object[]{2, Arrays.asList(new File[]{new File("file3")})},
					new Object[]{3, Arrays.asList(new File[]{new File("file5")})},
					new Object[]{4, Arrays.asList(new File[]{new File("file5")})},
					new Object[]{5, Arrays.asList(new File[]{new File("file4")})},
					new Object[]{6, Arrays.asList(new File[]{new File("file4")})},
					new Object[]{7, Arrays.asList(new File[]{new File("file4")})},
					new Object[]{8, Arrays.asList(new File[]{new File("file5")})},
					});
		}

		/**
		 * Test method for {@link de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)}.
		 */
		@Test
		public void testGetCommitFilesForRequirementID() {
			Iterable<File> commitFileIterable = interpreter.getCommitFilesForRequirementID(req);
			ArrayList<File> commitFileList = new ArrayList<>();
			for(File file : commitFileIterable) {
				commitFileList.add(file);
			}
			assertEquals(expected, commitFileList);
		}
	}
	@RunWith(Parameterized.class)
	public static class TestFileToReq {
		File file;
		List<Integer> expected;
		
		public TestFileToReq(File file, List<Integer> expected) {
			this.file = file;
			this.expected = expected;
		}

		@Parameters
		public static Collection<Object[]> data() {
			if(interpreter == null) {
				setup();
			}
			return Arrays.asList(new Object[][]{
					new Object[]{new File("file3"), Arrays.asList(new Integer[]{0,1,2})},
					new Object[]{new File("file4"), Arrays.asList(new Integer[]{5,6,7})},
					new Object[]{new File("file5"), Arrays.asList(new Integer[]{3,4,8})},
					});
		}

		/**
		 * Test method for {@link de.fau.osr.bl.VcsInterpreter#getCommitFilesForRequirementID(java.lang.String)}.
		 */
		@Test
		public void testGetCommitFilesForRequirementID() {
			Iterable<Integer> commitFileIterable = interpreter.getRequirementsForFile(file);
			ArrayList<Integer> reqList = new ArrayList<>();
			for(Integer req : commitFileIterable) {
				reqList.add(req);
			}
			Collections.sort(reqList);
			assertEquals(expected, reqList);
		}
	}
}
