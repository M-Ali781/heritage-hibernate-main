package ma.ali.mahraoui.heritage.model.singletable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Sous-classe Moto stockée dans la table 'vehicules' avec le discriminant 'MOTO'.
 *
 * @author Ali Mahraoui
 */
@Entity
@DiscriminatorValue("MOTO")
public class Moto extends Vehicule {

    private boolean sidecar;

    public Moto() {
        super();
    }

    public Moto(String marque, boolean sidecar) {
        super(marque);
        this.sidecar = sidecar;
    }

    public boolean isSidecar() {
        return sidecar;
    }

    public void setSidecar(boolean sidecar) {
        this.sidecar = sidecar;
    }

    @Override
    public String toString() {
        return String.format("Moto [id=%d, marque='%s', sidecar=%b]",
                getId(), getMarque(), sidecar);
    }
}
