# Code Structure Explanation

This document provides an overview of the project's structure and the role of each component.

## Project Structure

The project is organized into several packages, following standard Spring Boot conventions:

```
src/main/java/com/example/demo/
├── config/
├── controller/
├── dto/
├── exception/
├── model/
├── repository/
└── service/
```

### 1. `model`

This package contains the core domain objects, which are JPA entities mapped to database tables.

- **`Floor.java`**: Represents a single floor in the parking lot.
- **`Slot.java`**: Represents a single parking slot on a specific `Floor`. It has a many-to-one relationship with `Floor` and includes the `VehicleType`.
- **`Reservation.java`**: Represents a customer's reservation for a specific `Slot` over a time range. It includes start/end times, vehicle details, the calculated cost, and a `@Version` field for optimistic locking.
- **`VehicleType.java`**: An enum that defines the types of vehicles supported (e.g., `FOUR_WHEELER`, `TWO_WHEELER`) and holds the hourly parking rate for each. This makes it easy to add new vehicle types or change rates without altering business logic.

### 2. `repository`

This package contains the Spring Data JPA repositories. These interfaces handle all database operations (CRUD) for the entities.

- **`ReservationRepository.java`**: Includes a custom JPQL query (`@Query`) to efficiently find any existing reservations that overlap with a new reservation's time range for a specific slot. This is the core of the conflict detection mechanism.

### 3. `dto` (Data Transfer Object)

DTOs are used to shape the data for the API. They decouple the API layer from the internal database entities, which is a crucial best practice.

- **Request DTOs** (e.g., `CreateFloorRequest.java`, `CreateReservationRequest.java`): These objects model the expected JSON payload for `POST` requests. They contain validation annotations (`@NotNull`, `@Pattern`, etc.) to enforce business rules at the API entry point.
- **Response DTOs** (e.g., `FloorDto.java`, `ReservationDto.java`): These objects model the JSON payload sent back to the client, ensuring only relevant and well-formatted data is exposed.

### 4. `service`

The service layer contains the core business logic of the application.

- **`FloorService.java` & `SlotService.java`**: Handle the logic for creating floors and slots.
- **`ReservationService.java`**: The most critical service. It orchestrates the reservation process:
    - **Validation**: Enforces business rules that cannot be checked with simple annotations (e.g., reservation duration < 24 hours).
    - **Conflict Checking**: Uses the `ReservationRepository` to ensure a slot is available.
    - **Cost Calculation**: Calculates the parking fee based on the vehicle type's rate and rounds up partial hours.
    - **Availability**: Implements the logic to find all available slots for a given time range.

### 5. `controller`

The controller layer exposes the application's functionality as RESTful API endpoints.

- It uses annotations like `@RestController`, `@PostMapping`, `@GetMapping`, etc., to map HTTP requests to service methods.
- It uses `@Valid` on request bodies to trigger bean validation.
- It integrates with SpringDoc OpenAPI (`@Operation` annotations) to generate live API documentation.

### 6. `exception`

This package defines custom exceptions and a global exception handler.

- **Custom Exceptions** (e.g., `ResourceNotFoundException`, `ReservationConflictException`): These are used to represent specific error scenarios in the business logic.
- **`GlobalExceptionHandler.java`**: Annotated with `@ControllerAdvice`, this class intercepts all exceptions thrown from the controllers. It formats them into a consistent JSON error response (`ErrorResponse.dto`), providing clear and structured error messages to the API consumer.

### 7. `resources/static`

This directory contains the basic HTML and JavaScript frontend, which is served directly by the Spring Boot application.
