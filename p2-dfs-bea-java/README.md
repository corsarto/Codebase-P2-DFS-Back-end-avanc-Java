# p2-dfs-bea-java

Starter code for the OpenClassrooms P2 — Personal Finance Tracker (backend, Java).

Important constraints and choices:
- Java target: 25 (forced by user request)
- Spring Boot: 4.0.0 (forced, user requested 4.0.x)
- Build: Maven
- Console application only (no REST endpoints, no web UI, no DB)
- Persistence: local CSV files (UTF-8)
- Language: French documentation; code identifiers in English

Note on Java / Spring Boot versions
- You requested to force Java 25 and Spring Boot 4.0.x. This project sets Java 25 and Spring Boot 4.0.0 in pom.xml.
- I cannot verify remote release notes from this environment. Please verify locally that Spring Boot 4.0.0 (or the exact 4.0.x you choose) is compatible with JDK 25 by consulting the official Spring Boot release notes and the JDK compatibility matrix before using this configuration in production.
- If you want a specific 4.0.x patch (for example 4.0.1), replace the parent version in `pom.xml` with that exact value.

Quick start

Prerequisites
- JDK 25 (set JAVA_HOME accordingly)
- Maven 3.8+

Build
- mvn -v
- mvn clean package

Run (console)
- mvn spring-boot:run

Or run the generated jar:
- java -jar target/p2-dfs-bea-java-0.1.0-SNAPSHOT.jar

Project structure

p2-dfs-bea-java/
├── pom.xml
├── README.md
├── .gitignore
├── src/
│   ├── main/
│   │   ├── java/com/openclassrooms/p2dfsbea/
│   │   │   ├── P2DfsBeaApplication.java
│   │   │   ├── model/Expense.java
│   │   │   ├── repository/ExpenseRepository.java
│   │   │   ├── service/ExpenseService.java
│   │   │   └── ui/ConsoleMenu.java
│   │   └── resources/
│   │       ├── emma_expenses.csv
│   │       └── personnal_expenses.csv
│   └── test/
│       └── java/com/openclassrooms/p2dfsbea/ExpenseServiceTest.java
└── agent.md

CSV files
- src/main/resources/emma_expenses.csv
- src/main/resources/personnal_expenses.csv

Commands de test
- mvn test

Notes pédagogiques
- Les messages console en anglais reproduisent le original Python project behavior; documentation est fournie en français.
- Use BigDecimal for money, LocalDate for dates, DateTimeFormatter "dd/MM/yyyy".

What I did
- Updated pom.xml to force Java 25 and Spring Boot 4.0.0 (user request).
- Replaced deprecated BigDecimal rounding constants by RoundingMode.HALF_UP where needed.

Important follow-up
- Run the build and tests locally and paste any errors here if present. I cannot run Maven in this environment.

