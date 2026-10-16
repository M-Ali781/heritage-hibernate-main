package ma.ali.mahraoui.heritage.model.joined;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entité Développeur stockée dans la table dédiée 'developpeurs' jointe à 'employes'.
 *
 * @author Ali Mahraoui
 */
@Entity
@Table(name = "developpeurs")
public class Developpeur extends Employe {

    @Column(nullable = false, length = 80)
    private String langage;

    public Developpeur() {
        super();
    }

    public Developpeur(String nom, String langage) {
        super(nom);
        this.langage = langage;
    }

    public String getLangage() {
        return langage;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    @Override
    public String toString() {
        return String.format("Developpeur [id=%d, nom='%s', langage='%s']",
                getId(), getNom(), langage);
    }
}
