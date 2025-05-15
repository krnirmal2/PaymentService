# PaymentService Documentation

---

## Overview

**PaymentService** is a Spring Boot microservice designed to handle payment processing through multiple payment gateways (Razorpay and Stripe). It provides a RESTful API for initiating payments, generating payment links, and integrating with third-party payment providers using a strategy design pattern for extensibility.

---

## Architecture

```
+-------------------+
|  Client (e.g. UI) |
+--------+----------+
         |
         v
+--------+----------+
|  PaymentController |
+--------+----------+
         |
         v
+--------+----------+
|  PaymentService    |
+--------+----------+
         |
         v
+-----------------------------+
| PaymentGateway (Interface)   |
+-----------------------------+
         |                |
         v                v
+----------------+   +----------------+
| Razorpay Impl  |   | Stripe Impl     |
+----------------+   +----------------+
```

- **Controller Layer:** Exposes REST endpoints for payment operations.
- **Service Layer:** Contains business logic for payment processing.
- **Gateway Layer:** Abstracts payment provider logic using the Strategy pattern.
- **Config Layer:** Manages beans and external configuration.

---

## Project Structure

```
src/main/java/org/example/paymentservice/
├── PaymentServiceApplication.java
├── config/
│   └── PaymentGatewayConfig.java
├── controllers/
│   ├── HelloController.java
│   └── PaymentController.java
├── dtos/
│   ├── InitiatePaymentRequestDto.java
│   └── initiatePaymentRespondDto.java
├── paymentgateway/
│   ├── PaymentGateway.java
│   ├── RazorpayPaymentGateway.java
│   └── StripePaymentGateway.java
└── services/
    └── PaymentService.java
```

---

## Setup & Configuration

### Prerequisites
- Java 21+
- Maven
- MySQL (for persistence)

### Environment Variables
- `RAZORPAY_KEY_ID` and `RAZORPAY_KEY_SECRET` must be set in your environment for Razorpay integration.
- Stripe secret key is currently hardcoded for demo; move to environment/config for production.

### application.properties
```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/paymentservice
spring.datasource.username=paymentserviceuser
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql: true
razorpay.key.id=${RAZORPAY_KEY_ID}
razorpay.key.secret=${RAZORPAY_KEY_SECRET}
```

---

## Building & Running

```bash
mvn clean install
mvn spring-boot:run
```

---

## API Endpoints

### Health Check
- `GET /` → Returns "Hello"

### Initiate Payment
- `POST /payments/`
  - **Request Body:**
    ```json
    {
      "orderId": "string",
      "amount": 1000,
      "phoneNumber": "1234567890",
      "email": "user@example.com"
    }
    ```
  - **Response:**
    - Razorpay: JSON with payment link and details
    - Stripe: URL to payment link

---

## Payment Gateway Integration

### Strategy Pattern
- `PaymentGateway` interface defines `generatePaymentLink(...)`.
- `RazorpayPaymentGateway` and `StripePaymentGateway` implement this interface.
- `PaymentService` uses dependency injection to select the gateway at runtime.

### Adding a New Gateway
1. Implement the `PaymentGateway` interface.
2. Register the implementation as a Spring `@Service`.
3. Update configuration to inject the new gateway where needed.

---

## Key Classes

### PaymentServiceApplication
- Main entry point. Boots the Spring context.

### PaymentController
- Exposes `/payments/` endpoint for initiating payments.

### PaymentService
- Business logic for payment initiation. Delegates to the selected payment gateway.

### PaymentGateway (Interface)
- Contract for payment gateway implementations.

### RazorpayPaymentGateway
- Integrates with Razorpay API to generate payment links.

### StripePaymentGateway
- Integrates with Stripe API to generate payment links.

### PaymentGatewayConfig
- Provides configuration and beans for payment gateway clients.

### DTOs
- `InitiatePaymentRequestDto`: Request payload for payment initiation.
- `initiatePaymentRespondDto`: (Currently empty, can be extended for structured responses.)

---

## Example Payment Flow
1. Client sends a POST request to `/payments/` with order/payment details.
2. Controller receives and validates the request.
3. Service selects the payment gateway and generates a payment link.
4. Client receives the payment link and completes payment via the provider UI.

---

## Extending & Customizing
- Add new payment gateways by implementing the `PaymentGateway` interface.
- Add new endpoints or business logic in the service/controller layers.
- Secure sensitive keys using environment variables or a secrets manager.

---

