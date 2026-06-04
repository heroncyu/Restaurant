# CrousTycoon — Simulateur de Gestion de Restaurant

**CrousTycoon** est un jeu de gestion de restaurant en temps réel développé en **Java**. Dans ce simulateur, le joueur incarne un gérant qui doit faire prospérer son établissement jour après jour, en gérant son personnel, ses stocks, ses finances et la satisfaction de ses clients.

Ce projet a été réalisé en trinôme sur une période de 4 mois dans le cadre de l'UE "Génie Logiciel" de notre deuxième année de Licence Informatique à CY Cergy Paris Université.

![Capture d'écran de l'interface du jeu](https://heroncyu.alwaysdata.net/images/glp.jpg)
*Aperçu de la phase de service en temps réel.*

## Fonctionnalités principales (Gameplay)

Le jeu se divise en deux phases distinctes :
* **La phase de service (Jour) :** Simulation en temps réel. Les clients arrivent, s'installent, commandent et mangent. Le joueur doit gérer les alertes de stocks, la propreté de la salle, et peut étendre son restaurant (mode construction) ou acheter des meubles.
* **La phase de gestion (Nuit) :** Affichage d'un bilan financier détaillé (courbes de bénéfices, dépenses, salaires) permettant de prendre des décisions stratégiques pour le lendemain.

## Architecture et Choix Techniques

Ce projet a été conçu avec une approche rigoureuse d'ingénierie logicielle pour garantir un code maintenable et évolutif :

* **Architecture MVC :** Séparation stricte entre le Modèle (logique métier, boucle de jeu), la Vue (affichage graphique avec **Java Swing**) et le Contrôleur (gestion des clics et événements).
* **Design Patterns :**
  * *Factory* : Création dynamique des différents types de clients (Standard, Star, Critique).
  * *Singleton* : Gestion centralisée des dépôts de données (ArgentRepository, StockRepository).
  * *Strategy* : Délégation du rendu visuel de la grille (PaintStrategy).
* **Outils & Bibliothèques externes :**
  * **JFreeChart :** Génération des graphiques financiers dans le bilan de fin de journée.
  * **Log4j 2 :** Traçabilité des événements et débogage.
  * **JUnit 5 :** Tests unitaires validant la logique complexe (machine à états des entités, calculs économiques).

---

## Guide d'installation et d'exécution (sous Eclipse)

### Prérequis
Le jeu a été développé pour être compilé et exécuté sous **Java 8 (JRE / JDK 1.8)**. Assurez-vous que votre environnement Eclipse utilise cette version.

### 1. Importer le projet
1. Dans Eclipse, allez dans `File` > `Import...`.
2. Déroulez `General` > `Existing Projects into Workspace` > `Next`.
3. Cliquez sur `Browse...` (Select root directory) et sélectionnez le dossier racine du projet.
4. Assurez-vous que le projet est coché puis cliquez sur `Finish`.

### 2. Configurer les dépendances (Log4j, JFreeChart, JUnit)
*Le projet utilise des librairies externes situées dans le dossier `lib/`.*
1. Faites un clic droit sur le projet > `Build Path` > `Configure Build Path...`.
2. Onglet `Libraries` :
   * **Pour Log4j et JFreeChart :** Cliquez sur `Add External JARs...` et sélectionnez les fichiers `.jar` associés.
   * **Pour JUnit :** S'il n'est pas configuré, cliquez sur `Add Library...` > `JUnit` > `Finish`.
3. Cliquez sur `Apply and Close`.

### 3. Lancer le jeu
1. Dans le *Package Explorer*, ouvrez `src` > package `app`.
2. Faites un clic droit sur `Main.java` > `Run As` > `Java Application`.

### 4. Lancer les tests unitaires
1. Ouvrez le dossier `src` > package `test`.
2. Clic droit sur le package `test` > `Run As` > `JUnit Test`.
3. La vue JUnit s'ouvrira (barre verte si tous les tests passent !).

## Auteurs
* **EL HAJAM Ayoub**
* **HERON Sajid**
* **BOUSSALEM Nassim**
