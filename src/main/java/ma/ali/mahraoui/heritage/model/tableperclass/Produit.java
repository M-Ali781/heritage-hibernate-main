package ma.ali.mahraoui.heritage.model.tableperclass;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entité racine abstraite pour la stratégie TABLE_PER_CLASS.
 * Aucune table 'produit' n'est créée ; seules les tables concrètes existent.
 *
 * @author Ali Mahraoui
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    public Produit() {
        super();
    }

    public Produit(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
