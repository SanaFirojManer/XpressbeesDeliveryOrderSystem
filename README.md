# Delivery Order Management System

This project is a simple Delivery Order Management System that allows managing delivery orders, processing their status, and fetching statistics about orders and customers. The system implements various operations such as creating orders, fetching orders by status, processing orders concurrently, and tracking order status counts.

### Features

- **Order Management**:
  - Add a new order with a `PENDING` status.
  - Fetch orders based on delivery status (e.g., `PENDING`, `IN_PROGRESS`, `DELIVERED`).
  - Fetch order status counts (i.e., how many orders are in each status).
  - Fetch top 3 customers with the highest number of delivered orders.

- **Concurrent Order Processing**:
  - Orders with `PENDING` status are processed by multiple threads concurrently.
  - Each thread processes an order, updates it to `IN_PROGRESS`, waits for a simulated delivery time, and then marks it as `DELIVERED`.

- **Custom Exceptions**:
  - `OrderNotFoundException`: Thrown when an order cannot be found.
  - `InvalidOrderStatusException`: Thrown when an order cannot be processed due to an invalid status.
  - `OrderProcessingException`: Thrown when there is an issue during the processing of an order.

- **Thread-Safe**: Uses `Pessimistic Write Lock` to ensure no two threads process the same order simultaneously.

---

## Setup

### Prerequisites

- Java 17 or above
- Maven 
- A running instance of PostgreSQL

### Steps to run

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/delivery-order-management.git](https://github.com/SanaFirojManer/XpressbeesDeliveryOrderSystem](https://github.com/SanaFirojManer/XpressbeesDeliveryOrderSystem.git)
   cd delivery-order-management
   
2.**Configure Database:**
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

3.**Build and Run the Application:**
mvn clean install
mvn spring-boot:run

### API Endpoints
POST /api/v1/orders
GET /api/v1/orders
GET /api/v1/customers/top
GET /api/v1/orders/status-count
