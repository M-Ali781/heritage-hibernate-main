package ma.ali.mahraoui.heritage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.EntityManager;
import ma.ali.mahraoui.heritage.model.joined.Developpeur;
import ma.ali.mahraoui.heritage.model.joined.Employe;
import ma.ali.mahraoui.heritage.model.joined.Manager;
import ma.ali.mahraoui.heritage.model.singletable.Moto;
import ma.ali.mahraoui.heritage.model.singletable.Vehicule;
import ma.ali.mahraoui.heritage.model.singletable.Voiture;
import ma.ali.mahraoui.heritage.model.tableperclass.Electronique;
import ma.ali.mahraoui.heritage.model.tableperclass.Livre;
import ma.ali.mahraoui.heritage.model.tableperclass.Produit;
import ma.ali.mahraoui.heritage.service.PersistanceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Suite de tests unitaires validant l'intégrité des 3 stratégies d'héritage JPA.
 *
 * @author Ali Mahraoui
 */
public class HeritageJpaTest {

    private PersistanceService persistanceService;

    @Before
    public void initialiserEnvironnementTest() {
        Map<String, Object> testProperties = new HashMap<>();
        testProperties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:test_" + UUID.randomUUID());
        testProperties.put("hibernate.show_sql", "false");
        persistanceService = new PersistanceService(testProperties);
    }

    @After
    public void nettoyerEnvironnementTest() {
        if (persistanceService != null) {
            persistanceService.close();
        }
    }

    private long executerCompteurSql(EntityManager entityManager, String sqlQuery) {
        Number compte = (Number) entityManager.createNativeQuery(sqlQuery).getSingleResult();
        return compte.longValue();
    }

    @Test
    public void testCrudStrategieSingleTable() {
        Voiture voitureTest = new Voiture("Renault Clio", 5);
        Moto motoTest = new Moto("Honda CB500", false);

        persistanceService.creer(voitureTest, motoTest);

        assertNotNull("L'id de la voiture ne doit pas être nul après persistance", voitureTest.getId());
        assertNotNull("L'id de la moto ne doit pas être nul après persistance", motoTest.getId());
        assertNotEquals("Les identifiants doivent être uniques", voitureTest.getId(), motoTest.getId());

        List<Vehicule> vehicules = persistanceService.lister(Vehicule.class);
        assertEquals(2, vehicules.size());
        assertTrue(persistanceService.trouver(Vehicule.class, voitureTest.getId()) instanceof Voiture);
        assertTrue(persistanceService.trouver(Vehicule.class, motoTest.getId()) instanceof Moto);
        assertEquals(1, persistanceService.lister(Voiture.class).size());

        // Modification
        voitureTest.setNombrePortes(3);
        persistanceService.modifier(voitureTest);
        assertEquals(3, persistanceService.trouver(Voiture.class, voitureTest.getId()).getNombrePortes());

        // Suppression
        persistanceService.supprimer(Vehicule.class, motoTest.getId());
        assertNull(persistanceService.trouver(Vehicule.class, motoTest.getId()));
        assertNull(persistanceService.trouver(Moto.class, motoTest.getId()));
        assertEquals(1, persistanceService.lister(Vehicule.class).size());

        persistanceService.supprimer(Vehicule.class, voitureTest.getId());
        assertTrue(persistanceService.lister(Vehicule.class).isEmpty());
    }

    @Test
    public void testCrudStrategieJoined() {
        Developpeur dev = new Developpeur("Ali Mahraoui", "Java");
        Manager mgr = new Manager("Responsable Technique", 2);

        persistanceService.creer(dev, mgr);

        assertNotNull(dev.getId());
        assertNotNull(mgr.getId());
        assertNotEquals(dev.getId(), mgr.getId());

        List<Employe> employes = persistanceService.lister(Employe.class);
        assertEquals(2, employes.size());
        assertTrue(persistanceService.trouver(Employe.class, dev.getId()) instanceof Developpeur);
        assertTrue(persistanceService.trouver(Employe.class, mgr.getId()) instanceof Manager);
        assertEquals(1, persistanceService.lister(Developpeur.class).size());

        // Modification
        dev.setLangage("Kotlin");
        persistanceService.modifier(dev);
        assertEquals("Kotlin", persistanceService.trouver(Developpeur.class, dev.getId()).getLangage());

        // Suppression
        persistanceService.supprimer(Employe.class, mgr.getId());
        assertNull(persistanceService.trouver(Employe.class, mgr.getId()));
        assertNull(persistanceService.trouver(Manager.class, mgr.getId()));
        assertEquals(1, persistanceService.lister(Employe.class).size());

        persistanceService.supprimer(Employe.class, dev.getId());
        assertTrue(persistanceService.lister(Employe.class).isEmpty());
    }

