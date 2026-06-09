# Dublee — Backend Roadmap (50 задач)

> Статус: ✅ Завершён  
> ## 📊 Прогресс: 50/50

---
## 1. Подготовка и настройка (10)
- [x] 1.1 Создать Ktor проект через start.ktor.io (Netty, HOCON)
- [x] 1.2 Настроить Gradle с Kotlin 2.0+ и Ktor 3.1+
- [x] 1.3 Добавить зависимости: Exposed, HikariCP, PostgreSQL, JWT, BCrypt, FCM
- [x] 1.4 Настроить плагины: ContentNegotiation, Authentication, CORS, CallLogging
- [x] 1.5 Подключить логирование (Logback)
- [x] 1.6 Запустить локальный PostgreSQL в Docker (volume для данных)
- [x] 1.7 Настроить Git-репозиторий и защиту от секретов (.gitignore)
- [x] 1.8 Инициализировать Firebase Admin SDK (сервисный аккаунт)
- [x] 1.9 Создать базовые утилиты (хелперы, расширения)
- [x] 1.10 Настроить централизованную обработку ошибок (StatusPages)

---
## 2. База данных и модели (10)
- [x] 2.1 Создать DatabaseFactory с HikariCP
- [x] 2.2 Описать таблицу Users (id, login, password_hash, pair_id, fcm_token, icon_id, color_id)
- [x] 2.3 Описать таблицу Pairs (id, user1_id, user2_id, invite_code)
- [x] 2.4 Описать таблицу Likes (id, user_id, option_id, created_at)
- [x] 2.5 Описать таблицу Matches (id, pair_id, option_id, matched_at)
- [x] 2.6 Автоматическое создание таблиц через Exposed SchemaUtils
- [x] 2.7 Реализовать репозитории (UserRepository, PairRepository)
- [x] 2.8 Реализовать репозитории (LikeRepository, MatchRepository)
- [x] 2.9 Добавить маппинг row ↔ domain модели
- [x] 2.10 Настроить пул соединений (HikariCP)

---
## 3. Безопасность и API (10)
- [x] 3.1 Реализовать PasswordHasher (BCrypt)
- [x] 3.2 Реализовать JwtConfig (генерация и верификация)
- [x] 3.3 Настроить Ktor Authentication plugin с JWT
- [x] 3.4 Создать хелпер `requireUserId()`
- [x] 3.5 Реализовать эндпоинт POST /api/auth/register
- [x] 3.6 Реализовать эндпоинт POST /api/auth/login
- [x] 3.7 Реализовать эндпоинты GET/PATCH /api/users/me
- [x] 3.8 Реализовать эндпоинты пар: create, join, leave, invite-code
- [x] 3.9 Добавить DTO для запросов и ответов
- [x] 3.10 Защитить все роуты, кроме /auth, через authenticate("auth-jwt")

---
## 4. Бизнес-логика и уведомления (10)
- [x] 4.1 Реализовать ActivityService (обработка лайка)
- [x] 4.2 Реализовать проверку встречного лайка при processLike
- [x] 4.3 Автоматическое создание мэтча при взаимном лайке
- [x] 4.4 Удаление обоих лайков после мэтча
- [x] 4.5 Реализовать эндпоинты GET /activity/likes и /activity/matches
- [x] 4.6 Реализовать эндпоинт POST /activity/likes/add
- [x] 4.7 Реализовать удаление лайка DELETE /activity/likes/{id}
- [x] 4.8 Интегрировать NotificationService в процесс мэтча
- [x] 4.9 Отправка FCM-уведомлений обоим участникам пары
- [x] 4.10 Обновление FCM-токена через PATCH /api/users/me

---
## 5. Завершение и документация (10)
- [x] 5.1 Добавить health-check эндпоинт (GET /health)
- [x] 5.2 Настроить детальное логирование запросов (CallLogging)
- [x] 5.3 Подготовить Postman коллекцию для всех эндпоинтов
- [x] 5.4 Написать README (роадмап)
- [x] 5.5 Записать демо-видео работы API
- [x] 5.6 Очистить историю Git от секретов (firebase-service-account.json)
- [x] 5.7 Добавить пример env-конфигурации (application.example.conf)
- [x] 5.8 Написать модульные тесты для репозиториев
- [x] 5.9 Настроить CORS для разработки
- [x] 5.10 Подготовить docker-compose для сервера + БД

---

**Автор:** Епифанов М.О.

**Проект:** Dublee  

**Стек:** Kotlin + Ktor 3.x + Exposed + PostgreSQL + JWT + BCrypt + FCM
