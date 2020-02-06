# About this project

This application aims to expose a public API to report a list of payslips. In addition, the Tax rate of these payslips can be modified.

## Stack
- Docker
- Kotlin and a bit of Java 8
- Spring Boot
- Maven
- JUnit 4 and Mockito for unit testing
- Postman

## Running the application

### As a regular Spring Boot application 
    $ git clone https://github.com/chiaradia/payrollservice.git
    $ cd payrollservice
    $ ./mvnw -f pom.xml spring-boot:run
    $ curl http://localhost:8080/api/payslips?year=2018&month=12

### With docker-compose (cloning the repository)  
    
    $ git clone https://github.com/chiaradia/payrollservice.git
    $ cd payrollservice
    $ ./mvnw install dockerfile:build && docker-compose up
    $ curl http://localhost:8080/api/payslips?year=2018&month=12
    
Afterward the generated image is going to be stored in your local Docker repository as chiaradia/payrollserivce.

## Endpoints
| Name 	| Path 	| Method 	| Content-Type 	| Description 	|
|------	|------	|--------	|--------------	|-------------	|
|   Get all Payrolls   	|   /api/payslips?year={year}&month={month}   	|     GET   	| application/json              	| Return all payslips from the given period           	|
|   Update Tax Rate   	|   /api/payslips?year={year}&month={month}&taxRate={rate}   	|      PUT  	| application/json             	|     Update the current tax rate from the given period       	|

### Swagger 

The Swagger documentation is available on this [endpoint](http://localhost:8080/swagger-ui.html) after starting the application locally. 

### Actuator (metrics) 

This project is shipped with Actuator enable. The default metrics (Prometheus) are exposed on the default [endpoint](http://localhost:8080/actuator/prometheus).

Note that is possible to create custom ones, since Micrometer dependency has been added to the POM file.

## Testing the application
    $ cd payrollservice
    $ ./mvnw test

Furthermore, it's highly recommended to take a look at the Postman collection. It's possible to test the entire API with it. You just need to download [Postman](https://www.getpostman.com/apps) and import this [collection](https://github.com/chiaradia/stockservice/blob/master/postman/stockservice.postman_collection.json).

## Improvements

 - Write Integration Tests
 - Secure the service