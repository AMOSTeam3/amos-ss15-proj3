/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;

import java.nio.file.Path;

/**
 * This entity class holds the file path of a commited and existing file.
 *
 * PathDE == Path DataElement
 *
 * @author Taleh Didover
 */
public class PathDE extends DataElement {

    final public Path FilePath;

    /**
     */
    public PathDE(Path filePath) {
        this.FilePath = filePath;
    }


    public PathDE(PathDE f) {
        this.FilePath = f.FilePath;
    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathDE)) return false;

        PathDE that = (PathDE) o;

        return this.FilePath.equals(that.FilePath);
    }

    @Override
    public int hashCode() {
        return this.FilePath.hashCode();
    }

    @Override
    public String toString() {
        return FilePath.toString();
    }
}
