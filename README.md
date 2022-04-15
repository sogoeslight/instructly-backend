## Instructly

### About
Backend of Bachelor's work. Server part of the driving school aggregator application with functionality to book driving instructor's lessons. 

### ToDo someday:
- Add more documentation (also in Swagger)
- Finish all in-code todos
- Export some config beans into the Spring .yml configurations
- Driving instructor grade: `Double` -> `BigDecimal`
- Authorization
- Finish booking feature and add some end-to-end tests
- Remove some stuff connected with Flyway tasks from `build.gradle.kts`
- Some function could be extension functions too

### Prerequisites
- [JDK 11](https://adoptopenjdk.net/installation.html)
- [Docker](https://www.docker.com/products/docker-desktop/)

### Create Docker containers
- `docker-compose up`

### Migrate DB

- `./gradlew flywayMigrate`

### Run server

- `./gradlew bootRun`

### OpenAPI Docs

http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config
