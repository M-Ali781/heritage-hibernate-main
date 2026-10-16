package ma.ali.mahraoui.heritage.model.joined;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entité Manager stockée dans la table dédiée 'managers' jointe à 'employes'.
 *
 * @author Ali Mahraoui
 */
@Entity
@Table(name = "managers")
public class Manager extends Employe {

    @Column(nullable = false)
    private int nombreEquipes;

    public Manager() {
        super();
    }

    public Manager(String nom, int nombreEquipes) {
        super(nom);
        this.nombreEquipes = nombreEquipes;
    }

    public int getNombreEquipes() {
        return nombreEquipes;
    }

    public void setNombreEquipes(int nombreEquipes) {
        this.nombreEquipes = nombreEquipes;
    }

    @Override
    public String toString() {
        return String.format("Manager [id=%d, nom='%s', nombreEquipes=%d]",
                getId(), getNom(), nombreEquipes);
    }
}
