FROM eclipse-temurin:17-jdk-jammy AS builder
ENV JAVA_HOME=/opt/java/openjdk
WORKDIR /opt/app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ls -la
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:17-jre-jammy

#Install dockerize
ENV DOCKERIZE_VERSION v0.6.1
RUN apt-get update && apt-get install -y wget \
    && wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /opt/app
EXPOSE 8085
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar

ENTRYPOINT ["dockerize", "-wait", "tcp://db-service:8086", "-timeout", "60s", "java", "-Dspring.profiles.active=local", "-jar", "/opt/app/*.jar" ]