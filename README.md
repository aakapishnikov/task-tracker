# Task Tracker Backend

Учебный backend‑сервис для трекинга задач с разделением прав:
- обычный пользователь (USER) — работает только со своими задачами и группами;
- администратор (ADMIN) — может смотреть всех пользователей и их задачи, а также общую статистику.

---

## 1. Требования

- Java 17  
- Docker и docker-compose  
- (опционально) IntelliJ IDEA  

База данных: **PostgreSQL**.  
По умолчанию используется база `tasktracker`, пользователь `taskuser`, пароль `taskpass`.

---

## 2. Как запустить СУБД через docker-compose

В корне проекта лежит файл `docker-compose.yml`.

1. Открыть терминал в корне проекта:

```bash
cd task-tracker
```

2. Поднять контейнер с БД:

```bash
docker-compose up db
```

3. Проверить, что БД запустилась:

```bash
docker ps
```

В списке должен быть контейнер `tasktracker-db` со статусом `Up`.

После старта БД доступна по адресу:

- host: `localhost`
- port: `5432`
- database: `tasktracker`
- user: `taskuser`
- password: `taskpass`

---

## 3. Как запустить приложение

### Вариант A. Полностью через Docker

1. Собрать jar‑файл:

```bash
./gradlew clean bootJar
```

2. Собрать и запустить контейнер приложения и БД:

```bash
docker-compose up --build
```

3. После старта приложение доступно по адресу:

```text
http://localhost:8080
```

4. Остановить контейнеры:

```bash
docker-compose down
```

---

### Вариант B. Запуск из IntelliJ IDEA (БД в Docker)

1. Поднять только базу данных:

```bash
docker-compose up db
```

2. Открыть проект в IntelliJ IDEA.  
3. В `File → Project Structure…` выставить **Project SDK = Java 17**.  
4. Найти класс:

```text
src/main/java/com/example/tasktracker/TaskTrackerApplication.java
```

5. Открыть этот класс и запустить метод `main` (зелёный треугольник слева).

Приложение будет доступно по адресу:

```text
http://localhost:8080
```

---

## 4. Авторизация и JWT

Для всех защищённых эндпоинтов требуется заголовок:

```http
Authorization: Bearer <JWT_TOKEN>
```

JWT‑токен выражается в виде строки, которую приложение возвращает после регистрации или логина.

Роли пользователей:

- `USER` — обычный пользователь;
- `ADMIN` — администратор (доступ к `/api/admin/**`).

---

## 5. Основные эндпоинты и рабочие примеры

Базовый URL: `http://localhost:8080`.

### 5.1. Аутентификация (общедоступные эндпоинты)

| Метод | URL                  | Описание                    |
|-------|----------------------|-----------------------------|
| POST  | `/api/auth/register` | Регистрация пользователя    |
| POST  | `/api/auth/login`    | Логин, выдача JWT‑токена    |

**Пример запроса (register/login):**

```json
{
  "email": "user@example.com",
  "password": "secret"
}
```

**Пример ответа:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 5.2. Эндпоинты обычного пользователя (роль USER или ADMIN)

Для всех запросов ниже требуется заголовок:

```http
Authorization: Bearer <JWT_TOKEN>
```

#### Группы задач

| Метод | URL                    | Описание                      |
|-------|------------------------|-------------------------------|
| GET   | `/api/groups`          | Список групп текущего пользователя |
| POST  | `/api/groups`          | Создать группу                |
| PUT   | `/api/groups/{id}`     | Обновить название группы     |
| DELETE| `/api/groups/{id}`     | Удалить группу                |

**Пример создания группы:**

```json
{
  "name": "Учёба по Java"
}
```

**Пример обновления группы:**

```json
{
  "name": "Spring Boot"
}
```

#### Задачи

