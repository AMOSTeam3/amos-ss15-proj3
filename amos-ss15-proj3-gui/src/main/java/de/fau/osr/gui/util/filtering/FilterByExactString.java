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
package de.fau.osr.gui.util.filtering;

import com.google.common.base.Predicate;
import de.fau.osr.gui.Model.DataElements.Requirement;

/**
 * Filter-Strategy to filter/find a given Requirement-ID (String).
 * Empty-String means no filtering.
 * @author: Taleh Didover
 */
public class FilterByExactString implements Predicate<Requirement> {
    private String findString;

    public FilterByExactString() {
        this(null);
    }

    public FilterByExactString(String exactString) {
        if (exactString == null)
            exactString = "";

        this.findString = exactString;
    }

    @Override
    public boolean apply(Requirement req) {
        // if findString is empty, then no filtering.
        return findString.isEmpty() || findString.equals(req.getID());
    }
}