## References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Razorpay API Docs](https://razorpay.com/docs/api/)
- [Stripe API Docs](https://stripe.com/docs/api)

---

## Authors & Maintainers
- [Your Name Here]

---

## License
- [Specify your license here]

---

## Technical Stack & Developer Practices

### 1. Programming Language & Framework
- **Java 21**: Modern, robust, and widely used for backend development.
- **Spring Boot**: Rapid application development, dependency injection, REST API support, and easy integration with other Spring projects.

### 2. Project Management & Build
- **Maven**: Dependency management, build automation, and project structure standardization.

### 3. API Design
- **RESTful APIs**: Exposed using Spring's `@RestController` and `@RequestMapping`/`@PostMapping` annotations.
- **DTOs (Data Transfer Objects)**: Used for request and response payloads, ensuring clean API contracts.

### 4. Design Patterns
- **Strategy Pattern**: Used for payment gateway abstraction (`PaymentGateway` interface with multiple implementations).
- **Dependency Injection**: Managed by Spring for loose coupling and testability.

### 5. Third-Party Integrations
- **Razorpay Java SDK**: For payment link generation and payment processing.
- **Stripe Java SDK**: For payment link generation and payment processing.

### 6. Database & Persistence
- **MySQL**: Configured for persistence (can be extended for storing payment/order data).
- **Spring Data JPA**: (Dependency included, ready for use if you add repositories/entities).

### 7. Configuration Management
- **application.properties**: Centralized configuration for database and payment gateway credentials.
- **Environment Variables**: Used for sensitive data (API keys/secrets).

### 8. Testing & Debugging
- **Postman**: For manual API testing.
- **Localtunnel**: For exposing local services to the internet (useful for webhook/callback testing).

### 9. Code Quality & Tooling
- **Lombok**: Reduces boilerplate code for DTOs (getters/setters).
- **Spotless Maven Plugin**: For code formatting and style consistency.

### 10. Security Practices
- **Environment Variables**: For secret management (never hardcoding sensitive keys).
- **Best Practices**: Recommendations to use secrets managers for production.

### 11. Documentation
- **Markdown Files**: For project, design, and step-by-step documentation.
- **Diagrams**: ASCII/markdown diagrams for architecture and flow.

#### Summary Table

| Area                | Technology/Practice         | Purpose/Benefit                                 |
|---------------------|----------------------------|-------------------------------------------------|
| Language            | Java 21                    | Modern, robust backend language                 |
| Framework           | Spring Boot                | Rapid REST API development, DI, config mgmt     |
| Build Tool          | Maven                      | Dependency/build management                     |
| API Design          | REST, DTOs                 | Clean, scalable API contracts                   |
| Patterns            | Strategy, DI               | Extensibility, loose coupling                   |
| Payment Gateways    | Razorpay, Stripe SDKs      | Third-party payment integration                 |
| Database            | MySQL, Spring Data JPA     | Persistence (ready for extension)               |
| Config              | application.properties, env| Centralized, secure configuration               |
| Testing             | Postman, Localtunnel       | API and webhook testing                         |
| Code Quality        | Lombok, Spotless           | Less boilerplate, consistent style              |
| Security            | Env vars, best practices   | Protect sensitive data                          |
| Documentation       | Markdown, diagrams         | Developer onboarding, clarity                   |

---

## Step-by-Step Integration & Testing Guide

### 1. Exposing Your Local Service (for Testing)
- Use [Localtunnel](https://github.com/localtunnel/localtunnel) to expose your local server to the internet for webhook and payment gateway callbacks.
- **Install Localtunnel globally:**
  ```bash
  npm install -g localtunnel
  ```
- **Start a tunnel:**
  ```bash
  lt --port 8080
  ```
- You'll receive a public URL (e.g., `https://xyz.loca.lt`) that proxies requests to your local server.

### 2. Razorpay Integration Steps

#### a. Add Razorpay Dependency
- Add the following to your `pom.xml`:
  ```xml
  <dependency>
      <groupId>com.razorpay</groupId>
      <artifactId>razorpay-java</artifactId>
      <version>1.4.5</version>
  </dependency>
  ```

#### b. Create a Razorpay Account
- Sign up at [Razorpay](https://razorpay.com/).
- Generate your **Test Key** and **Secret**.

#### c. Set Up Environment Variables
- Store your keys securely as environment variables:
  - `RAZORPAY_KEY_ID`
  - `RAZORPAY_KEY_SECRET`
- Reference them in `application.properties`:
  ```properties
  razorpay.key.id=${RAZORPAY_KEY_ID}
  razorpay.key.secret=${RAZORPAY_KEY_SECRET}
  ```

#### d. Implement Payment Link Generation
- Use the code provided in `RazorpayPaymentGateway.java` to generate payment links.
- Example request payload for payment link:
  ```json
  {
    "orderId": "12344",
    "amount": 100,
    "phoneNumber": "958494839",
    "email": "nirmalkrmajhi14@gmail.com"
  }
  ```

#### e. Test with Postman
- Set up a POST request to `/payments/` with the above payload.
- You'll receive a response with a payment link and details, e.g.:
  ```json
  {
    "short_url": "https://rzp.io/i/GOXpgSEw8C",
    ...
  }
  ```

#### f. Complete the Payment
- Visit the `short_url` to complete the payment.
- Use Razorpay's test card details for simulation:
  - **Mastercard Domestic:** `5267 3181 8797 5449`

### 3. Stripe Integration Steps

#### a. Add Stripe Dependency
- Add the following to your `pom.xml`:
  ```xml
  <dependency>
      <groupId>com.stripe</groupId>
      <artifactId>stripe-java</artifactId>
      <version>24.16.0</version>
  </dependency>
  ```

#### b. Set Up Stripe Keys
- For demo, the secret key is hardcoded in `StripePaymentGateway.java`. For production, use environment variables.

#### c. Generate a Price Object
- Example Stripe price object:
  ```json
  {
    "id": "price_1MoBy5LkdIwHu7ixZhnattbh",
    "currency": "usd",
    "unit_amount": 1000,
    "product": "prod_NZKdYqrwEYx6iK",
    "type": "recurring"
  }
  ```
- Reference: [Stripe Price Object Docs](https://docs.stripe.com/api/prices/object)

#### d. Test Stripe Payment Link
- Use the generated payment link (e.g., `https://buy.stripe.com/test_fZedU97v72LQaoU185`) to simulate a payment.

### 4. General Testing & Debugging
- Use Postman to test your endpoints and verify responses.
- Check logs for errors or issues with payment gateway integration.
- Use the screenshots and sample responses as a reference for expected behavior.

### 5. Security Notes
- **Never** commit your real API keys to version control.
- Always use environment variables or a secrets manager for sensitive data.

### 6. Useful Links
- [Razorpay API Docs](https://razorpay.com/docs/api/)
- [Stripe API Docs](https://stripe.com/docs/api)
- [Localtunnel](https://github.com/localtunnel/localtunnel) 