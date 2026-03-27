# 🚀 Employee Management System (Spring Boot)

A **production-ready backend application** built using **Spring Boot** that simulates a real-world **Employee Management System** used in corporate environments. This project is designed with clean architecture, role-based responsibilities, and standardized API responses — making it highly suitable for **freshers targeting Java backend roles**.

---

## 📌 Project Overview

The Employee Management System manages:

* Employees and departments
* Attendance tracking
* Leave management with approval workflows
* Role-based responsibilities (Employee, Manager, Admin)

The goal of this project is to demonstrate **real-life backend design**, not just CRUD operations.

---

## 🛠️ Tech Stack

* **Java 21**
* **Spring Boot 4.x**
* **Spring Data JPA (Hibernate)**
* **MySQL**
* **Lombok**
* **Maven**

---

## 🧱 Architecture

```
Controller Layer  →  Service Layer  →  Repository Layer  →  Database
                        ↓
                    DTOs & Validators
                        ↓
              Global Exception Handling
```

### Key Architectural Highlights

* Clean separation of concerns
* DTO-based API responses
* Centralized exception handling using `@RestControllerAdvice`
* Reusable API response wrapper

---

## 👥 User Roles & Responsibilities

### 👤 Employee

* View personal profile
* Mark daily attendance
* Apply for leave
* View leave status and leave balance
* Cancel pending leave requests

### 👨‍💼 Manager

* View team members
* Approve or reject employee leave requests
* View team attendance by date

### 🛡️ Admin

* Create and manage employees
* Create and assign departments
* Assign roles (Employee / Manager / Admin)
* View attendance of all employees
* Reject or approve any leave request of only manager

---

## 📦 Core Modules

### 1️⃣ Employee Module

* Employee creation and management
* Department and manager mapping

### 2️⃣ Attendance Module

* Mark daily attendance
* Fetch attendance by date and department

Example API:

```
GET /api/attendance?date=2026-02-03&departmentId=1
```

### 3️⃣ Leave Management Module

* Apply leave
* Approve / reject leave
* Cancel leave
* Automatic leave balance updates

---

## 🗃️ Database Design (High-Level)

### Entities

* **Employee** (id, name, email, role, status, department, manager)
* **Department** (id, name)
* **Attendance** (id, date, status, employee)
* **LeaveRequest** (id, startDate, endDate, leaveType, status, employee)
* **LeaveBalance** (employee, sickLeave, casualLeave)

### Relationships

* One-to-Many (Department → Employees)
* Many-to-One (Employee → Department)
* One-to-One (Employee → LeaveBalance)
* One-to-Many (Employee → Attendance, LeaveRequest)

---

## 📊 Standard API Response Format

All APIs return a consistent, production-level JSON response:

```json
{
  "status": "SUCCESS",
  "message": "Human readable message",
  "data": {},
  "timestamp": "2026-02-03T14:35:22",
  "path": "/api/employees/101"
}
```

### Benefits

* Frontend-friendly
* Easy debugging
* Industry-standard structure

---

## ❌ Global Exception Handling

Handled centrally using `@RestControllerAdvice`:

* Resource Not Found
* Validation Errors
* Illegal Arguments
* Generic Exceptions

All error responses also follow the same API response format.

---

## ✅ Validations & Business Rules

* Bean validation using `jakarta.validation`
* Custom validations (date range, enum values)
* Database-level constraints
* Role-based leave approval logic

---

### 🚫 Secure Logout with Token Blacklisting & Attendance Automation

This project implements an advanced **secure logout mechanism** combined with **automatic attendance tracking**, simulating real-world enterprise behavior.

### 🔒 Token Blacklisting

* On logout, the user's JWT token is **stored in a blacklist**
* Every incoming request is validated against the blacklist
* If the token is blacklisted → request is rejected

👉 This ensures **true logout functionality in a stateless JWT system**

---

### ⏱ Attendance Automation Logic

#### ✅ On Login (Check-In)

* When a user successfully logs in:

  * JWT token is generated
  * **Check-in time is automatically recorded** in the Attendance module

#### 🚪 On Logout (Check-Out)

* When a user logs out:

  * JWT token is blacklisted
  * **Check-out time is automatically updated** for that user

---

### 🎯 Key Benefits

* Eliminates manual attendance marking
* Ensures **accurate login/logout tracking**
* Prevents reuse of invalid/expired tokens
* Simulates **real corporate attendance systems**

---

### 🔁 Flow Summary

1. User logs in → JWT generated + Check-in marked
2. User accesses secured APIs using token
3. User logs out → Token blacklisted + Check-out marked
4. Any further request with same token → ❌ Rejected

---

## 🎯 What This Project Demonstrates

* Real-world backend workflows and business logic implementation
* Clean layered architecture (Controller → Service → Repository)
* Secure authentication & authorization using JWT and Spring Security
* Advanced logout handling using token blacklisting
* Automated attendance system integrated with login/logout flow
* JPA entity relationships and database design
* Centralized exception handling using `@RestControllerAdvice`
* Standardized and production-ready API response structure

---

## 📈 Project Level

✅ **Intermediate Backend Project**

This project goes beyond basic CRUD and demonstrates real business logic commonly used in enterprise applications.

---

## 🔮 Future Enhancements
* Swagger / OpenAPI documentation
* Pagination and sorting
* Docker support
---

⭐ If you find this project useful, feel free to star the repository!
