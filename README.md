# TYPER :soccer:

Typer is an application that allows you to bet on match results. It was created for amateur purposes e.g. betting on matches with friends or work colleagues. The application using the RESTful API allows administrator to register and manage users participating in the game, add and manage tournaments (e.g. world championships, league games, champions league), add and manage matches assigned to a given torunament, as well as set rules for awarding points – a given rule is assigned to a given tournament. It is also possible to add matches using batch file to make it easier to add more matches at once. Of course the application also allows user to create and manage bets, bet can be created and edited freely until the match starts. After the match ends, the administrator enters the match result and closes the match, during this operation the points obtained by users are counted. 

# Technologies

The backend part is written in Java using the Spring framework. Application is secured with Spring Security using token-based authentication. Below is the list of used technologies:
* Java 17,
* Spring Boot 3,
* Spring JPA with Hibernate,
* PostgreSQL,
* Maven,
* Spring Security (OAuth2 with JWT),
* Spring Batch,
* Lombok,
* MapStruct,
* Swagger 3

# API documentation

Complete API documentation can be obtained when application is running locally by entering the following address in web browser:

```
http://localhost:8080/typer-documentation
```

# :hammer_and_wrench: Installation steps:

Assuming you are running docker engine locally, please follow this steps to run application: 

* Clone the project to your local repository:
```
git clone https://github.com/rafikos97/typer.git
```
* Using terminal, run the following command in root directory:
```
docker compose up --build
```
And there you go! Backend server, database and Angular app are up and running :rocket:

# How to use?
* All API endpoints are exposed on  localhost:8080. For the details please refer to API documentation.
* Angular app is listening on localhost:4200, you can access it by entering the following address in web browser:
```
http://localhost:4200/
```

