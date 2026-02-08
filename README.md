# Hotel Reservation System 🏨

A robust Hotel Reservation System built with Pure Java, featuring JDBC integration and a custom REST server. This project demonstrates core backend development principles and architectural patterns.

---

## 🛠 Features & Requirements Covered

This project implements the following technical requirements:

### Core Java & Architecture
* **OOP Principles:** Full implementation of Encapsulation, Inheritance, Polymorphism, and Abstraction.
* **SOLID:** Strict adherence to principles, specifically focusing on **Dependency Inversion (DIP)**.
* **Layered Package Structure:** Clear separation of concerns (API, Service, DAO, Model).
* **Generics & Lambdas:** Used for clean and reusable code.
* **Reflection API:** Implementation of reflection for dynamic operations.

### Data Management
* **PostgreSQL + JDBC:** Database interaction using `PreparedStatement` for security and performance.
* **DataPool:** In-memory collection filtering, sorting, and searching.
* **Custom Exceptions:** At least 2 specialized exceptions for business-logic error handling.

### Design Patterns
1.  **Builder Pattern:** For flexible object construction.
2.  **Factory Pattern:** For centralized object instantiation.

### Networking
* **REST API:** Built using pure Java `HttpServer` with JSON support.

---

## 🚀 Quick Start

### 1. Database Setup
Create the database schema and (optionally) populate it with initial data:
* Execute: `sql/01_schema.sql`
* *(Optional)* Execute: `sql/02_seed.sql`

### 2. Configuration
Configure your database connection credentials in:
`src/main/java/kz/hotel/config/AppConfig.java`

### 3. Build and Run
Use Maven to package the project and run the JAR:

```bash
# Build the project
mvn -q -DskipTests package

# Run the application
java -jar target/HotelReservationSystem-1.0.0.jar
