CREATE DATABASE innrate;
--ALTER DATABASE innrate SET timezone TO 'UTC';
CREATE USER innrate IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON innrate.*  TO 'innrate'@'%';


