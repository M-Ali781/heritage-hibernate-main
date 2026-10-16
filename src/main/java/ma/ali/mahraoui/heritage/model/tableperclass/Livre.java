package ma.ali.mahraoui.heritage.model.tableperclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entité concrète Livre stockée dans sa propre table 'livres'.
 *
 * @author Ali Mahraoui
 */
@Entity
@Table(name = "livres")
public class Livre extends Produit {

    @Column(nullable = false, length = 100)
    private String auteur;

    public Livre() {
        super();
    }

    public Livre(String nom, String auteur) {
        super(nom);
        this.auteur = auteur;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    @Override
    public String toString() {
        return String.format("Livre [id=%d, nom='%s', auteur='%s']",
                getId(), getNom(), auteur);
    }
}
