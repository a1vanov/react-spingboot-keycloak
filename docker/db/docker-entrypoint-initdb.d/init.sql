
-- KeyCloak
CREATE DATABASE keycloak;
CREATE USER keycloak IDENTIFIED BY '123456';
GRANT ALL ON keycloak.* TO 'keycloak'@'%';

-- InnRate
CREATE DATABASE innrate;
CREATE USER innrate IDENTIFIED BY '123456';
GRANT ALL ON innrate.*  TO 'innrate'@'%';