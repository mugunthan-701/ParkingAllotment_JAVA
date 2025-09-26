# Parking Lot Reservation System

This is a backend REST API for a Parking Lot Reservation System, built with Java and Spring Boot.

## 1. Objective

Design and implement a Parking Lot Reservation backend with a REST API. The API allows parking lot administrators to manage floors and slots, and customers to reserve slots for specific time ranges without conflicts.

## 2. Tech Stack

- **Framework**: Spring Boot 3+
- **Language**: Java 17+
- **Data Persistence**: Spring Data JPA with H2 (In-Memory Database)
- **Validation**: Spring Boot Validation (`@Valid`)
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Gradle

## 3. Features & API Endpoints

The following API endpoints are available:

- `POST /floors`: Create a new parking floor.
- `POST /slots`: Create a new parking slot for a given floor.
- `GET /slots`: Get a list of all created slots.
- `POST /reservations/reserve`: Reserve a parking slot for a specific time range.
- `GET /reservations/availability`: Get a list of all available slots for a given time range.
- `GET /reservations/{id}`: Fetch the details of a specific reservation.
- `DELETE /reservations/{id}`: Cancel a reservation.

## 4. Setup and Run Instructions

### Prerequisites

- Java Development Kit (JDK) 17 or newer.

### Running the Application

1.  Clone the repository or download the source code.
2.  Open a terminal in the project's root directory.
3.  Execute the following command:

    ```bash
    ./gradlew bootRun
    ```

4.  The application will start on `http://localhost:8082`.

## 5. Accessing the Application

### API Documentation (Swagger UI)

Once the application is running, you can access the interactive API documentation here:

- **URL**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

### Basic Frontend

A basic user interface is also provided to interact with the backend:

- **URL**: [http://localhost:8082/](http://localhost:8082/)

## 6. Example API Requests (cURL)

Here are some example `curl` commands to interact with the API.

### Create a Floor

```bash
curl -X POST "http://localhost:8082/floors" \
-H "Content-Type: application/json" \
-d '{
  "floorNumber": 1
}'
```

### Create a Slot

```bash
# First, create a floor and note its ID (e.g., 1)
curl -X POST "http://localhost:8082/slots" \
-H "Content-Type: application/json" \
-d '{
  "slotNumber": 101,
  "floorId": 1,
  "vehicleType": "FOUR_WHEELER"
}'
```

### Check Availability

```bash
# Replace with your desired start and end times
curl -X GET "http://localhost:8082/reservations/availability?startTime=2025-10-01T10:00:00&endTime=2025-10-01T12:00:00"
```

### Reserve a Slot

```bash
# Use a slotId from the availability check
curl -X POST "http://localhost:8082/reservations/reserve" \
-H "Content-Type: application/json" \
-d '{
  "slotId": 1,
  "startTime": "2025-10-01T10:00:00",
  "endTime": "2025-10-01T12:00:00",
  "vehicleNumber": "KA01XY1234"
}'
```

### Get Reservation Details

```bash
# Replace {id} with a valid reservation ID
curl -X GET "http://localhost:8082/reservations/1"
```

## 7. Code Structure

For a detailed explanation of the project's architecture and code structure, please see the [CODE_EXPLANATION.md](CODE_EXPLANATION.md) file.
