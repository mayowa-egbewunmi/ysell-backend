# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.6-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

FROM gcr.io/distroless/java

EXPOSE 3306
EXPOSE 465

COPY --from=builder /app/target/ysell-*.jar /ysell.jar

ENTRYPOINT ["java","-jar","ysell.jar"]