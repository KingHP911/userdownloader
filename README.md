Загрузка пользователей с randomuser.me.

В репозитории реализована загрузка пользователей с ресурса randomuser.me и экспорт в csv файл. Реализация представляет из себя web-приложение на spring boot. В приложении реализован rest controller (класс DownloaderController) с 2-я методами: download (post), export (get). Которые ссылаются на соответствующие методы сервисного слоя (интерфейс UserService и имплементирующий его класс UserServiceImpl). 

Загружаются данные с помощью объекта RestTemplate в объекты, описанные в пакете randuserdata, и далее преобразуются в объекты User. User – описывает сущности, хранимые в базе (Postgre SQL). Взаимодействие с базой осуществляем через UserRepository, расширяющий CrudRepository.

Экспорт в файл осуществляется с помощью библиотеки opencsv с использованием HttpServletResponse.

В ответах используется отображение сущностей User через UserDto, на прямую entity не отправляем, конвертируем с помощью ModelMapper.
Тесты приложения можно посмотреть в UserdownloaderApplicationTests.


Возможные дополнения, замечания:
•	Класс user может содержать и больше полей. Для этого надо или расширить количество pojo, отображающих загружаемые сущности, или парсить json (csv) в user непосредственно по строкам.
•	В параметры запроса можно добавить параметр format (по аналогии с randomuser.me) для выбора типа экспортируемых файлов. И далее использовать соответствующие библиотеки для их создания.
•	Возможно, для выгрузки можно использовать не HttpServletResponse, а что-то более подходящее.
