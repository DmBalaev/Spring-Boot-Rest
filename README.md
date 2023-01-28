# Spring-Boot-Rest-Task-Manager
___

## Technologies
* Spring Boot 3.0
* Spring Security
* JSON Web Tokens (JWT)
* BCrypt
* Maven
* Docker
* PostgreSql
* Liquibase
___

## Steps to Setup
1. Clone the repository

       git clone https://github.com/DmBalaev/Spring-Boot-Rest.git

2. Go to the project folder

       cd Spring-Boot-Rest-Task-Manager

3. Build the maven project

       mvn clean install -DskipTests=true

4. Run container

       docker-compose up

All commands should  be run from project root

The application will be available at http://localhost:8080


After starting the application, a user was created with the name: admin@gmail.com and password: adminpassword
___


## SpringDoc(Swagger)
After starting the application will be available:

- Swagger UI: http://localhost:8080/swagger-ui/index.html