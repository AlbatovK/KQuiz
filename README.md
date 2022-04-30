# KQuiz
[![codebeat badge](https://codebeat.co/badges/532250f5-5bb9-4a9d-97ee-a658e19caddf)](https://codebeat.co/projects/github-com-albatovk-kquiz-master) [![CodeFactor](https://www.codefactor.io/repository/github/albatovk/kquiz/badge)](https://www.codefactor.io/repository/github/albatovk/kquiz)
[![CI Build and Test](https://github.com/AlbatovK/KQuiz/actions/workflows/main.yml/badge.svg)](https://github.com/AlbatovK/KQuiz/actions/workflows/main.yml)
# Описание проекта
Проект представляет собой мобильное приложение-клиент, выполнящее роль платформы для поиска, организации и выполнения онлайн тестов или викторин. Также в состав проекта входит серверное приложение, обеспечивающее возможность сетевой игры в режиме реальном времени с неопределённым количеством других игроков.
### Цели проекта
* Модернизация учебного процесса с использованием платформы онлайн тестов
* Упрощение интерфейса организации и прохождения учебного тестирования
* Увеличение доступности и надёжности контроля знаний с использованием распространённых Android устройств
* Развитие заинтересованности учеников в процессе с помощью соревновательной системы в реальном времени
# Техническая характеристика проекта
 * Стэк технологий
    * Kotlin + Coroutines - выполнение асинхронных операций в отдельном потоке
    * Retrofit - быстрый и безопасный доступ к API со стороны клиента
    * Spring Boot Framework - web-приложение развёрнутое на Heroku
    * Google Firebase Filestore - серверная NoSql база данных
    * Android Jetpack
        * Navigation - навигация между фрагментами в раиках одной активности
        * Navigation SafeArgs Plugin - безопасная передача данных между пунктами назначения
        * Lifecycle - обработка событий на основе жизненного цикла приложения
        * ViewModel - хранение и использование данных относящихся к UI в привязке к жизненному циклу представления
        * Android KTX - набор расширений для Котлина
        * Fragment - использование нескольких экранов в рамках одной активности
        * ViewBinding - современный способ доступа к элементам разметки
* Современная архитектура
    * Многоуровневая архитектура
    * Паттерн проектирования MVVM на стороне клиента
    * Spring MVP на стороне сервера
    * Koin - Инъекция зависимостей
    * JUnit4 и Espresso - модульное и инструментальное тестирование
* Continious Integration
  * GitHub Actions - сборка и анализ артефакта приложения с помощью Gradle
  * Автоматический анализ кода с помощью сторонних сервисов
* UI дизайн
    * Material design
    * FlexBox

# Базовая структура
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/circles.drawio.svg?raw=true)

# Слои приложения
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/layers.drawio.svg?raw=true)

# Структура интерфейса
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/app_structure.drawio.svg?raw=true)

# Структура базы данных
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/db_export.drawio.svg?raw=true)

# Скриншоты работы приложения
 ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/enter.jpeg?raw=true)       | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/list.jpeg?raw=true)       |
| -------------- | -------------- |
| ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/search_1.jpeg?raw=true)   | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/search_2.jpeg?raw=true)    |
| ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/name_host.jpeg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/host.jpeg?raw=true) |
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/host_info.jpeg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/name_client.jpeg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/client_ex.jpeg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/client.jpeg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/ex_1.jpeg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/ex_2.jpeg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/ex_3.jpeg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/res_1.jpeg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/res_2.jpeg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/res_3.jpeg?raw=true)
### Inspired by Kolodin Dmitriy
