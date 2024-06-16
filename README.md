# Opening Hours Service

This Spring Boot application provides functionality to retrieve and display opening hours for places based on the days of the week.

## Features

- **Retrieve Place Opening Hours**: Fetch detailed opening hours for a specific place identified by its ID.
- **Display Opening Hours**: Format and display opening hours in a structured manner by day.

## Technologies Used

- Java
- Spring Boot
- Spring Webflux (Reactive Web)
- Spring Boot Actuator
- Lombok
- Jackson JSON

## Resources Used

- https://start.spring.io/
- https://www.baeldung.com/
- https://chatgpt.com/
- https://stackoverflow.com/

## Setup Instructions

To run this project locally, follow these steps:

1. **Clone Repository:**

   ```bash
   git clone <repository-url>
   cd opening-hours-service
   ```

2. **Build the Project:**

   If you're using Gradle:
   ```bash
   gradle build
   ```

3. **Run the Application:**

   ```bash
   java -jar target/opening-hours-service.jar
   ```

4. **Access the Application:**

   Open your web browser and navigate to [http://localhost:8080](http://localhost:8080) to interact with the application.

## Endpoints

- **Retrieve Place Opening Hours**: 
  - **GET** `/place/{placeId}`
    - Retrieves detailed opening hours for the place identified by `{placeId}`.

## Usage

### Example

Assuming you have the application running locally:

1. Retrieve opening hours for a place:

   ```bash
   curl -X GET http://localhost:8080/place/GXvPAor1ifNfpF0U5PTG0w
   ```

   Replace `GXvPAor1ifNfpF0U5PTG0w` with an actual place ID.

### Sample Response

```json
{
  "name": "Example Place",
  "address": "123 Example Street",
  "openingDays": {
    "Monday - Friday": [
      {
        "start": "11:30",
        "end": "14:00",
        "type": "OPEN"
      },
      {
        "start": "18:30",
        "end": "22:00",
        "type": "OPEN"
      }
    ],
    "Saturday - Sunday": [
      {
        "start": "18:00",
        "end": "00:00",
        "type": "OPEN"
      },
      {
        "start": "11:30",
        "end": "15:00",
        "type": "OPEN"
      }
    ]
  }
}
```

## Contributing

Contributions are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](LICENSE)
