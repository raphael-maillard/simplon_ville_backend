# app-backend

This component provided api to interact with database.
This is the project backend core.

## Teicno
* Java 11
* Maven
* MySQL
* Spring

## Use-to

### Compilation 

```java
mvn clean install
```

### Open-Api UI
* To get a visual representation of API, and test your API : 
```shell
http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config
```

### Database Migration

* Database migration is the process used to update the database schéma.
* This process is managed by flyway
* File to update database schema have to be set in `flywaydb/src/main/resources/db/migration/`
* Migration file are named with the following pattern `V<version +1>__<description>.sql`

````shell
# launch a database update 
mvn clean flyway:migrate -pl flyway -Dflyway.configFiles=src/main/resources/flyway.conf
````

## Development Environment

### Application
#### Run

```shell
cd api
mvn spring-boot:run
```

#### Development mode
* Spring offer you a hot reload mode.
* After a code modification, you don't need to restart your server.
  * only recompile your app with : 
  ````shell
    mvn compile
  ````

### Database

* Database is provided in a docker container
* You must have the last version of docker installed and docker-compose
* MySQL is exposed on your local port 3306

#### Start Database
* The following command will start MySQL and phpmyadmin.
* phpmyadmin will be available on the following url http://localhost:8008
    * serveur bdd : db / login : root / password : example
* Data are store in the directory : database, please take care to not remove it to prevent loosing data  
    
```shell
docker-compose up
```

#### Stop Database
* The following command will stop the database and delete the container
```shell
ctrl + c
# OR
docker-compose down -v
```

#### Database User Interface
* There is a phpMyAdmin installed beside the database
```shell
http://localhost:8008
```

