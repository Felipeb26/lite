FROM adoptopenjdk/openjdk14:x86_64-alpine-jre-14.0.2_12

WORKDIR /app

COPY src /app/src
COPY target/lite-0.0.1-SNAPSHOT.jar /app/app.jar

ENV PASS="dtbsglwrtchxrxsi"
ENV EMAIL="felipeb2silva@gmail.com"

EXPOSE 8081
CMD ["java", "-jar", "app.jar"]