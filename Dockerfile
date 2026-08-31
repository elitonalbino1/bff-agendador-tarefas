FROM openjdk:17-jdk-alpine

WORKDIR  /app

COPY target/bff-agendador-0.0.1-SNAPSHOT.jar  /app/bff-agendador-tarefas.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/bff-agendador-tarefas.jar"]