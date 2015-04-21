/**
 * 
 */
package de.fau.osr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.fau.osr.core.vcs.base.VcsControllerCommitMessageParserTest;
import de.fau.osr.core.vcs.base.VcsControllerTest;
import de.fau.osr.parser.GitCommitMessageParserTest;

/**
 * @author Florian Gerdes
 *
 */
@RunWith(Suite.class)
@SuiteClasses({VcsControllerTest.class, GitCommitMessageParserTest.class, VcsControllerCommitMessageParserTest.class })
public class AllTests {

}
