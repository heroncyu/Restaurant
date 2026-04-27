====================================================================
           GUIDE D'EXÉCUTION DU PROJET CROUSTYCOON (RESTAURANT)
                            SOUS ECLIPSE
====================================================================

Ce document explique comment importer et lancer le jeu de gestion de restaurant dans l'environnement de développement Eclipse.

PRÉREQUIS IMPORTANT
-------------------
Le jeu a été développé pour être compilé et exécuté spécifiquement sous Java 8 (JRE / JDK 1.8). Assurez-vous que votre environnement Eclipse utilise bien cette version pour éviter toute erreur de compatibilité.

1. IMPORTER LE PROJET
---------------------
  a) Ouvrez Eclipse.
  b) Dans la barre de menu supérieure, cliquez sur "File" puis sur "Import...".
  c) Déroulez le dossier "General" et sélectionnez "Existing Projects into Workspace".
  d) Cliquez sur "Next".
  e) Cliquez sur "Browse..." à côté de "Select root directory" et sélectionnez le dossier racine du projet (le dossier qui contient src/, bin/, etc.).
  f) Assurez-vous que le projet est bien coché dans la liste "Projects" puis cliquez sur "Finish".

2. VÉRIFIER LES DÉPENDANCES (Log4j, JFreeChart, JUnit)
------------------------------------------------------
  Le projet utilise des librairies externes. Si vous rencontrez des erreurs (petites croix rouges sur les fichiers) :
  a) Faites un clic droit sur le dossier du projet dans le "Package Explorer" à gauche.
  b) Allez dans "Build Path" > "Configure Build Path...".
  c) Allez dans l'onglet "Libraries".
  d) Pour Log4j et JFreeChart : cliquez sur "Add External JARs..." et sélectionnez les fichiers .jar respectifs.
  e) Pour JUnit : s'il n'est pas déjà configuré, cliquez sur "Add Library...", sélectionnez "JUnit", choisissez la version et cliquez sur "Finish".
  f) Cliquez enfin sur "Apply and Close".

3. LANCER LE JEU
----------------
  Le point d'entrée principal de l'application est situé dans le package "app".
  
  a) Dans le "Package Explorer", ouvrez le dossier "src" puis le package "app".
  b) Repérez le fichier nommé "Main.java".
  c) Faites un clic droit sur ce fichier.
  d) Allez dans "Run As" > "Java Application".

Le jeu devrait alors se compiler et la fenêtre principale de CrousTycoon va s'ouvrir. 

4. EXÉCUTER LES TESTS (JUnit)
-----------------------------
  Le projet contient plusieurs tests unitaires pour vérifier le bon fonctionnement du moteur.
  
  a) Dans le "Package Explorer", ouvrez le dossier "src" puis le package "test" (ou "test.unit" s'il existe).
  b) Vous y trouverez des fichiers comme TestSimulation.java, TestDayStatistics.java, etc.
  c) Pour lancer tous les tests d'un coup, faites un clic droit sur le package "test".
  d) Allez dans "Run As" > "JUnit Test".
  e) La vue JUnit s'ouvrira dans Eclipse avec une barre verte si tout fonctionne correctement.

Bon jeu !
