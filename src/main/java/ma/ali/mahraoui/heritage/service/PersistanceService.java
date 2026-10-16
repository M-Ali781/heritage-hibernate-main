package ma.ali.mahraoui.heritage.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Service générique de gestion de la persistance JPA avec gestion automatique des transactions.
 * Fournit les opérations CRUD de base sur les hiérarchies d'entités.
 *
 * @author Ali Mahraoui
 */
public class PersistanceService implements AutoCloseable {

    private static final String DEFAULT_PERSISTENCE_UNIT = "heritagePU";
    private final EntityManagerFactory emf;

    public PersistanceService() {
        this(Collections.emptyMap());
    }

    public PersistanceService(Map<String, ?> customConfig) {
        this.emf = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT, customConfig);
    }

    /**
     * Exécute une opération au sein d'une transaction JPA isolée avec rollback automatique en cas d'erreur.
     */
    public <T> T transaction(Function<EntityManager, T> operation) {
        Objects.requireNonNull(operation, "L'opération transactionnelle ne peut être nulle");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = operation.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                try {
                    tx.rollback();
                } catch (RuntimeException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
            }
            throw ex;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Persiste une liste d'entités dans la base de données.
     */
    public void creer(Object... entites) {
        transaction(em -> {
            for (Object entite : entites) {
                if (entite != null) {
                    em.persist(entite);
                }
            }
            return null;
        });
    }

    /**
     * Recherche une entité par sa classe et son identifiant unique.
     */
    public <T> T trouver(Class<T> type, Long id) {
        Objects.requireNonNull(type, "Le type d'entité ne peut pas être nul");
        if (id == null) {
            return null;
        }
        return transaction(em -> em.find(type, id));
    }

    /**
     * Récupère la liste ordonnée de toutes les entités du type spécifié.
     */
    public <T> List<T> lister(Class<T> type) {
        Objects.requireNonNull(type, "Le type d'entité ne peut pas être nul");
        return transaction(em -> {
            String entityName = em.getMetamodel().entity(type).getName();
            String jpql = "SELECT e FROM " + entityName + " e ORDER BY e.id ASC";
            return em.createQuery(jpql, type).getResultList();
        });
    }

    /**
     * Met à jour une entité existante en base.
     */
    public <T> T modifier(T entite) {
        Objects.requireNonNull(entite, "L'entité à modifier ne peut pas être nulle");
        return transaction(em -> em.merge(entite));
    }

    /**
     * Supprime une entité en base de données par son identifiant.
     */
    public <T> void supprimer(Class<T> type, Long id) {
        Objects.requireNonNull(type, "Le type d'entité ne peut pas être nul");
        Objects.requireNonNull(id, "L'identifiant est obligatoire pour la suppression");
        transaction(em -> {
            T found = em.find(type, id);
            if (found == null) {
                throw new IllegalArgumentException("Entité introuvable pour suppression avec l'id : " + id);
            }
            em.remove(found);
            return null;
        });
    }

    @Override
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
