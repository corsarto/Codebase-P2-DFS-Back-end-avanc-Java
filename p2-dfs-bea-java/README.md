# p2-dfs-bea-java

Starter code for the OpenClassrooms P2 — Personal Finance Tracker (backend, Java).

Important constraints and choices:
- Java target: 21 (see note below)
- Spring Boot: 3.1.7
- Build: Maven
- Console application only (no REST endpoints, no web UI, no DB)
- Persistence: local CSV files (UTF-8)
- Language: French documentation; code identifiers in English

Note on Java / Spring Boot versions
- The original requirement requested Java 25 and Spring Boot 4.0+ if compatible.
- As of the time this starter was created the compatibility of Spring Boot 4.0 with Java 25 could not be safely verified by this assistant. To provide a stable, buildable starter without inventing dependency versions, this project uses Java 21 and Spring Boot 3.1.7 which are known-compatible (documented at creation time).
- If you specifically require Java 25 and Spring Boot 4.x, you should verify official Spring Boot release notes and update the pom.xml accordingly.

Quick start

Prerequisites
- JDK 21
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
- Created a Maven Spring Boot starter project that runs as a console application.
- Implemented basic CSV reading/writing, a menu, service skeleton and tests.

Validation
I cannot run Maven or tests from this environment. Please run locally:
1. mvn clean test
2. mvn spring-boot:run

If you find build errors I will fix them — provide the mvn output and I will iterate.
