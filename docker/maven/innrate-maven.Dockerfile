
FROM maven:3.8.6 as innrate-agent
ARG PROJECT_NAME=innrate-backend

WORKDIR /opt/innrate
COPY . /opt/innrate
# Этого не достаточно, чтобы уйти в полностью offline режим, так как некоторые плагины подгружают зависимости динамически.
# Для того, что получить полностью оффлайн, можно воспользоваться плагином https://github.com/qaware/go-offline-maven-plugin
# и прописать динамические зависимости тоже
RUN mvn dependency:go-offline -DexcludeReactor=false
