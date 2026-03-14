FROM maven:3.9.9 AS build
WORKDIR /app
ARG CONTAINER_PORT
COPY pom.xml /app
#run all the cmd we need to build the application
RUN mvn dependency:resolve 
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:21-ea-31-jdk-oracle
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9008
CMD [ "java","-jar","app.jar" ]
# Fomer one
# FROM openjdk:21-ea-31-jdk-oracle as build
# VOLUME /tmp
# COPY target/*.jar app.jar
# ENTRYPOINT [ "java","-jar","app.jar" ]