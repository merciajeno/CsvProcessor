# Setup & Installation

Follow the steps below to set up and run the application locally.

## 1. Clone the Repository

Clone the project from GitHub:

```bash
git clone https://github.com/merciajeno/CsvProcessor
```

## 2. Configure PostgreSQL

Create a database in PostgreSQL. Open the `application.properties` file and update the PostgreSQL database configuration with your credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database-name>
spring.datasource.username=<your-postgresql-username>
spring.datasource.password=<your-postgresql-password>
```

Make sure PostgreSQL is running locally and that the specified database exists.

## 3. Run the Application

Start the application using your preferred IDE or build tool.

Once the application is running, it will be available at:

```text
http://localhost:8080
```

## 4. API Documentation

The project includes interactive API documentation using Swagger/OpenAPI.

After starting the application, open:

`http://localhost:8080/api-docs.html`

You can use the documentation to explore the available APIs and test endpoints directly.
