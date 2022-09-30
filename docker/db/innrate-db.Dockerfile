FROM mysql:8.0

COPY docker/db/docker-entrypoint-initdb.d /docker-entrypoint-initdb.d
