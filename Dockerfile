FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/health-monitor-app-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]