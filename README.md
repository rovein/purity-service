# PurityService

My personal project for "Software Architecture" course in NURE.

This is the program system for automizing cleaning processes for placement owners.

It consists of Backend, Frontend, Mobile Android application and IoT device.

[Project demo](https://drive.google.com/file/d/1P1cwzwU3RYW9eeFkfF55v5A9LotrBvdb/view?usp=sharing) (some features were included after this recording).

# Technologies and details

1. The backend was implemented using Java, Spring Boot, Spring MVC, Spring Data JPA (MySql Driver), Spring Security, Maven, Lombok, etc. REST architectural style and REST API was implemented, documentation using Swagger.
2. The frontend was implemented using React and JavaScript, used some libraries for loading files, modals, animating loaders and progress bars. Building with npm.
3. Mobile application was developed using Kotlin and Android OS. Retrofit2 used for asynchronous HTTP requests.
4. For IoT was used Arduino Uno R3, Ethernet module for network communication, DHT11 humidity and temperature sensor and MQ135 air quality sensor, as well as some related libraries.

Communication established via HTTP protocol, that is, the web client interacts with the server via fetch api, mobile application via retrofi2, IoT via EthernetClient and plain HTTP request.

# Implemented features

Briefly about the implemented functions: registration, authorization, profile management, all CRUD operations, sending letters to mail, administration functions: managing user accounts, blocking accounts, obtaining a backup copy of the database, configuring IoT.
Business logic: monitoring the level of pollution and indicators of the placements, the presence of 2 levels of access (in addition to the administrator): the owner of the placements and the cleaning provider, automatic calculation of the cost of cleaning at the conclusion of the contract, the application of the coefficient of settlement of the cost of cleaning to all future contracts for cleaning (the higher the level of pollution in the room, the higher the cost of cleaning).

# ToDo section &#9745;

- add the WebSockets protocol so that the room state indicators are updated in real time &#9744;
- start implementing React components using TypeScript and Hooks API (now all components are class-based) &#9744;
- rewrite fetch calls on the frontend to axios, write axios interceptors for requests and responses, remove code duplication &#9744;
- refactor and improve the code, add more design patterns &#9744;
- optimize Hibernate queries and JPA mapping &#9744;
- cover code by tests (unit, mock, persistence, API, integration) &#9744;
- refactor and improve Stream API, Optional API and lambda expressions usage &#9744;
- add logging, improve the error handling system &#9744;
- divide backend into the microservices, use Spring Cloud, AWS, try use Apache Kafka &#9744;
- use git flow, branching and PR on github (now all the commits were pushed to the master branch) &#9744;
- improve UI and try new front-end features &#9744;
- add new features to the system: password recovery, account activation (email), something like the ability to enable two-factor authentication, generate reports in PDF, improve the functionality of data backup, add SSO and OIDC &#9744;
- add new interesting business functions that relate to the system &#9744;
- deploy the application using Docker containers &#9744;


## How to run
1. Backend  
    In backend root directory run next terminal command:<br>
    ``mvn spring-boot:run``<br>
    (note: [Maven](https://www.baeldung.com/install-maven-on-windows-linux-mac) has to be installed)  
    *or* you can run it via your IDE ([IntelliJ IDEA](https://www.jetbrains.com/idea/promo/?gclid=Cj0KCQjwvr6EBhDOARIsAPpqUPEPhEKTJSsYO3bdgCMDZs6LLwa-z7ZChv8HlTbY0uQim4XURKKrPLgaAqGREALw_wcB) is preferred)  
    Then you can navigate to the http://localhost:8080/swagger-ui.html and check backend **REST API**.  
2. Frontend  
    - [install](https://phoenixnap.com/kb/install-node-js-npm-on-windows) **node.js** and **npm**
    - in frontend root directory run ```npm install```
    - in frontend root directory run ```npm start```
    - navigate to http://localhost:3000


