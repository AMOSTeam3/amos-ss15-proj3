/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.util;

import de.fau.osr.PublicTestData;
import junit.framework.TestCase;

import org.junit.BeforeClass;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * author: Taleh Didover
 */
public class VisibleFilesTraverserTest extends TestCase {


    final String testRepoFile = PublicTestData.getGitTestRepo().replace("git", "");
    List<String> expectedTestRepoFileContent;

    @BeforeClass
    public void setUp() {
        expectedTestRepoFileContent = Arrays.asList(
                "TestFile2",
                "TestFile1",
                "reqFile/SampleTestFile1.txt",
                "TestFile4",
                "LICENSE",
                "git/description",
                "git/HEAD",
                "git/objects/95/91157b0e0241b23c012c7d3332a2cfa9438902",
                "git/objects/ac/5ab8fbdb5f9b410cba904145f975593182dced",
                "git/objects/c4/5b91e57b93931489c6c3abb26edabdbfc36c43",
                "git/objects/02/56ffd2f4757804354495d7324a515520bca3f6",
                "git/objects/f3/196114a214a91ae3994b6cf6424d8347b2e918",
                "git/objects/f7/97a24287f5764b5798775cb467154681e29430",
                "git/objects/44/2dd61341346eca30ac164c2e8e4e22276d2a04",
                "git/objects/b0/b5d16e8071c775bdcd1b2d0b1cca464917780b",
                "git/objects/38/3a033b2fa2b26968e751f9c4176f5bbcabdf5f",
                "git/objects/e8/09219f14332572b9cd07c47ff4c77d913bfef9",
                "git/objects/6f/e0630845fefb46069e6450aeffc2b5b13746e1",
                "git/objects/a8/dc4129802939d620ce0bd3484a1f0538338a0e",
                "git/objects/ec/51eef48b2821f95849d9f9aac8cc876682cf41",
                "git/objects/ec/3b8ce27adc17dbac9db6e067103c9be2f97313",
                "git/objects/bc/87c2039d1e14d5fa0131d77780eaa3b2cc627c",
                "git/objects/01/59d74fb599379b9007b2c7c46dcb22d9d3e77b",
                "git/objects/de/e896c8d52af6bc0b00982ad2fcfca2d9d003dc",
                "git/objects/4a/486acd6261cdc9876c5cb6b6d0e88883eea28d",
                "git/objects/11/0c58e6f30cdd0d7dbe279abaec7e0633fbfca6",
                "git/objects/92/3d0b83e54f89981e80ec1510753a77f0b05b84",
                "git/objects/b5/6a4051230fb495fdb692982857f4f716dfc1e3",
                "git/config",
                "git/refs/heads/master",
                "git/refs/heads/test1",
                "git/refs/heads/test2",
                "git/info/exclude",
                "git/packed-refs",
                "git/hooks/prepare-commit-msg.sample",
                "git/hooks/applypatch-msg.sample",
                "git/hooks/pre-commit.sample",
                "git/hooks/pre-applypatch.sample",
                "git/hooks/commit-msg.sample",
                "git/hooks/pre-push.sample",
                "git/hooks/update.sample",
                "git/hooks/post-update.sample",
                "git/hooks/pre-rebase.sample",
                "git/FETCH_HEAD",
                "git/ORIG_HEAD",
                "git/logs/HEAD",
                "git/logs/refs/remotes/origin/HEAD",
                "git/logs/refs/heads/master",
                "git/logs/refs/heads/test1",
                "git/logs/refs/heads/test2",
                "git/index"
        );
    }

    public void testTraversWithoutIgnoring() throws Exception {
        VisibleFilesTraverser filetraverser = VisibleFilesTraverser.Get(
                Paths.get(testRepoFile)
        );
        List<String> got = new ArrayList<>();
        filetraverser.traverse().forEach((Path file) -> {
            got.add(file.toString().replaceAll("\\\\","/"));
        });
        List<String> expected = expectedTestRepoFileContent;

        Collections.sort(expected);
        Collections.sort(got);
        assertEquals(expected, got);

    }

    public void testTraversWithIgnoring() throws Exception {
        VisibleFilesTraverser filetraverser = VisibleFilesTraverser.Get(
                Paths.get(testRepoFile),
                "git",
                ".txt"
        );
        List<String> got = new ArrayList<>();
        filetraverser.traverse().forEach((Path file) -> {
            got.add(file.toString().replaceAll("\\\\","/"));
        });
        List<String> expected = new ArrayList();

        expectedTestRepoFileContent.forEach(new Consumer<String>() {
            public void accept(String filename) {
                if (!(filename.startsWith("git")||filename.endsWith(".txt")))
                    expected.add(filename);
            }
        });

        Collections.sort(expected);
        Collections.sort(got);
        assertEquals(expected, got);
    }
}

