FROM maven:3.8.1-openjdk-17-slim AS maven-build
WORKDIR /tmp/react-cats
COPY ./pom.xml ./
RUN mvn verify --fail-never -Dmaven.test.skip dependency:go-offline

COPY src ./src
COPY frontend ./frontend
RUN mvn install

FROM openjdk:17-alpine

WORKDIR /opt/react-cats
COPY --from=maven-build /tmp/react-cats/target/react-app-0.0.1-SNAPSHOT.jar ./react-app-0.0.1-SNAPSHOT.jar
COPY stubs ./stubs
RUN mkdir /opt/react-cats/public

ENTRYPOINT ["java","-jar","/opt/react-cats/react-app-0.0.1-SNAPSHOT.jar"]
