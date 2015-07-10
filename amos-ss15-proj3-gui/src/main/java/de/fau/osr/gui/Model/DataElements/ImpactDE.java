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
package de.fau.osr.gui.Model.DataElements;

        import de.fau.osr.gui.Controller.Visitor;
        import de.fau.osr.gui.View.Presenter.Presenter;

        import java.nio.file.Path;

/**
 * This entity class holds the impact value of a
 * commited and existing file for a set of requirement ids.
 *
 * ImpactDE == Impact  DataElement
 *
 * @author Taleh Didover
 */
public class ImpactDE extends DataElement {

    final public Float Impact;

    /**
     */
    public ImpactDE(float impactValue) {
        this.Impact = impactValue;
    }


    public ImpactDE(ImpactDE f) {
        this.Impact = f.Impact;
    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImpactDE)) return false;
        return false;
    }

    @Override
    public int hashCode() {
        return Impact.hashCode();
    }
}
