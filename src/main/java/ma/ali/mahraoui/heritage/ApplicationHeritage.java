package ma.ali.mahraoui.heritage;

import java.util.Arrays;
import ma.ali.mahraoui.heritage.model.singletable.Moto;
import ma.ali.mahraoui.heritage.model.singletable.Vehicule;
import ma.ali.mahraoui.heritage.model.singletable.Voiture;
import ma.ali.mahraoui.heritage.model.joined.Developpeur;
import ma.ali.mahraoui.heritage.model.joined.Employe;
import ma.ali.mahraoui.heritage.model.joined.Manager;
import ma.ali.mahraoui.heritage.model.tableperclass.Electronique;
import ma.ali.mahraoui.heritage.model.tableperclass.Livre;
import ma.ali.mahraoui.heritage.model.tableperclass.Produit;
import ma.ali.mahraoui.heritage.service.PersistanceService;
import org.h2.tools.Server;

/**
 * Point d'entrée principal de l'application de démonstration JPA & Hibernate.
 * Illustre les trois stratégies fondamentales d'héritage objet-relationnel.
 *
 * @author Ali Mahraoui
 */
public class ApplicationHeritage {

    public static void main(String[] args) throws Exception {
        System.out.println("======================================================================");
        System.out.println("  TP JPA / HIBERNATE - STRATÉGIES D'HÉRITAGE (SINGLE_TABLE, JOINED, TPC)");
        System.out.println("  Auteur : Ali Mahraoui");
        System.out.println("======================================================================\n");

        try (PersistanceService service = new PersistanceService()) {
            // 1. Initialisation des entités
            Voiture voiture = new Voiture("Peugeot 208", 5);
            Moto moto = new Moto("Kawasaki Z900", false);

            Developpeur developpeur = new Developpeur("Ali Mahraoui", "Java & Spring");
            Manager manager = new Manager("Fatima Zahra", 3);

            Livre livre = new Livre("Le Petit Prince", "Antoine de Saint-Exupéry");
            Electronique ordinateur = new Electronique("MacBook Pro M3", 36);

            // 2. Persistance
            System.out.println(">>> 1. Insertion des entités de test en base de données...");
            service.creer(voiture, moto, developpeur, manager, livre, ordinateur);
            afficherEtat(service);

            // 3. Mise à jour
            System.out.println("\n>>> 2. Mise à jour des entités dans chaque stratégie...");
            voiture.setNombrePortes(3);
            developpeur.setLangage("Java 17 & Hibernate 5");
            livre.setNom("Terre des Hommes");
            livre.setAuteur("Antoine de Saint-Exupéry");

            service.modifier(voiture);
            service.modifier(developpeur);
            service.modifier(livre);

            System.out.println("Véhicule modifié : " + service.trouver(Vehicule.class, voiture.getId()));
            System.out.println("Employé modifié  : " + service.trouver(Employe.class, developpeur.getId()));
            System.out.println("Produit modifié  : " + service.trouver(Produit.class, livre.getId()));

            // 4. Suppression polymorphique
            System.out.println("\n>>> 3. Suppression polymorphique via les classes parentes...");
            service.supprimer(Vehicule.class, moto.getId());
            service.supprimer(Employe.class, manager.getId());
            service.supprimer(Produit.class, ordinateur.getId());
            afficherEtat(service);

            // 5. Console Web H2 facultative
            if (Arrays.asList(args).contains("--console")) {
                Server webServer = Server.createWebServer("-webPort", "8082").start();
                try {
                    System.out.println("\n[Console H2 démarrée] -> http://localhost:8082");
                    System.out.println("URL JDBC  : jdbc:h2:mem:heritage");
                    System.out.println("Utilisateur: sa | Mot de passe: (aucun)");
                    System.out.println("Appuyez sur Entrée pour clore l'application...");
                    System.in.read();
                } finally {
                    webServer.stop();
                }
            }
        }

        System.out.println("\n[Démonstration terminée avec succès]");
    }

    private static void afficherEtat(PersistanceService service) {
        System.out.println("\n--- [SINGLE_TABLE] Véhicules (table unique avec discriminant) ---");
        service.lister(Vehicule.class).forEach(v -> System.out.println("  * " + v));

        System.out.println("\n--- [JOINED] Employés (tables filles jointes à la table mère) ---");
        service.lister(Employe.class).forEach(e -> System.out.println("  * " + e));

        System.out.println("\n--- [TABLE_PER_CLASS] Produits (tables concrètes indépendantes) ---");
        service.lister(Produit.class).forEach(p -> System.out.println("  * " + p));
    }
}
