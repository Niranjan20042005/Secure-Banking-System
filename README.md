# 🏦 Secure Banking System

This is a full stack banking web application built using Spring Boot and MySQL.

## 🚀 Features
- User registration & login
- Secure JWT authentication
- Money transfer
- Account balance checking
- Email OTP verification
- Admin dashboard
- Transaction history

## 🛠 Technologies Used
- Java 17
- Spring Boot
- Spring Security + JWT
- MySQL
- HTML, CSS, JavaScript
- Maven
- Render (deployment)

## 💻 Run Project Locally

### 1. Install Requirements
- Java 17
- MySQL
- Maven
- STS or VS Code

### 2. Create Database
Open MySQL and run:
CREATE DATABASE bankdb;

### 3. Update MySQL password
Open:
src/main/resources/application-local.properties

Change:
spring.datasource.password=your_mysql_password

### 4. Run project

Using STS:
Right click project → Run as → Spring Boot App

Using VS Code terminal:
mvn spring-boot:run

Project runs on:
http://localhost:8080

## 👨‍💻 Developer
Niranjan