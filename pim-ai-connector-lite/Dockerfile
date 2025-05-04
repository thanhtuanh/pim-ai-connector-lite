FROM openjdk:17-slim

WORKDIR /app

COPY target/pim-ai-connector-lite-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/pim-config.xml /app/pim-config.xml
COPY entrypoint.sh /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

ENV ACTIVE_PRODUKTTYP_ID=fashion
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]