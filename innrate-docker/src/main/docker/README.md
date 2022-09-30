
# Сборка docker image для dashboard service
## Тестирование сборки

Файл .env нужен только, чтоб протестировать сборку image'а из командной строки
```shell script
docker-compose --env-file=.env build
# или короткий вариант - файл .env подхватывается по-умолчанию
docker-compose build
``` 
