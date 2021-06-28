## Instructly

### Prerequisites

- [Intellij IDEA](https://www.jetbrains.com/idea/download/)
- [JDK 11](https://adoptopenjdk.net/installation.html)
- [Gradle](https://gradle.org/install/)
- [PostgreSQL](https://www.postgresql.org/download/windows/)
- [Flyway](https://flywaydb.org/documentation/usage/commandline/#download-and-installation)
- [npm](https://www.npmjs.com/get-npm)
- yarn - (`npm install --global yarn`)

### How to migrate DB

- `./gradlew flywayMigrate`
- Check that all went good `./gradlew flywayInfo`

### Run server

- `./gradlew bootRun`

### OpenAPI Docs

http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config
