import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.flywaydb.flyway") version "7.5.1"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    kotlin("plugin.jpa") version "1.4.30"
}

group = "com.sogoeslight"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //implementation("org.springframework.boot:spring-boot-starter-security")
    //implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.1.RELEASE")
    //implementation("org.springframework.security:spring-security-oauth2-client:5.4.6")
    //implementation("org.springframework.security:spring-security-oauth2-jose:5.4.6")
    //implementation("com.nimbusds:nimbus-jose-jwt:8.20.1")
    //implementation("com.jayway.jsonpath:json-path:2.5.0")
    //implementation("org.json:json:20210307")

    //implementation("io.jsonwebtoken:jjwt-api:0.11.2") {
    //    exclude(group = "org.json", module = "json")
    //}
    //implementation("io.jsonwebtoken:jjwt-impl:0.11.2") {
    //    exclude(group = "org.json", module = "json")
    //}
    //implementation("io.jsonwebtoken:jjwt-jackson:0.11.2") {
    //    exclude(group = "org.json", module = "json")
    //}

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.vladmihalcea:hibernate-types-52:2.10.2")
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.5")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.5.5")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.5.5")
    implementation("com.github.ben-manes.caffeine:caffeine")
    //developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.json", module = "json")
    }
    testImplementation("org.testcontainers:testcontainers:1.15.1")
}

flyway {
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASS")
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:/src/main/resources/db/migration")
}

sourceSets {
    val flyway by creating {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
    main {
        output.dir(flyway.output)
    }
}

val migrationDirs = listOf(
    "$projectDir/src/main/resources/db/migration"
)

tasks.flywayMigrate {
    dependsOn("flywayClean")
    migrationDirs.forEach { inputs.dir(it) }
    outputs.dir("${project.buildDir}/generated/flyway")
    doFirst { delete(outputs.files) }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
