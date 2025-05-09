FROM openjdk:21-ea-31-jdk-oracle
VOLUME /tmp
COPY target/*jar app.jar
ENTRYPOINT [ "java","-jar","app.jar" ]