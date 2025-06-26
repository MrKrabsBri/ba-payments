# Payment Service

A simple Spring Boot based backend service for managing payments, packaged as a Docker container. 

## Features

- [x] Payment creation, reading, update functionality
- [x] Payment type validation, details field validation for different types,
- [x] Validator, custom annotation for field validation
- [x] Payment cancellation if cancelling before midnight. In case of cancellation, a fee is calculated
- [x] Filtering payments by amount, querying payments by state
- [x] Retrieving pending payments
- [x] Controller, service layer tests - 62% class coverage, 46% method coverage
- [ ] Client country logging
- [ ] Notification about saved payment

## Technologies
- Java 17
- Spring Boot
- Rest API
- H2 Database
- Docker

## Docker usage
- Pull the image from Docker Hub using Windows Command Prompt, PowerShell, or other compatible terminals with command "docker pull mrkrabsbri/payments-ba:v2.0"
- Database credentials are provided via environment variables. In order to access database the credentials have to be entered manually in a command. 
- Run container with command : 
  "docker run -p 8082:8082 -p 8000:8000 -e DB_URL=jdbc:h2:mem:paymentapp -e DB_USER=sa -e DB_PASSWORD=password mrkrabsbri/payments-ba:v2.0"

## H2 usage
  H2 console can be accessed by http://localhost:8082/h2-console/ 
  Database access requires credentials set through environment variables. When running locally, you must provide them manually as shown below:
  DB_URL=jdbc:h2:mem:paymentapp;
  DB_USER=sa;
  DB_PASSWORD=password

  ## Postman
  Link to Postman collection to test out API : https://www.postman.com/julius-9465839/workspace/payments-ba/collection/44657444-2004ed9b-d57d-43b7-bf0c-52774a78b25c?action=share&creator=44657444
  If link is not working or http requests are missing, you can find a postman collection JSON file below.
[Payments.postman_collection.json](https://github.com/user-attachments/files/20930345/Payments.postman_collection.json)
