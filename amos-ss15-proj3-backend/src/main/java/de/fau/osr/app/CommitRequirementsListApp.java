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
package de.fau.osr.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;
import de.fau.osr.util.AppProperties;
import de.fau.osr.util.parser.CommitMessageParser;
import de.fau.osr.util.parser.Parser;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author tobias
 * This is an application to fulfill the acceptance criterion for Req-4
 */
public class CommitRequirementsListApp {

    private static class CliOptions {
        @Parameter(names = "-repo", required = true)
        String repoURL;
    }

    public static void main(String[] args) {
        CliOptions cli = new CliOptions();
        new JCommander(cli, args);
        final VcsClient client = VcsClient.connect(VcsEnvironment.GIT, cli.repoURL);
        Parser parser = new CommitMessageParser(Pattern.compile(AppProperties.GetValue("RequirementPattern")));
        for(String commitId : new Iterable<String>(){
            @Override
            public Iterator<String> iterator() {
                return client.getCommitList();
            }}) {
            String commitMsg = client.getCommitMessage(commitId);
            List<String> reqs = parser.parse(commitMsg);
            for(String req : reqs) {
                System.out.println("commit " + commitId + " references Req-" + req);
            }
        }
    }

}
