# KQuiz
[![codebeat badge](https://codebeat.co/badges/532250f5-5bb9-4a9d-97ee-a658e19caddf)](https://codebeat.co/projects/github-com-albatovk-kquiz-master) [![CodeFactor](https://www.codefactor.io/repository/github/albatovk/kquiz/badge)](https://www.codefactor.io/repository/github/albatovk/kquiz)
[![CI Build and Test](https://github.com/AlbatovK/KQuiz/actions/workflows/main.yml/badge.svg)](https://github.com/AlbatovK/KQuiz/actions/workflows/main.yml)
# Описание проекта
Проект представляет собой мобильное приложение, выполнящее роль платформы для создания, поиска, организации и выполнения онлайн тестов или викторин. Также в состав проекта входит [серверное приложение (ссылка на проект)](https://github.com/AlbatovK/Simpriser), обеспечивающее возможность сетевой игры в режиме реальном времени с неопределённым количеством других игроков.
### Цели проекта
* Модернизация учебного процесса с использованием платформы онлайн тестов
* Упрощение интерфейса организации и прохождения учебного тестирования
* Увеличение доступности и надёжности контроля знаний с использованием распространённых Android устройств
* Развитие заинтересованности учеников в процессе с помощью соревновательной системы в реальном времени

Проект может быть использован не только в рамках учебной деятельности, но и для корпоративных квизов, для развлечения в компании друзей или с семьёй в свободное время благодаря встроенному сетевому режиму и свободе в выборе тем и создании собственных викторин.
# Техническая характеристика проекта
* Стэк технологий
    * Kotlin Coroutines - выполнение асинхронных операций для работы с сетью
    * Retrofit - быстрый и удобный доступ к API со стороны клиента
    * Spring Boot Framework - web-приложение развёрнутое на [хостинге (ссылка на сервер)](https://floating-eyrie-81969.herokuapp.com) Heroku
    * Swagger2 - автоматическое создание [документации API](https://floating-eyrie-81969.herokuapp.com/swagger-ui.html)
    * Google Firebase Filestore - серверная документоориентированная NoSql база данных
    * Android Jetpack
        * Fragment - использование нескольких экранов в рамках одной активности
        * Navigation - навигация между фрагментами в рамках одной активности
        * Navigation SafeArgs Plugin - безопасная передача данных между фрагментами
        * Lifecycle - обработка событий на основе жизненного цикла приложения
        * ViewModel - хранение и использование данных относящихся к UI в привязке к жизненному циклу представления
        * RecyclerView - представление данных в виде интерактивного списка
        * ViewBinding - современный способ доступа к элементам разметки
* Современная архитектура
    * Многоуровневая архитектура
    * Паттерн проектирования MVVM на стороне клиента
    * Spring MVP на стороне сервера
    * Koin - Инъекция зависимостей
    * JUnit4 и Espresso - модульное и инструментальное тестирование
* Continious Integration
    * GitHub Actions - сборка и анализ артефакта приложения с помощью Gradle
    * Автоматический анализ кода с помощью сторонних сервисов (Codebeat, Codefactor)
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
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/main_screen.jpg?raw=true)       | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/full_list.jpg?raw=true)       |
| -------------- | -------------- |
| ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/list_filter_name.jpg?raw=true)   | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/list_filter_topic.jpg?raw=true)    |
| ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/name_host.jpg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/info_host.jpg?raw=true) |
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/client_name.jpg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/host_list.jpg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/ex_1.jpg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/ex_2.jpg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/ex_3.jpg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/res_1.jpg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/res_2.jpg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/res_3.jpg?raw=true)
![](https://github.com/AlbatovK/KQuiz/blob/master/assets/pedestal.jpg?raw=true) | ![](https://github.com/AlbatovK/KQuiz/blob/master/assets/creator_1.jpg?raw=true)
