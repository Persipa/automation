FROM openjdk:17.0-jdk-slim
WORKDIR /
ADD target/automation-core-*.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 7005
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
