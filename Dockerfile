FROM adoptopenjdk/openjdk11:alpine
EXPOSE 8080
ADD /build/libs/instructly-0.0.1-SNAPSHOT.jar instructly.jar
ENTRYPOINT ["java", "-jar", "instructly.jar"]
