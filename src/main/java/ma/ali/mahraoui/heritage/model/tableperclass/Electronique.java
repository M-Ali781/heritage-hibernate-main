package ma.ali.mahraoui.heritage.model.tableperclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entité concrète Electronique stockée dans sa propre table 'electroniques'.
 *
 * @author Ali Mahraoui
 */
@Entity
@Table(name = "electroniques")
public class Electronique extends Produit {

    @Column(nullable = false)
    private int garantie;

    public Electronique() {
        super();
    }

    public Electronique(String nom, int garantie) {
        super(nom);
        this.garantie = garantie;
    }

    public int getGarantie() {
        return garantie;
    }

    public void setGarantie(int garantie) {
        this.garantie = garantie;
    }

    @Override
    public String toString() {
        return String.format("Electronique [id=%d, nom='%s', garantie=%d mois]",
                getId(), getNom(), garantie);
    }
}
