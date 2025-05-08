# E-Commerce Backend

This is the backend of an e-commerce application built with **Spring Boot**. It provides a robust and scalable REST API for managing the core functionalities of an online shopping platform.

## Features

- [x] **User Management**: User authentication, role-based access control, and profile management.
- [x] **Product Management**: CRUD operations for products, categories, and variants.
- [x] **Shopping Cart**: Add, update, and remove items from the cart.
- [ ] **Order Management**: Place orders, view order history, and manage order statuses.
- [x] **Review System**: Add, edit, and delete product reviews with eligibility checks.
- [x] **Admin Dashboard**: Manage users, products, and categories.
- [x] **Media Management**: Upload and delete product images using Cloudinary.
- [x] **Search and Filtering**: Advanced search and filtering for products.
- [x] **Pagination**: Support for paginated API responses.
- [x] **Error Handling**: Centralized exception handling with meaningful error messages.

## Tech Stack

- **Spring Boot**: Backend framework for building REST APIs.
- **Hibernate**: ORM for database interactions.
- **PostgreSQL**: Relational database for data storage.
- **RabbitMQ**: Message broker for asynchronous tasks (e.g., image deletion).
- **Cloudinary**: Cloud-based image management service.
- **JUnit & Mockito**: Unit and integration testing.
- **Lombok**: Simplifies Java code with annotations.

## Folder Structure

```sh
    src/ 
    ├── main/ 
    │ ├── java/com/nr/ecommercebe/ 
    │ │ ├── module/ # Feature-specific modules (e.g., user, product, cart) 
    │ │ ├── shared/ # Shared utilities, exceptions, and configurations 
    │ │ └── EcommerceBeApplication.java # Main application entry point 
    │ └── resources/ │ ├── application.yml # Application configuration 
    │ └── static/ # Static resources 
    ├── test/ # Unit and integration tests
  ```

## Getting Started

### Prerequisites

- **Java** (>= 17)
- **Maven** (>= 3.8)
- **PostgreSQL** (or any supported database)
- **RabbitMQ** (for message queuing)

### Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/your-username/ecommerce-be.git
    cd ecommerce-be
    ```

2. Configure the database and RabbitMQ in src/main/resources/application.yml.

3. Build the project:

    ```sh
    ./mvnw clean install
    ```

4. Run the application:

    ```sh
    ./mvnw spring-boot:run   
    ```

### API Documentation

    The API is documented using Swagger. Once the application is running, you can access the API documentation at:

    http://localhost:8080/swagger-ui.html

### Testing

    Run the test suite using:

```sh
   ./mvnw test
   ```
    