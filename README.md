# Payment Service

A simple Spring Boot based backend service for managing payments, packaged as a Docker container. 

## Features

- Create and manage 3 types of payments in different currencies
- Validation rules for payment types
- Querying payments according to their state
- H2 in-memory database (can be switched via environment variables)
- Dockerized for easier deployment

## Upcoming Features
- [x] Payment creation
- [x] Payment type validation
- [x] Payment cancellation, with cancellation fee
- [x] Querying of pending payments
- [x] Filtering payments by amount
- [x] Unit tests - in progress
- [ ] Client country logging
- [ ] Notification about saved payment

## Technologies
- Java 17
- Spring Boot
- Rest API
- H2 Database
- Docker

## Docker usage
- Pull the image from Docker Hub with command "docker pull mrkrabsbri/payments-ba:v1.0"
- Database credentials are provided via environment variables. In order to access database the credentials have to be entered manually: 
- Run the container with command : 
  "docker run -p 8082:8082 -p 8000:8000 -e DB_URL=jdbc:h2:mem:paymentapp -e DB_USER=sa -e DB_PASSWORD=password paymentapp:v1.0"

## H2 usage
  H2 console can be accessed by http://localhost:8082/h2-console/ 
  Database access requires credentials set through environment variables. When running locally, you must provide them manually as shown below:
  DB_URL=jdbc:h2:mem:paymentapp;
  DB_USER=sa;
  DB_PASSWORD=password

  ## Postman
  Link to Postman collection to test out API : https://julius-9465839.postman.co/workspace/Julius's-Workspace~2c28515a-eceb-433a-8cec-019fe2c244c4/request/44657444-f9518051-87db-47d1-9157-d1f49cda06f1?action=share&source=copy-link&creator=44657444
  
