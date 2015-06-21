package de.fau.osr.gui.Model.DataElements;

import de.fau.osr.gui.Controller.Visitor;
import de.fau.osr.gui.View.Presenter.Presenter;



public class Requirement extends DataElement {
    private String id;
    private String title;
    private String description;

    public Requirement(de.fau.osr.core.db.domain.Requirement requirement) {
        this.id = requirement.getId();
        this.title = requirement.getTitle();
        this.description = requirement.getDescription();
    }

    public Requirement(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    @Override
    public Presenter visit(Visitor visitor) {
        return visitor.toPresenter(this);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Requirement)) return false;

        Requirement that = (Requirement) o;

        if (!id.equals(that.id)) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return !(description != null ? !description.equals(that.description) : that.description != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
