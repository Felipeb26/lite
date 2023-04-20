FROM adoptopenjdk/openjdk14:x86_64-alpine-jre-14.0.2_12

WORKDIR /app

ENV PASS=dtbsglwrtchxrxsi
ENV EMAIL=felipeb2silva@gmail.com

COPY src /app/src
COPY target/lite-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 9091
CMD ["java", "-jar", "app.jar"]