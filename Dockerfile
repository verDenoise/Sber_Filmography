FROM openjdk:17
ARG jarFile=target/filmography-0.0.2-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${jarFile} filmography.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "filmography.jar"]