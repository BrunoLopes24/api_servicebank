# API Service Bank

## Overview
This project, API Service Bank, is designed to provide a comprehensive backend system for banking services. It encompasses features such as client management, transaction processing, and authentication services. The system is built using Java and Spring Boot, leveraging various Spring modules such as Spring Security for authentication and authorization.

## Features

### Client Management
- **Create Client Accounts**: Enables the creation of client accounts with necessary details including name, username, email, and more. Age restrictions are applied.
- **View Client Details**: Admins can view details of all clients or a specific client by ID.
- **Update Client Information**: Allows updating client details by their ID.
- **Delete Client Accounts**: Admins can delete client accounts by their ID.

### Transaction Services
- **Process Transactions**: Supports processing of transactions including transfers between clients and deposit operations. Ensures validation such as balance checks and client existence.
- **Transaction History**: Clients can view their sent and received transactions.

### Authentication and Authorization
- **User Registration and Login**: Supports registration and login functionalities. It uses Spring Security to manage authentication and token generation.

## Technology Stack
- **Spring Boot: For creating the API service**.
- **Spring Security: For handling authentication and authorization**.
- **MYSQL Database: Database for development purposes**.
- **JPA & Hibernate: For ORM and database interaction**.
- **Lombok: To reduce boilerplate code for model/data objects**.

## Setup and Installation
### Without Docker:
1. Ensure you have Java JDK and Maven installed on your machine.
2. Clone the repository to your local machine.
3. Navigate to the project directory and run `mvn spring-boot:run` to start the application.
4. The application will be available at `http://localhost:8080`.

### With Docker:
1. Make sure you have Docker installed on your machine.
2. Clone the repository to your local machine.
3. Navigate to the project directory and run the command `docker build -t api_servicebank .` to build the Docker image of the application.
4. Next, run the command `docker run -p 8080:8080 api_servicebank` to start the container with the application.
5. The application will be available at `http://localhost:8080`.

## API Endpoints

### Auth
- `POST /auth/login`: Authenticate users.
- `POST /auth/register`: Register new users.

### Client
- `GET /client/`: Fetch all clients (Accessible by normal clients).
- `POST /client/add`: Add a new client (Admin only).
- `GET /client/{id}`: Get client details by ID (Admin only).
- `DELETE /client/{id}`: Delete a client by ID (Admin only).
- `PUT /client/{id}`: Update client details by ID (Admin only).

### Transactions
- Transactions related endpoints are integrated within the service layer and are accessible through client operations. Passwords are encrypted using BCrypt.

## Security
The API uses JWT tokens for securing endpoints. Spring Security configures the necessary authentication and authorization.

## Conclusion
This API Service Bank project aims to provide a robust backend for banking applications, focusing on security, reliability, and ease of use.