FROM openjdk:14-alpine
COPY build/libs/web_server-*-all.jar web_server.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "web_server.jar"]