    @Test
    public void testCrudStrategieTablePerClass() {
        Livre livreTest = new Livre("Antigone", "Jean Anouilh");
        Electronique ordiTest = new Electronique("Ordinateur", 24);

        persistanceService.creer(livreTest, ordiTest);

        assertNotNull(livreTest.getId());
        assertNotNull(ordiTest.getId());
        assertNotEquals(livreTest.getId(), ordiTest.getId());

        List<Produit> produits = persistanceService.lister(Produit.class);
        assertEquals(2, produits.size());
        assertTrue(persistanceService.trouver(Produit.class, livreTest.getId()) instanceof Livre);
        assertTrue(persistanceService.trouver(Produit.class, ordiTest.getId()) instanceof Electronique);
        assertEquals(1, persistanceService.lister(Livre.class).size());

        // Modification
        livreTest.setAuteur("Nouvel Auteur");
        persistanceService.modifier(livreTest);
        assertEquals("Nouvel Auteur", persistanceService.trouver(Livre.class, livreTest.getId()).getAuteur());

        // Suppression
        persistanceService.supprimer(Produit.class, ordiTest.getId());
        assertNull(persistanceService.trouver(Produit.class, ordiTest.getId()));
        assertNull(persistanceService.trouver(Electronique.class, ordiTest.getId()));
        assertEquals(1, persistanceService.lister(Produit.class).size());

        persistanceService.supprimer(Produit.class, livreTest.getId());
        assertTrue(persistanceService.lister(Produit.class).isEmpty());
    }

    @Test
    public void testVerificationSchemaTableUniqueEtDiscriminant() {
        persistanceService.creer(new Voiture("Renault", 5), new Moto("Yamaha", false));

        persistanceService.transaction(em -> {
            assertEquals(2, executerCompteurSql(em, "SELECT COUNT(*) FROM vehicules"));
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM vehicules WHERE vehicule_type='VOITURE' AND nombrePortes=5 AND sidecar IS NULL"));
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM vehicules WHERE vehicule_type='MOTO' AND sidecar=FALSE AND nombrePortes IS NULL"));
            assertEquals(0, executerCompteurSql(em, "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME IN ('VOITURE', 'MOTO')"));
            return null;
        });
    }

    @Test
    public void testVerificationSchemaJointuresEtSuppressionCascade() {
        Developpeur dev = new Developpeur("Ali Mahraoui", "Java");
        Manager mgr = new Manager("Direction", 2);

        persistanceService.creer(dev, mgr);

        persistanceService.transaction(em -> {
            assertEquals(2, executerCompteurSql(em, "SELECT COUNT(*) FROM employes"));
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM employes e JOIN developpeurs d ON d.id=e.id WHERE d.langage='Java'"));
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM employes e JOIN managers m ON m.id=e.id WHERE m.nombreEquipes=2"));
            return null;
        });

        persistanceService.supprimer(Employe.class, mgr.getId());

        persistanceService.transaction(em -> {
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM employes"));
            assertEquals(0, executerCompteurSql(em, "SELECT COUNT(*) FROM managers"));
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM developpeurs"));
            return null;
        });
    }

    @Test
    public void testVerificationSchemaTablesConcretesIndependantes() {
        Livre livre = new Livre("Antigone", "Jean Anouilh");
        Electronique appareil = new Electronique("Ordinateur", 24);

        persistanceService.creer(livre, appareil);

        assertNotEquals(livre.getId(), appareil.getId());

        persistanceService.transaction(em -> {
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM livres WHERE nom='Antigone' AND auteur='Jean Anouilh'"));
            assertEquals(1, executerCompteurSql(em, "SELECT COUNT(*) FROM electroniques WHERE nom='Ordinateur' AND garantie=24"));
            assertEquals(0, executerCompteurSql(em, "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='PRODUIT'"));
            return null;
        });

        assertEquals(2, persistanceService.lister(Produit.class).size());
    }

    @Test
    public void testAnnulationTransactionRollbackEtIsolation() {
        assertThrows(RuntimeException.class, () -> persistanceService.creer(
                new Voiture("Dacia", 5), new Developpeur(null, "Java")));

        assertTrue(persistanceService.lister(Vehicule.class).isEmpty());
        assertTrue(persistanceService.lister(Employe.class).isEmpty());

        persistanceService.creer(new Manager("Directeur Général", 2));
        assertEquals(1, persistanceService.lister(Employe.class).size());
    }
}
