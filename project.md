# P2 DFS BEA — Suivi de dépenses en Java

> Instructions fournies à l'étudiant.

---

## Contexte

Vous venez de rejoindre une startup fintech en tant que développeur·se back-end junior. L'entreprise développe une application qui aide les jeunes actifs de 20 à 30 ans à mieux gérer leur argent : suivre leur budget, épargner et commencer à investir.

Votre manageuse, Mai, vous accompagne dans vos premiers pas. Pour votre première mission, vous allez prendre en main une application console existante de suivi des dépenses, puis y ajouter des fonctionnalités simples.

---

## Objectifs pédagogiques

Ce projet vous permettra de :

- mettre en place un environnement Java 25 avec Maven et Spring Boot 4.0 ;
- cloner, exécuter et versionner un projet Java ;
- lire et écrire des données dans un fichier CSV ;
- manipuler des collections, des chaînes et des dates en Java ;
- implémenter des calculs de dépenses, de budget et de capacité d'emprunt ;
- pratiquer progressivement la programmation orientée objet et la séparation des responsabilités ;
- écrire et exécuter des tests JUnit 5 ;
- utiliser l'IA de manière autonome et critique.

---

## Scénario

Le projet est un fil rouge en deux parties, avec un accompagnement IA tout au long du parcours.

- **Partie 1 — Mettez en place le back-end d'une application** : installez vos outils, récupérez le dépôt de départ et faites fonctionner l'application Java en local.
- **Partie 2 — Implémentez des fonctionnalités** : explorez le starter code, manipulez les données CSV, ajoutez les fonctionnalités demandées et pratiquez la POO.

Les frameworks et les applications structurées seront approfondis dans les projets suivants. Ici, le code doit rester simple, clair et adapté aux fondamentaux.

---

## Exercices

### Exercice 1 — Mettez en place le back-end d'une application

Exercice fil rouge — Partie 1. Vous devez mettre en place votre environnement de développement, votre dépôt Git et l'application console Java.

#### Votre mission

- Installer et configurer Java 25, Maven, Spring Boot 4.0, votre IDE, Git et les extensions utiles.
- Cloner le dépôt de départ fourni et l'ouvrir dans votre IDE.
- Exécuter l'application localement et vérifier le message de bienvenue.
- Créer une branche `develop` et pousser le dépôt dans votre espace GitHub.
- Préparer une courte démonstration pour votre mentor.

#### Résultat attendu

L'application démarre sans erreur avec la commande Maven adaptée et affiche le message de bienvenue prévu. Votre dépôt est cloné, versionné, poussé sur GitHub et contient une branche `develop`.

### Exercice 2 — Implémentez des fonctionnalités avec les fondamentaux de Java

Exercice fil rouge — Partie 2. Vous reprenez votre dépôt pour comprendre le starter code et implémenter les premières fonctionnalités de l'application de suivi de dépenses.

#### Votre mission

- Explorer le code jusqu'à pouvoir expliquer le point d'entrée, les classes principales et leur rôle.
- Exécuter le projet et observer les sorties console.
- Manipuler le fichier [PLACEHOLDER ASSETS] contenant les données d'exemple.
- Implémenter progressivement les fonctionnalités demandées : historique, ajout d'une dépense, statistiques par catégorie, plan d'achat d'une voiture et capacité d'emprunt immobilier.
- Utiliser `BigDecimal` pour les montants et `LocalDate` pour les dates.
- Ajouter ou adapter les tests JUnit 5 nécessaires et corriger les erreurs simples.
- Fusionner `develop` dans `main` sans supprimer `develop`.
- Préparer une démonstration et une explication orale pour votre mentor.

#### Fonctionnalités attendues

- afficher l'historique des dépenses et le total ;
- ajouter une dépense datée du jour dans le fichier CSV ;
- afficher les totaux, moyennes mensuelles et parts par catégorie ;
- calculer le nombre de mois nécessaires pour acheter une voiture ;
- calculer une capacité d'emprunt selon un paiement mensuel, un taux et une durée ;
- appliquer la limite d'endettement de 35 % des revenus pour le prêt immobilier.

#### Points de vigilance

- Conserver le format CSV `date,category,description,amount` et le format de date `dd/MM/yyyy`.
- Vérifier les résultats console et les calculs avant de poursuivre.
- Privilégier la simplicité, la lisibilité et la séparation des responsabilités.
- Ne pas transformer cette application console en API REST ou en application web.

---

## Livrables

- Un dépôt GitHub Java complet, avec un historique de commits lisible.
- Une branche `develop` conservée et fusionnée dans `main`.
- Une application console Java 25 fonctionnelle, construite avec Maven et Spring Boot 4.0.
- Les fonctionnalités demandées implémentées et testées.
- Une démonstration live de l'IDE, du dépôt, du code et des résultats.
- Une explication de la structure, des choix réalisés, des difficultés rencontrées et des corrections apportées.
