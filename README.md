### Тестовое задание для dfsystems

####Сборка и тесты  
./gradlew jibDockerBuild  
  
####Запуск  
docker run -p [port]:8080 --env-file [env_file_name] --name dfsystems_test_task_AntonShumilin dfsystems_test_task:AntonShumilin

где  
port - порт хост системы, на котором будет доступно приложение  
  
env_file_name - файл с переменными окружения:  
  
  APP_ID - ID приложения, зарегистрированного в VK  
CLIENT_SECRET - SECRET приложения, зарегистрированного в VK  
POSTGRES_URL - JDBC URL базы данных  
POSTGRES_USER - пользователь БД  
POSTGRES_PSW - пароль БД  
  
  
Кроме того на странице настроек приложения VK нужно указать разрешенные редиректы:  
http://localhost:[port]/user"  
http://localhost:[port]/friends"  
  
Cхема БД:

create table vk_request (  
ID BIGSERIAL not null primary key,  
CREATED timestamp not NULL,  
PARAMS text  
);  
  
  
create table vk_group (  
ID BIGSERIAL not null primary key,  
group_info text not null,  
request_id INT,  
 CONSTRAINT fk_vk_request  
    FOREIGN KEY(request_id)  
    REFERENCES vk_request(id)  
);

####Описание

У приложения 3 эндпоинта:  

/user - ищет и отдает все группы пользователя, при первом запросе браузер будет переадресован на страницу авторизации VK  
Необязательный параметр запроса substring - строка поиска по именам групп  
Результат запроса сохраняется в БД  
  
 /friends - ищет и отдает все группы пользователя и его друзей, исключая дубликаты  
Необязательный параметр запроса substring - строка поиска по именам групп  
 
/all_groups - отдает из БД все схраненные запросы и их результаты  
Пагинация задается набором стандартных параметров page, size, sort  
 
