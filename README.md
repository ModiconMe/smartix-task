> ### Smartix
[![CI](https://github.com/ModiconMe/smartix-task/workflows/CI/badge.svg)](https://github.com/alexey-lapin/realworld-backend-spring/actions)

http://localhost:8080/swagger-ui.html - swagger ui клиент.
http://localhost:8080/api-docs - документация.

### Функционал
Доступен без авторизации.
* РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЕЙ. Регистрация пользователей по номеру телефона и паролю. Валидация пароля(от 8 до 32 символов) и номера телефона(номер должен быть российский +79132004141).
Возвращает jwt токен.

Требуется авторизация.
* ПОЛУЧЕНИЕ БАЛАНСА ПОЛЬЗОВАТЕЛЯ. Возвращает текущий баланс пользователя.
* ОПЛАТА. Требуется авторизация. Можно переводить денежные средства другим пользователям по номеру телефона.
* РЕДАКТИРОВАНИЕ ПОЛЬЗОВАТЕЛЬСКИХ ДАННЫХ. Можно редактировать пользовательские данные. Валидация email(должен быть формата email).
* ИСТОРИЯ ОПЕРАЦИЙ. Возращает список проведенных оплат пользователю. Поддерживается пагинация.
* ЛОГИН. Возвращает jwt токен при успешной авторизации.

### Technology
- Spring Boot 3.0.1 и Java 17
- Spring Data JPA + PosgreSQL
- OpenApi
- Logback
- JUnit 5 + Mockito для тестирования
- Docker + Docker-compose
- GitHub actions в качестве CI

### Getting started
Требуется Java 17 и PostgreSQL или выше

    ./gradlew bootRun

Или запустить с помощью Docker

    docker compose up

Deploy версия с секретными полями определенными в файле .env

    `docker compose -f docker-compose-deploy.yml up`

