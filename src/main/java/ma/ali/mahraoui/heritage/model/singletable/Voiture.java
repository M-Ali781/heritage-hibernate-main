package ma.ali.mahraoui.heritage.model.singletable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Sous-classe Voiture stockée dans la table 'vehicules' avec le discriminant 'VOITURE'.
 *
 * @author Ali Mahraoui
 */
@Entity
@DiscriminatorValue("VOITURE")
public class Voiture extends Vehicule {

    private int nombrePortes;

    public Voiture() {
        super();
    }

    public Voiture(String marque, int nombrePortes) {
        super(marque);
        this.nombrePortes = nombrePortes;
    }

    public int getNombrePortes() {
        return nombrePortes;
    }

    public void setNombrePortes(int nombrePortes) {
        this.nombrePortes = nombrePortes;
    }

    @Override
    public String toString() {
        return String.format("Voiture [id=%d, marque='%s', nombrePortes=%d]",
                getId(), getMarque(), nombrePortes);
    }
}
