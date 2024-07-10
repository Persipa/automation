FROM openjdk:17.0-jdk-slim
ARG VERSION
WORKDIR /
ADD target/automation-core-$VERSION.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 7005
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
