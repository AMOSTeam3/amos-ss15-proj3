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
