package ma.ali.mahraoui.heritage.model.joined;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entité racine illustrant la stratégie JOINED (table employes reliée par jointure aux tables filles).
 *
 * @author Ali Mahraoui
 */
@Entity
@Table(name = "employes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nom;

    public Employe() {
        super();
    }

    public Employe(String nom) {
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
