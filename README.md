
# URLs
Service 
http://localhost:8090/ 
user: user1 
password: 123456 

Swagger 
http://localhost:8090/swagger-ui/index.html 

KeyCloak 
http://localhost:8096/ 

user: admin 
password: 123456 
 
Database 
http://localhost:3306/innrate 

user: innrate 
password: 123456 

user: root 
password: 123456 

# Ports
# Порты Dashboard
- 8090 - Innrate service - http
- 8092 - Innrate service - https
- 8094 - Innrate service - JMX
- 5005 - Innrate service - debug
- 8096 - KeyCloak - http
- 8098 - KeyCloak - https

# Commands
## Start on 188.225.76.20
docker-compose --env-file=dc-vm.env up innrate-keycloak innrate-service 

## Start all services (innrate-db, innrate-keycloak, innrate-service)
docker-compose up -d

## Stop and remove all services 
docker-compose down

## Rebuild and start all service
docker-compose up --build -d

## Start particular service
docker-compose up innrate-db -d

## Stop and remove particular service 
docker-compose down innrate-db

# Liquibase changelog
innrate-backend/src/main/resources/database/migration

# forwarding
https://keycloak.discourse.group/t/get-internal-wrong-redirect-uri/181

## issue
q: Repeated line in log "liquibase.lockservice: Waiting for changelog lock...."
a: Sometimes if the update application is abruptly stopped, then the lock remains stuck 
execute
UPDATE DATABASECHANGELOGLOCK SET LOCKED=0, LOCKGRANTED=null, LOCKEDBY=null where ID=1;

