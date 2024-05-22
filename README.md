Симулятор банковского приложения на Spring Boot + PostgreSql. Поиск данных ведется через JPA(в т.ч пагинация и сортировка). 
В рамках задачи создана базовая сущность "Users" с наличием баланса и первоначального депозита. Сущность "Accounts" не создана, поскольку в рамках задачи
у каждого User может быть только один счет. 

Для начала работы:
1) Требуется подключиться к серверу PostegreSql и создать базу данных, используя v1__init.sql
2) Загрузить в БД ряд тестовых данных, выполнив скрипт V2__initValues.sql

Запустив "BanksampleApplication" - функционал можно попробовать на Swagger http://localhost:8080/swagger-ui/index.html#/

1) Авторизуемся в системе, введя пару {"login":"user9", "password":"pass5"}. Система вернет JWT-токен
2) При помощи специальной кнопки "Authorize" в рамках интерфейса Swagger - вводим полученный JWT-токен
3) Нам доступен функционал: изменения/удаления ряда данных. В частности User может удалить либо телефон, либо email,
 с сохранением хотя бы одного контакта. В противном случае, система вернет исключение. Изменение телефона или email на пустое значение
приравнивается к удалению.
4) Поиск по фильтрам с сортировкой и пагинацией, где этого требует задача.
   - Поиск по имени выдает значения по типу Like формата, допуская наличие похожий значений, можно фильтровать по различным полям, добавлена пагинация по умолчанию
   - Поиск по дате может выдавать список значение больших дат от искомой, можно фильтровать по различным полям, добавлена пагинация по умолчанию
   - Поиск по email - возвращает только одно значение или отстуствие такового
   - Поиск по telephone - возвращает только одно значение или отстуствие такового
5) Каждые 60 секунд система индексирует текущий баланс пользователей на 5%, но не более 207% от первоначального депозита.
6) В системе между пользователями можно переводить денежные средства, но не более тех, что находятся на счету.
   При этом отправитель и адресат должны сущестовать в системе. Отправитель должен иметь валидный JWT-токен. Сумма к отправлению должна быть положительной, отрицательные значения нельзя переводить.
8) В системе предусмотрено логгирование через slf4j в файл appLog.log
9) Модуль тестирования функции "Перевода средств" - происходит в классе "UserControllerTests"
   
