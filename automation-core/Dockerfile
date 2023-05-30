FROM openjdk:11-jre-slim-bullseye
WORKDIR /
ADD target/automation-core-1.0.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 7005
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
