##### _To Shard, or Not to Shard_
###### No, don't think so.

Running the application locally

There are several ways to run a application on your local machine. One way is to execute the main method in the ByKeyApplication class from your IDE.  

File -> Import -> Existing Maven Project -> Navigate to the project folder  
Select the project  
Choose the Spring Boot Application file (search for @SpringBootApplication)  
Right Click on the file and Run as Java Application  
Alternatively you can use the Spring Boot Maven plugin like so:  
```
mvn clean package  
cd payments-api  
mvn spring-boot:run
```

Now you can test it in your browser by visiting http://localhost:8080/swagger-ui.html



Implement a payment processing service.  
Payment details: sender, receiver, amount.  
The service uses a sharding mechanism to store payments in 3 databases.  
Payment information is stored in one of three databases for load balancing.  

Required to implement api:  
Load a list of payments;  
The issuance of the total amount spent the sender. 
