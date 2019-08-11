##### _To Shard, or Not to Shard_
###### No, don't think so.

Running the application locally

There are several ways to run a application on your local machine. One way is to execute the main method in the ShardsApplication class from your IDE.

File -> Import -> Existing Maven Project -> Navigate to the project folder
Select the project
Choose the Spring Boot Application file (search for @SpringBootApplication)
Right Click on the file and Run as Java Application
Alternatively you can use the Spring Boot Maven plugin like so:

`mvn clean package  
cd payments-api  
mvn spring-boot:run`



Реализовать сервис обработки платежей. 
Данные платежа: отправитель, получатель, сумма.
Сервис использует механизм шардирования для хранения платежей в 3 базах данных.
Информация о платеже сохраняется в одной из трех БД для распределения нагрузки.

Требуется реализовать api:
Загрузка списка платежей;
Выдача общей суммы потраченных средств по отправителю.