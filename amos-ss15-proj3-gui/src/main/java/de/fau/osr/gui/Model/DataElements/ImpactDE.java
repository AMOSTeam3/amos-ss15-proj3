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