| Метод | URL                    | Описание                      |
|-------|------------------------|-------------------------------|
| GET   | `/api/tasks`           | Список задач текущего пользователя |
| POST  | `/api/tasks`           | Создать задачу                |
| PUT   | `/api/tasks/{id}`      | Обновить задачу               |
| DELETE| `/api/tasks/{id}`      | Удалить задачу                |

**Пример создания задачи:**

```json
{
  "title": "Разобраться с JWT",
  "description": "Изучить фильтры и конфигурацию Spring Security",
  "status": "PLANNED",
  "groupId": 1
}
```

**Пример частичного обновления задачи:**

```json
{
  "status": "IN_PROGRESS"
}
```

---

### 5.3. Админские эндпоинты (роль ADMIN)

Чтобы назначить пользователю роль ADMIN, нужно в БД выполнить:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

После этого необходимо заново залогиниться (`/api/auth/login`), чтобы получить новый токен уже с ролью ADMIN.

| Метод | URL                               | Описание                            |
|-------|------------------------------------|-------------------------------------|
| GET   | `/api/admin/users`               | Список всех пользователей           |
| GET   | `/api/admin/tasks`               | Список всех задач                   |
| GET   | `/api/admin/groups`              | Список всех групп задач             |
| GET   | `/api/admin/users/{userId}/tasks`| Задачи конкретного пользователя     |
| GET   | `/api/admin/stats/statuses`      | Статистика задач по статусам        |

**Пример ответа `/api/admin/stats/statuses`:**

```json
[
  { "status": "PLANNED", "count": 3 },
  { "status": "IN_PROGRESS", "count": 2 },
  { "status": "DONE", "count": 5 }
]
```

---

## 6. Рекомендуемый порядок работы с API

### Для обычного пользователя (USER)

1. Зарегистрироваться или залогиниться:
   - `POST /api/auth/register`
   - или `POST /api/auth/login`
2. Скопировать токен из ответа.
3. Создать одну или несколько групп:
   - `POST /api/groups`
4. Создать задачи:
   - `POST /api/tasks`
5. Посмотреть свои задачи и группы:
   - `GET /api/tasks`
   - `GET /api/groups`
6. При необходимости обновлять и удалять:
   - `PUT /api/tasks/{id}`
   - `DELETE /api/tasks/{id}`
   - `PUT /api/groups/{id}`
   - `DELETE /api/groups/{id}`

### Для администратора (ADMIN)

1. Назначить роль ADMIN нужному пользователю в БД.
2. Залогиниться под ним (`/api/auth/login`) и получить новый токен.
3. Использовать админские эндпоинты:
   - `GET /api/admin/users`
   - `GET /api/admin/tasks`
   - `GET /api/admin/groups`
   - `GET /api/admin/users/{userId}/tasks`
   - `GET /api/admin/stats/statuses`

---

## 7. Проверка сервиса через Swagger UI

Swagger UI подключён через библиотеку `springdoc-openapi-starter-webmvc-ui`.

После запуска приложения открой в браузере:

```text
http://localhost:8080/swagger-ui/index.html
```

В Swagger UI будут доступны три блока:

- **Auth** — регистрация и логин;
- **Tasks** — работа с задачами и группами для текущего пользователя;
- **Admin** — админские операции.

### Как работать с JWT в Swagger

1. В блоке **Auth** выполнить запрос `POST /api/auth/register` или `POST /api/auth/login` с телом:

   ```json
   {
     "email": "user@example.com",
     "password": "secret"
   }
   ```

2. Скопировать `token` из ответа.
3. Нажать кнопку **Authorize** в правом верхнем углу.
4. Ввести в поле:

   ```text
   Bearer <полученный_токен>
   ```

5. Нажать **Authorize** → **Close**.
6. Теперь можно вызывать защищённые эндпоинты из блоков **Tasks** и **Admin** напрямую из Swagger UI.

Для роли ADMIN алгоритм такой же, только логин выполняется для пользователя, у которого в БД выставлена роль `ADMIN`.