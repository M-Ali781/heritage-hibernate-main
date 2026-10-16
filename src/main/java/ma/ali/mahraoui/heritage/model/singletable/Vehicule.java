package ma.ali.mahraoui.heritage.model.singletable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entité racine illustrant la stratégie SINGLE_TABLE (table unique pour toute la hiérarchie).
 *
 * @author Ali Mahraoui
 */
@Entity
@Table(name = "vehicules", indexes = {
        @Index(name = "idx_vehicule_type", columnList = "vehicule_type")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicule_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String marque;

    public Vehicule() {
        super();
    }

    public Vehicule(String marque) {
        this.marque = marque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }
}
