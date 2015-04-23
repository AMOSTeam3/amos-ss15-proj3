/**
 * 
 */
package de.fau.osr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.fau.osr.app.CommitRequirementsListAppTest;
import de.fau.osr.core.vcs.base.VcsControllerTest;
import de.fau.osr.parser.GitCommitMessageParserTest;

/**
 * This TestSuite is Req-4
 * Use this kind of TestSuites to write manageable regression tests.
 * @author Florian Gerdes
 *  @see <a href="URL#https://github.com/uv48uson/amos-ss15-proj3/wiki/3.-Testing">Testing Requirements</a>
 */
@RunWith(Suite.class)
@SuiteClasses({VcsControllerTest.class, GitCommitMessageParserTest.class, CommitRequirementsListAppTest.class})
public class Req_4_Test {

}
