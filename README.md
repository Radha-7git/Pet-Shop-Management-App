# Pet Shop Management Application

A Spring Boot application for managing a pet shop with customer and sales tracking capabilities.

## Technology Stack

- **Spring Boot**: 3.5.1
- **Spring Framework**: 6.2.8
- **Java**: 21
- **Database**: MySQL 8.0
- **Build Tool**: Maven

## Prerequisites

Before running this application, ensure you have:

1. **Java 21** installed

   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/#java21) or [OpenJDK](https://adoptium.net/)
   - Verify: `java -version`

2. **Maven 3.9+** installed

   - Download from [Apache Maven](https://maven.apache.org/download.cgi)
   - Verify: `mvn -version`

3. **MySQL 8.0+** installed and running
   - Download from [MySQL](https://dev.mysql.com/downloads/mysql/)

## Database Setup

1. Start MySQL server

2. Create the database and user:

```sql
CREATE DATABASE petshop;
CREATE USER 'petshop'@'localhost' IDENTIFIED BY 'petshop123';
GRANT ALL PRIVILEGES ON petshop.* TO 'petshop'@'localhost';
FLUSH PRIVILEGES;
```

3. The application will automatically create the tables on first run (using JPA/Hibernate)

## Configuration

Database configuration is in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petshop
spring.datasource.username=petshop
spring.datasource.password=petshop123
```

Update these values if your MySQL setup is different.

## Running the Application

### Option 1: Using Maven

```bash
cd backend
mvn spring-boot:run
```

### Option 2: Using JAR file

```bash
cd backend
mvn clean package -DskipTests
java -jar target/petshop-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

## API Endpoints

### Pets

- `GET /api/pets` - List all pets
- `GET /api/pets/{id}` - Get pet by ID
- `POST /api/pets` - Create new pet
- `PUT /api/pets/{id}` - Update pet
- `DELETE /api/pets/{id}` - Delete pet

### Customers

- `GET /api/customers` - List all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Sales

- `GET /api/sales` - List all sales
- `GET /api/sales/{id}` - Get sale by ID
- `POST /api/sales` - Create new sale
- `GET /api/sales/customer/{customerId}` - Get sales by customer

### Health Check

- `GET /actuator/health` - Application health status

## Testing the Application

Access the web interface at: **http://localhost:8080**

Or test the API with curl:

```bash
# Check health
curl http://localhost:8080/actuator/health

# List pets
curl http://localhost:8080/api/pets

# Create a pet
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -d '{"name":"Buddy","species":"Dog","price":500.00}'
```

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/petshop/
│   │   │   ├── PetShopApplication.java      # Main application
│   │   │   ├── config/                       # Configuration classes
│   │   │   ├── controller/                   # REST controllers
│   │   │   ├── dto/                          # Data transfer objects
│   │   │   ├── model/                        # Entity classes
│   │   │   └── repository/                   # JPA repositories
│   │   └── resources/
│   │       ├── application.properties        # App configuration
│   │       └── static/index.html            # Web UI
│   └── test/                                # Test files
├── pom.xml                                  # Maven configuration
└── README.md                                # This file
```

## Development

### Building the Project

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Compiling Only

```bash
mvn clean compile
```

## Troubleshooting

### MySQL Connection Issues

- Verify MySQL is running: `mysql -u root -p`
- Check credentials in `application.properties`
- Ensure database `petshop` exists

### Port 8080 Already in Use

- Stop other applications using port 8080
- Or change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Build Failures

- Ensure Java 21 is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Clean Maven cache: `mvn clean`

## License

This is a demonstration project for educational purposes.
