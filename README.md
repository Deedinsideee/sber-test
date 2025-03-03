# Subscription Service

Этот проект представляет собой сервис управления подписками на книги.

## Тестовые данные

### Инициализация базы данных

Используйте следующий SQL-скрипт для загрузки тестовых данных:

```sql
-- Добавляем книги
INSERT INTO books (id, title, author, published_date) VALUES
  ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Властелин колец', 'Толкин', '1954-07-29'),
  ('a5c1e1fa-7bd2-4de5-9e71-8d3645c13e93', 'Хоббит', 'Толкин', '1937-09-21'),
  ('7c9e6679-7425-40de-944b-e07fc1f90ae7', 'Мартин Иден', 'Джек Лондон', '1909-01-01');

-- Добавляем подписки
INSERT INTO subscriptions (id, username, user_full_name, active) VALUES
  ('1b9d6f5a-8b72-4e33-bc4f-f2db27362b68', '1', 'Иван Иванов', TRUE),
  ('6a4a50e9-3cfa-46e1-9731-9c62f7db995a', '2', 'Петр Петров', TRUE);

-- Связываем подписки с книгами
INSERT INTO subscription_books (subscription_id, book_id, issue_date) VALUES
  ('1b9d6f5a-8b72-4e33-bc4f-f2db27362b68', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', '2025-02-01'),
  ('1b9d6f5a-8b72-4e33-bc4f-f2db27362b68', 'a5c1e1fa-7bd2-4de5-9e71-8d3645c13e93', '2025-02-03'),
  ('6a4a50e9-3cfa-46e1-9731-9c62f7db995a', '7c9e6679-7425-40de-944b-e07fc1f90ae7', '2025-02-05');
```

## Импорт старых подписок

Для загрузки старых подписок из JSON-файлов можно через API.
Данные попадут в буферную таблицу legacy_subscription. После по крону данные будут вычитаны фоновой задачей.
Задача разложит данные по нужным таблицам

- **Запрос на загрузку старых данных**:
  ```json
  [
  {
    "userFullName": "ЛЕв Петров",
    "username": "3",
    "bookName": "Мартин Иден",
    "bookAuthor": "Джек Лондон",
    "userActive": true
  },
  {
    "userFullName": "ЛЕв Петров",
    "username": "3",
    "bookName": "Война и мир",
    "bookAuthor": "Толстой",
    "userActive": true
  }
  ]

## Поиск существующего абонимента по ФИО пользователя
**Пример ответа:**
```json
{
  "userFullName": "Иван Иванов"
}
```

**Пример ответа:**
```json
{
  "username": "1",
  "userFullName": "Иван Иванов",
  "active": true,
  "books": [
    {
      "title": "Властелин колец",
      "author": "Толкин",
      "publishedDate": "1954-07-29"
    },
    {
      "title": "Хоббит",
      "author": "Толкин",
      "publishedDate": "1937-09-21"
    }
  ]
}
```

