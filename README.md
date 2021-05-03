# PurityService

My personal project for "Software Architecture" course in NURE.

This is the program system for automizing cleaning processes for placement owners.

It consists of Backend, Frontend, Mobile Android application and IoT device.

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

```TODO - running via Docker and docker-compose```
