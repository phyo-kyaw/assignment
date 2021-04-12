FROM openjdk:11

ADD target/water-order-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080

ENV TZ Australia/Sydney

