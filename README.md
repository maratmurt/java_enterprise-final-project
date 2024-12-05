# Курс Skillbox "Enterprise-технологии в Java-разработке" - финальная работа
Это микросервисное приложение для управления заказами и обработки платежей. Оно предоставляет REST API для взаимодействия с фронтендом и другими сервисами, а также использует Apache Kafka для обмена сообщениями между сервисами. В состав проекта входят служебные модули:
1. Discovery service - для обнаружения служб и доступа к файлам конфигурации
2. Authentication service - для аутентификации и авторизации пользователей
3. Gateway API - общая точка входа для фронтэнда

и клиентские модули:
1. Order service - для хранения информации о заказах
2. Payment service - для осуществления платежей по заказам
3. Inventory service - служба сборки заказа для последующей отправки
4. Delivery service - доставка заказа клиенту

## Технологии
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Spring Security](https://spring.io/projects/spring-security)
- [PostgreSQL](https://www.postgresql.org/)
- [Apache Kafka](https://kafka.apache.org/)

## Разработка

### Требования
Для установки и запуска проекта необходимы [Maven](https://maven.apache.org/download.cgi) и [Docker](https://www.docker.com/).

### Запуск Development сервера
Чтобы запустить сервер для разработки, выполните команду:
```shell
docker-compose up
```

### Запуск модулей проекта
Сначала поочерёдно запустите служебные модули:
```shell
mvn -f discovery/pom.xml spring-boot:run
```
```shell
mvn -f auth-service/pom.xml spring-boot:run
```
```shell
mvn -f gateway/pom.xml spring-boot:run
```
Затем запустите клиентские модули:
```shell
mvn -f order-service/pom.xml spring-boot:run
```
```shell
mvn -f payment-service/pom.xml spring-boot:run
```
```shell
mvn -f inventory-service/pom.xml spring-boot:run
```
```shell
mvn -f delivery-service/pom.xml spring-boot:run
```

## Интерфейс
Проверить работу эндпоинтов можно следующим адресам:
1. http://localhost:8080/api/order/swagger-ui/index.html
2. http://localhost:8085/api/payment/swagger-ui/index.html
3. http://localhost:8086/api/inventory/swagger-ui/index.html
4. http://localhost:8083/auth/swagger-ui/index.html


## Тестирование
В проекте предусмотрены базовые тесты с использованием JUnit, Mockito и TestContainers.
```shell
mvn test
```

- [Марат Муртузалиев](https://t.me/marat_murtuzaliev) — Java backend developer