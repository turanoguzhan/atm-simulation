FROM amazoncorretto:11-alpine-jdk
MAINTAINER oguzhanturan
COPY target/atm*.jar atm-simulation.jar
ENTRYPOINT ["java","-jar","/atm-simulation.jar"]