# State Machine Builder

Визуальный конструктор машин состояний на основе Spring State Machine с поддержкой динамического создания и управления через REST API и Kafka.

## Технологический стек

- **Spring Boot 4.0.0** с **Spring Framework 7**
- **Spring State Machine 4.0.0** - для управления состояниями
- **PostgreSQL 17** - основная база данных
- **Liquibase** - миграции базы данных
- **Apache Kafka** - для обработки событий
- **Docker & Docker Compose** - контейнеризация
- **Swagger/OpenAPI** - документация API
- **Cytoscape.js** - визуализация графа состояний

## Основные возможности

1. **Визуальный конструктор** - создание машин состояний через веб-интерфейс
2. **Шаблоны машин состояний** - сохранение и повторное использование конфигураций
3. **Динамическое создание инстансов** - создание экземпляров по шаблону для каждого проекта
4. **REST API** - полное управление через HTTP
5. **Kafka интеграция** - обработка событий через Kafka топики
6. **История переходов** - отслеживание всех изменений состояний
7. **Персистентность** - сохранение состояния в PostgreSQL

## Быстрый старт

### Требования

- Docker и Docker Compose
- Java 21 (для локальной разработки)
- Maven 3.9+ (для локальной разработки)

### Запуск через Docker Compose

```bash
# Клонировать репозиторий
git clone <repository-url>
cd stateMachine

# Запустить все сервисы
docker-compose up -d

# Проверить логи
docker-compose logs -f app
```

Приложение будет доступно по адресу:
- Web UI: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Локальный запуск для разработки

```bash
# Запустить только инфраструктуру (PostgreSQL, Kafka)
docker-compose up -d postgres kafka zookeeper

# Собрать проект
mvn clean package

# Запустить приложение
mvn spring-boot:run
```

## Архитектура проекта

```
stateMachine/
├── src/
│   └── main/
│       ├── java/com/statemachine/builder/
│       │   ├── config/              # Конфигурация Spring
│       │   ├── controller/          # REST контроллеры
│       │   ├── dto/                 # Data Transfer Objects
│       │   ├── kafka/               # Kafka consumers
│       │   ├── model/               # JPA entities
│       │   ├── repository/          # Spring Data repositories
│       │   ├── service/             # Бизнес-логика
│       │   └── statemachine/        # Spring State Machine фабрика
│       └── resources/
│           ├── db/changelog/        # Liquibase миграции
│           ├── static/              # Веб-интерфейс
│           └── application.yml      # Конфигурация приложения
├── Dockerfile
└── docker-compose.yml
```

## Модель данных

### Шаблоны машин состояний (Templates)
- **state_machine_templates** - основная информация о шаблоне
- **template_states** - состояния в шаблоне
- **template_transitions** - переходы между состояниями

### Экземпляры (Instances)
- **state_machine_instances** - работающие экземпляры
- **state_machine_history** - история переходов

## REST API

### Шаблоны

```http
# Создать шаблон
POST /api/templates
Content-Type: application/json

{
  "name": "Order Processing",
  "description": "Order workflow",
  "initialState": "NEW",
  "states": [
    {
      "stateName": "NEW",
      "stateType": "INITIAL"
    },
    {
      "stateName": "PROCESSING",
      "stateType": "NORMAL"
    },
    {
      "stateName": "COMPLETED",
      "stateType": "FINAL"
    }
  ],
  "transitions": [
    {
      "sourceState": "NEW",
      "targetState": "PROCESSING",
      "eventName": "START_PROCESSING"
    },
    {
      "sourceState": "PROCESSING",
      "targetState": "COMPLETED",
      "eventName": "COMPLETE"
    }
  ]
}

# Получить все шаблоны
GET /api/templates

# Получить шаблон по ID
GET /api/templates/{id}

# Удалить шаблон
DELETE /api/templates/{id}
```

### Экземпляры

```http
# Создать экземпляр
POST /api/instances?templateId={templateId}&projectId={projectId}

# Получить экземпляр
GET /api/instances/{id}

# Получить экземпляры проекта
GET /api/instances/project/{projectId}

# Отправить событие
POST /api/instances/{id}/event
Content-Type: application/json

{
  "eventName": "START_PROCESSING",
  "eventData": {
    "userId": "123",
    "timestamp": "2024-01-01T00:00:00Z"
  }
}

# Получить историю переходов
GET /api/instances/{id}/history
```

## Kafka интеграция

### Формат события

```json
{
  "instanceId": "uuid",
  "projectId": "project-123",
  "eventName": "START_PROCESSING",
  "eventData": {
    "key": "value"
  }
}
```

### Отправка события через Kafka

```bash
# Используя kafka-console-producer
docker exec -it statemachine-kafka kafka-console-producer \
  --broker-list localhost:9092 \
  --topic state-machine-events \
  --property "parse.key=true" \
  --property "key.separator=:"

# Затем введите:
key:{"instanceId":"your-instance-id","eventName":"START_PROCESSING","eventData":{}}
```

## Визуальный конструктор

Откройте http://localhost:8080 в браузере для доступа к визуальному конструктору.

### Основные функции:

1. **Добавление состояний** - укажите имя и тип состояния
2. **Добавление переходов** - соедините состояния событиями
3. **Сохранение шаблона** - сохраните конфигурацию в БД
4. **Загрузка шаблонов** - просмотр и редактирование существующих

## Конфигурация

Основные переменные окружения в `docker-compose.yml`:

```yaml
DB_HOST: postgres
DB_PORT: 5432
DB_NAME: statemachine
DB_USER: postgres
DB_PASSWORD: postgres
KAFKA_BOOTSTRAP_SERVERS: kafka:9092
```

## Разработка

### Добавление новых действий (Actions)

1. Расширьте модель `TemplateTransition` или `TemplateState`
2. Добавьте обработчики в `StateMachineFactory`
3. Обновите UI для поддержки новых полей

### Добавление Guard условий

Guards можно добавить через поле `guardExpression` в переходах.

## Примеры использования

### Пример: Workflow обработки заказов

```java
// 1. Создать шаблон через API или UI
// 2. Создать экземпляр для заказа
POST /api/instances?templateId={templateId}&projectId=order-12345

// 3. Отправлять события по мере обработки заказа
POST /api/instances/{instanceId}/event
{
  "eventName": "START_PROCESSING",
  "eventData": {"orderId": "12345"}
}

// 4. Отслеживать текущее состояние
GET /api/instances/{instanceId}

// 5. Просмотреть историю
GET /api/instances/{instanceId}/history
```

## Troubleshooting

### Проблемы с подключением к БД

```bash
# Проверить статус PostgreSQL
docker-compose ps postgres
docker-compose logs postgres
```

### Проблемы с Kafka

```bash
# Проверить топики
docker exec -it statemachine-kafka kafka-topics --list --bootstrap-server localhost:9092

# Создать топик вручную
docker exec -it statemachine-kafka kafka-topics --create \
  --bootstrap-server localhost:9092 \
  --topic state-machine-events \
  --partitions 3 \
  --replication-factor 1
```

## Лицензия

MIT License

## Контакты

Для вопросов и предложений создавайте Issues в репозитории.