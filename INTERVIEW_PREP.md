# Interview Preparation Guide: Parking Lot System

This document is structured in a Q&A format to help you explain the project's design, architecture, and features, including direct references to the code.

---

### Q1: Can you give a high-level overview of the project and its architecture?

**Answer:**

This project is a RESTful backend for a parking lot reservation system, built using Java and Spring Boot. It allows administrators to manage the parking structure (floors and slots) and enables customers to reserve slots for a specific duration.

The architecture follows a classic N-tier pattern:

1.  **Controller Layer (`controller`):** Exposes the REST API endpoints. It handles incoming HTTP requests, validates them, and delegates the business logic to the service layer.
2.  **Service Layer (`service`):** Contains all the core business logic. This is where we validate rules, check for booking conflicts, and calculate costs.
3.  **Repository Layer (`repository`):** Manages all data persistence using Spring Data JPA.
4.  **Domain/Model Layer (`model`):** Consists of the JPA entities that map directly to database tables.

To ensure a clean separation of concerns, the API uses **Data Transfer Objects (DTOs)** (`dto` package). This means the internal database models are not directly exposed to the client, providing a stable and secure API contract.

### Q2: Are you using a relational database? If not, where is the data being stored?

**Answer:**

**Yes, the application is designed to work with any relational database** using the **Spring Data JPA** abstraction layer.

For this project's current configuration, we are using the **H2 in-memory database**. This is configured in `src/main/resources/application.properties` on lines 5-10.

*   **In-Memory:** The data is stored directly in the computer's RAM, not on a physical disk. This means the data is **volatile** and is **completely wiped clean every time the application restarts**.

We use H2 for development because it's fast and requires no setup. To switch to a persistent database like **MySQL**, we would only need to change these properties in `application.properties`; no Java code changes would be necessary.

### Q3: How does the system prevent double bookings or reservation conflicts?

**Answer:**

The system prevents double bookings at the service layer. The process is as follows:

1.  A custom JPQL query is defined in `src/main/java/com/example/demo/repository/ReservationRepository.java` on line 15.
2.  This query searches for any existing reservations for the same slot that overlap with the requested time range.
3.  In `src/main/java/com/example/demo/service/ReservationService.java`, on lines 48-51, this query is executed. If any overlapping reservations are found, a custom `ReservationConflictException` is thrown, resulting in a `409 Conflict` HTTP response.

### Q4: What if two users try to book the same slot at the exact same time? How do you handle that race condition?

**Answer:**

That race condition is handled using an **optimistic locking** strategy.

1.  In `src/main/java/com/example/demo/model/Reservation.java`, on line 38, the entity is annotated with `@Version`. This tells JPA to manage a version number for each reservation.
2.  When two users try to book a slot simultaneously, the first user to save their reservation increments the version number in the database.
3.  When the second user tries to save, JPA detects the version mismatch and throws an `ObjectOptimisticLockingFailureException`.
4.  In `src/main/java/com/example/demo/service/ReservationService.java`, on lines 57-62, there is a `try-catch` block that specifically catches this exception. It then throws a user-friendly `ReservationConflictException` with the message, "This slot was just booked by someone else."

### Q5: How did you implement the business rules, like fee calculation and validation?

**Answer:**

Validation is handled at two levels:

1.  **Format Validation:** In `src/main/java/com/example/demo/dto/CreateReservationRequest.java`, on line 28, the `@Pattern` annotation ensures the vehicle number matches the required format (`XX00XX0000`). This is checked at the controller level.
2.  **Business Logic Validation:** In `src/main/java/com/example/demo/service/ReservationService.java`, on lines 120-122, a method checks complex rules, such as ensuring the reservation duration does not exceed 24 hours.

For **fee calculation**, the logic is in `src/main/java/com/example/demo/service/ReservationService.java` on lines 125-128:

*   It calculates the duration, uses `Math.ceil()` to round up partial hours, and multiplies the result by the hourly rate.
*   The rate itself is defined in the `VehicleType` enum in `src/main/java/com/example/demo/model/VehicleType.java` on lines 6-7, which makes it easy to modify or extend.