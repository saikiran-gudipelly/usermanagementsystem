# 🚀 User Management Service – Spring Boot Backend

A production-style **User Management Backend Service** built using **Java, Spring Boot, and MongoDB**.

This project demonstrates clean backend architecture, RESTful API design, pagination, filtering, and production-ready development practices.

---

## 📌 Objective

The goal of this project is to build a scalable backend service for managing users while following:

- Layered architecture
- Clean code principles
- DTO-based API design
- Proper exception handling
- Pagination, filtering, and search

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data MongoDB
- MongoDB Atlas
- Gradle
- Lombok
- Jakarta Validation
- JUnit & Mockito

---

## 🧱 Project Architecture

```
Controller → Service → Repository → MongoDB
```

### 📂 Package Structure

```
com.usermanagementsystem
│
├── controller
├── service
├── serviceImpl
├── repository
├── model
├── dto
├── enums
└── exception
```

---

## ✨ Features

### Core APIs

- Create User
- Get User By ID
- Update User Status
- Delete User
- List Users

### Enhancements

- Pagination support
- Filtering by status
- Search by name/email
- Sorting by latest users
- DTO-based responses
- Global exception handling
- Validation annotations
- MongoDB auditing (createdAt, updatedAt)

---

## 📊 User Model

```
User
 ├── id
 ├── name
 ├── email
 ├── status
 ├── createdAt
 └── updatedAt
```

---

## 🔌 API Endpoints

### Create User
```
POST /api/users
```

### Get User
```
GET /api/users/{id}
```

### Update Status
```
PATCH /api/users/{id}/status
```

### List Users
```
GET /api/users?page=0&size=10&status=ACTIVE&search=sai
```

### Delete User
```
DELETE /api/users/{id}
```

---

## ⚙️ Setup

### 1️⃣ Clone Repository

```
git clone <repo-url>
```

### 2️⃣ Create `.env`

```
MONGO_URI=mongodb+srv://<username>:<password>@cluster.mongodb.net/user_management_db
```

`.env` is excluded from Git for security.

### 3️⃣ application.properties

```
spring.data.mongodb.uri=${MONGO_URI}
server.port=8080
```

### 4️⃣ Run Application

```
./gradlew bootRun
```

---

## 🧪 Testing

Unit tests implemented using:

- JUnit 5
- Mockito

Service layer logic is covered.

---

## 🛡️ Error Handling

Global exception handling using `@RestControllerAdvice`.

Handles:

- UserNotFoundException
- DuplicateUserException
- Validation errors

---

## 👨‍💻 Author

Saikiran Gudipelly
