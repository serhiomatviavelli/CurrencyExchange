# CurrencyExchange
Данное приложение представляет собой сервис обмена валюты, обновляющее курсы валют с сайта ЦБ один раз в час.

## Используемые технологии

- Spring Boot
- Maven
- Lombok
- Mapstruct
- Liquibase
- PostgreSQL

## Требования

### JDK 17

Проект использует синтаксис Java 17. Для локального запуска вам потребуется
установленный JDK 17.

### Docker
Для запуска проекта вам потребуется установленный и запущенный Docker.
Для запуска БД(PostgreSQL) требуется запустить соответствующий сервис в Docker.

### Подключение к интернету

Подключение к интернету для получения курсов валют.

## Полезные команды

### Запуск контейнера с базой данных

```bash
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=postgres -d postgres
```

Пользователь для подключения к контейнеру `postgres`.

### IntelliJ IDEA

Запустите main метод класса Application

### Запросы API

Создание новой записи о валюте

```bash
curl --request POST \
  --url http://localhost:8080/api/currency/create \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "Доллар Готэм-Сити",
  "nominal": 3,
  "value": 32.2,
  "isoNumCode": 1337
}'
```

Получение Валюты по id

```bash
curl --request GET \
  --url http://localhost:8080/api/currency/1333
```

Конвертация валюты в рубли по числовому коду

```bash
curl --request GET \
--url http://localhost:8080/api/currency/convert/ru?value=100&numCode=840
```

Конвертация валюты по числовому коду

```bash
curl --request GET \
--url http://localhost:8080/api/currency/convert?value=100&numCode=840&toNumCode=933
```