# Modular Monolith E-Commerce Application

A robust, scalable, and modular monolith e-commerce system built with **Java 17**, **Spring Boot 3**, and **MySQL**. This project demonstrates the power of a modular architecture where distinct business domains are separated within a single application codebase, allowing for easy transitions to microservices if needed.

---

## 🏗️ Architecture Overview

This application follows the **Modular Monolith** pattern. Each module represents a specific business domain, encapsulated with its own controllers, services, repositories, and entities.

### Key Benefits:
- **Separation of Concerns**: Each module handles its own logic.
- **Maintainability**: Clearer boundaries between different parts of the system.
- **Scalability**: Can be easily split into microservices in the future.

---

## 📦 Modules Breakdown

The system is divided into several key modules:

- **🔐 Auth Module**: Handles secure login, registration, and JWT-based authentication using **Spring Security**.
- **👤 User Module**: Manages user profiles, roles, and administrative tasks.
- **🛒 Product Module**: Manages product catalog, search, and inventory updates.
- **📦 Order Module**: Handles the creation and management of customer orders.
- **💳 Payment Module**: Simulates secure payment processing and transaction status.
- **🔔 Notification Module**: Sends system-wide notifications for order updates and alerts.

---

## 🛠️ Technologies Used

### Backend:
- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Data JPA** (Hibernate)
- **Spring Security & JWT**
- **MySQL Database**
- **Lombok** (for cleaner code)
- **Maven** (Build Tool)

### Frontend:
- **Vanilla HTML5, CSS3, & JavaScript** (Single-Page Application architecture)
- **Modern Responsive Design** (Glassmorphism, smooth animations)

---

## 🚀 Getting Started

### Prerequisites:
- **JDK 17** or higher
- **Maven 3.x**
- **MySQL Server**

### Step 1: Database Configuration
1. Create a MySQL database named `modular_monolith`:
   ```sql
   CREATE DATABASE modular_monolith;
   ```
2. Update the `src/main/resources/application.yml` file with your MySQL credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/modular_monolith
       username: your_username
       password: your_password
   ```

### Step 2: Build & Run
1. From the root directory, build the project:
   ```bash
   mvn clean install
   ```
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

---

## 🌐 Accessing the Application

- **Frontend Interface**: Access the web-based e-commerce platform at `http://localhost:8080/index.html`.
- **API Base URL**: `http://localhost:8080/api`

---

## 🔐 Security Information

The application uses **JWT (JSON Web Token)** for secure authentication. Upon successful login, the token must be included in the `Authorization` header (`Bearer <token>`) for subsequent requests to protected endpoints.

---

## 📄 License
TBA

---

*Made with ❤️ for Modern Web Development*
