# Stack : Java 25 + Spring Boot 4.0 + Maven

> Standard technique cible pour la transposition du projet Python P2 DFS BEA.

## 1. Stack technique

- **Langage** : Java 25
- **Framework / Core** : Spring Boot 4.0 ou version ultérieure compatible avec Java 25
- **Type de projet** : application console, sans API REST ni interface web
- **Build / dépendances** : Maven
- **Persistance** : fichiers CSV locaux, sans base de données
- **Tests** : JUnit 5 via Spring Boot Starter Test si nécessaire

## 2. Règles d'implémentation

- Conserver les fonctionnalités, la progression pédagogique et les sorties console de la version Python.
- Utiliser un point d'entrée Java avec `SpringApplication` et un composant `CommandLineRunner` pour lancer l'application console.
- Séparer progressivement les responsabilités : lecture/écriture CSV, calculs métier et interaction console.
- Utiliser `BigDecimal` pour les montants financiers et `LocalDate` pour les dates.
- Conserver le format de date `dd/MM/yyyy` et le format CSV `date,category,description,amount`.
- Conserver les fichiers `emma_expenses.csv` et `personnal_expenses.csv` comme assets de données du projet.
- Utiliser des noms de classes, méthodes et variables en anglais comme dans le code source, avec une documentation pédagogique en français.

## 3. Pratiques interdites

- Ne pas transformer le projet en API REST, application web ou application avec base de données.
- Ne pas remplacer les CSV par une persistance externe.
- Ne pas utiliser `double` ou `float` pour les montants financiers.
- Ne pas modifier arbitrairement les catégories, les calculs, le menu ou les messages attendus.
- Ne pas ajouter de framework ou de fonctionnalité non nécessaire au niveau pédagogique du projet.

## 4. Mapping de migration (Python -> Java)

| Concept Python | Concept Java | Implémentation |
| :--- | :--- | :--- |
| Script `main.py` | Classe principale Spring Boot | `@SpringBootApplication` + `CommandLineRunner` |
| Fonctions globales | Méthodes de classes | Services ou composants dédiés |
| `list` de dictionnaires CSV | `List<Expense>` | Classe modèle `Expense` |
| Dictionnaire de totaux | `Map<String, BigDecimal>` | `HashMap` ou `Map` |
| `csv.DictReader` / `csv.writer` | API Java NIO ou bibliothèque CSV | Lecture et écriture UTF-8 avec en-tête |
| `float` | `BigDecimal` | Calculs exacts et formatage à deux décimales |
| `datetime` | `LocalDate` / `LocalDateTime` | `DateTimeFormatter` avec `dd/MM/yyyy` |
| `input()` | `Scanner` ou `BufferedReader` | Saisie console |
| `print()` | `System.out.println()` | Affichage console |
| `math.ceil()` | `Math.ceil()` | Calcul du nombre entier de mois |
| `set` | `Set<String>` | Comptage des mois distincts |
| Exceptions Python | Exceptions Java | Gestion des erreurs de saisie et de fichiers |
| Tests Python adaptés | Tests JUnit 5 | Tests unitaires des calculs et tests ciblés de l'I/O |
