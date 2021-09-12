# FROM adoptopenjdk:11-jre-hotspot
# COPY . .
# CMD ["java","-jar","target/foodlogger-0.0.1-SNAPSHOT.jar"]
FROM maven:3.8.2-jdk-11 AS MAVEN_BUILD

WORKDIR /app
COPY . /app/
RUN apt-get update
# install curl
RUN apt-get install curl
# get install script and pass it to execute:
RUN curl -sL https://deb.nodesource.com/setup_16.x | bash
# and install node
RUN apt-get install nodejs
RUN npm install
RUN npm run webapp:build:prod
RUN mvn package -P prod

ENTRYPOINT ["java","-Xms64M","-Xmx64m","-XX:+UseSerialGC","-Xss512k","-XX:MaxRAM=72m", "-jar", "target/foodlogger-0.0.1-SNAPSHOT.jar"]
