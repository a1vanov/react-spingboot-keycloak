# Особенности

## KeyCloak:18.0 не дает зайти в консоль
KeyCloak:18.0 поднимается, можно перейти на стартовую страницу http://localhost:8080, 
но если перейти в консоль администратора, то будет чистая страница. 
По запросам выглядит так, что где-то вместо http используется https.
Видимо что-то с настройками.
Пока будет использоваться KeyCloak:17.0.1

## http
Чтобы можно было заходить админом по http в master realm нужно прописать: 
Realm -> Login -> Require SSL = NONE
Пока руками, потом добавим в скрипт. 
./bin/kcadm.sh config credentials --server http://localhost:8096 --realm master --user admin 
./bin/kcadm.sh update realms/master -s sslRequired=NONE

В Innrate realm:
Realm -> General -> Frontend URL: http://localhost:8096 - внешний адрес keycloak
Realm -> Security Defences -> HTTP Strict Transport Security: max-age=0; includeSubDomains 

## Realm export
export \ 
  KC_DB=mysql \ 
  KC_DB_URL=jdbc:mysql://innrate-db/keycloak \ 
  KC_DB_USERNAME=keycloak \
  KC_DB_PASSWORD=123456 && \
./bin/kc.sh export --realm Innrate --dir /tmp --users realm_file

## Realm import
export \
  KC_DB=mysql \
  KC_DB_URL=jdbc:mysql://innrate-db/keycloak \
  KC_DB_USERNAME=keycloak \
  KC_DB_PASSWORD=123456 && \
./bin/kc.sh import --file data/import/Innrate-realm.json --override false

## Details
keycloak/keycloak:18.0.0 base on Red Hat Enterprise Linux release 8.6 (Ootpa) 

### Подключиться к контейнеру
docker run --rm -it -u 0 --add-host=host.docker.internal:host-gateway --entrypoint bash innrate/innrate-keycloak

### Установить ping
microdnf install -y ping

### Переключиться на пользователя keycloak
su keycloak -s /bin/bash 

