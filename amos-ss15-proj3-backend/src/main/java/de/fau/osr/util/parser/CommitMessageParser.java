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
package de.fau.osr.util.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Taleh Didover
 *
 * Parses requirement id in given commit message.
 * Format of requirement ids must be Req-\d+
 *
 */
public class CommitMessageParser implements Parser {

    private Pattern reqPattern;

    public CommitMessageParser(Pattern pattern) {
        if (pattern != null) {
            this.setPattern(pattern);
        }
    }

    @Override
    public List<String> parse(String latestCommitMessage) {
        Matcher m = reqPattern.matcher(latestCommitMessage);
        List<String> found_reqids = new ArrayList<String>();

        while (m.find())
            for (int i=0; i < m.groupCount(); ++i) {
                String group = m.group(i + 1);
                if (group != null)
                    found_reqids.add(group);
            }

        return found_reqids;

    }

    public void setPattern(Pattern pattern) {
        reqPattern = pattern;
    }

    public Pattern getPattern() {
        return reqPattern;
    }
}
