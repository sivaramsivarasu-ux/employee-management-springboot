# Employee Management System - Spring Boot Backend

Enterprise REST API backend built with **Spring Boot 3**, **Spring Data JPA**, and **MySQL** to manage employee information, shift attendance, and payroll.

---

## 📌 Architecture & Features

- **RESTful Endpoints:**
  - `GET /api/employees`: Show active employees (`status = 1`).
  - `POST /api/employees`: Register new employee (`employeeID`, `name`, `timein`, `timeout`, `salary`).
  - `GET /api/employees/search?query=...`: Search by ID or partial/full name (e.g., searching "John" returns all matching records).
  - `PUT /api/employees/{id}`: Edit and update employee information in MySQL.
  - `DELETE /api/employees/{id}?permanent=false`: Soft delete (`status = 0`) or permanent hard delete.
  - `DELETE /api/employees`: Permanently delete all employee records.
  - `GET /api/employees/inactive`: View soft-deleted employees (Recycle Bin).
  - `POST /api/employees/{id}/restore`: Restore soft-deleted employee back to active (`status = 1`).
- **CORS Configured:** Pre-configured for React / frontend clients.
- **Hibernate SQL Logging:** Prints executed SQL queries in real-time.

---

## 🚀 How to Run

### 1. Prerequisites
- Java JDK 25 (the current LTS release)
- Apache Maven
- MySQL Server 8.0 running on port 3306 with database `employee_db`

### 2. Configure Database
Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Build & Run

On Windows, ensure Java 25 or later and Maven are available in your environment, then run the supplied script from PowerShell or Command Prompt:
```powershell
.\run-app.cmd
```

For VS Code debugging, use **Run and Debug** and select **Run Employee Management Spring Boot**.

Alternatively, if `JAVA_HOME` points to JDK 25 or later:
```bash
mvn clean spring-boot:run
```
The REST API will be available at: `http://localhost:8080/api/employees`
