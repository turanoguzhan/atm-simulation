# ATM Simulation
 Basic demonstration of how atm works with java technologies. 
 Each operation needs account and pin validation.
 - Expose Authorized Account Current Info
 - Withdraw Operation ( Aims to dispatch at least banknote as much as possible )
 - Deposit Operation
 
 * ATM initializes with €1500 made up of 10 x €50s, 30 x €20s, 30 x €10s and 20 x €5s.
 
 
 #### You can interact with application with Open API interface after run the application on your local machine >> http://localhost:8080/swagger-ui/index.html
 
 ## Technologies
 - Java 11
 - Spring Boot
 - Docker
 - Kubernetes ( Call a request via nodePort )
 
 ## Libraries
 - Spring Boot Security ( Basic Auth )
 - Spring Boot Test
 - Spring Data JPA ( H2 Memory DB )
 - Liquibase 4.11.0
 - OpenAPI 1.6.9
 - Lombok

To run the project on your local machine,
- ```mvn clean install```
- ```docker build -t atmsimulation:latest .```
- ```docker run -dp 8080:8080 atmsimulation:latest```
