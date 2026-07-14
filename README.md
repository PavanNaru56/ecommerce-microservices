ecommerce-microservices/
│
├── README.md
├── api-gateway
└── user-service


order-service will:
Receive productId
Call product-service
verify products exists
fetch price
calculate

totalPrice = productPrice * quantity

Steps for Service to Service communication
----------------------------
Add the Feign dependency
Enable the @EnableFeignCLients in main class
create a DTO exact to the product entity
create a Interface for the ProductClient
@FeignClient(
name = "",
url = ""
)
@GetMapping("/api/products/{Id})
ProductResponse findProductById(@PathVariable Long Id);


Create a dependency in order service
using the productClient fetch the details


Steps for User Context Propagation
-------------------------------------------
Normally we are hardcoded the username in the order-service while creating the order object
Now extract the username and role from the token. In WebFlux the request inside the exchange is immutable So we can't modify the headers
So copy the request from the exchange using the mutate and build it 
then add username and role in the request and pass the modified exchange


Kafka Setup
---------------------------------
story : whenever the user create the order it should notify the user that the order was created

producer - order-service
consumer - notification-service
Topic - order-created


Steps to Dockerize the Application:
--------------------------------------------------
create a DockerFile inside the application
We need the jar file to excute in the container. 
Run mvn clean package --> to generate the jar file 

In Dockerfile
--------------------------------------
FROM eclipse-temurin:17-jre   ---- type of the java version for executing the jar in the container

Why JRE instead of JDK ? because we already build the application so compilation required just we need the runtime environment

WORKDIR /app

Simple like the cd in Linux. in docker app directory will be created

COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

copies the jar file from the source to the app.jar

EXPOSE 8081

port that runs in the container. Should be similar to the service port

ENTRYPOINT ["java", "-jar", "app.jar"]

java -jar app.jar -> Whenever someone starts a container from the image, run the spring boot application

#Steps to execute
---------------------

First we need to build the image

docker build -t user-service:v1 .
build the image of user-service with version v1 in the current directory
-t : tag of the image
tags will vary whenever changes and deployed
docker images

build the container

docker run -d --name user-service -p 8081:8081 user-service:v1

application starts running in the port 8081

docker ps

-----------------------------------------------

#Steps for using the Kafka
---------------------------------------------
pull the image of apache/kafka:latest image from the docker
create a container using the kafka image and port 9092
add the kafka dependencies in order-service and kafka-notification services
Enable the kafka  enabled in the both services to the port 9092
spring.kafka.bootstrap-servers=localhost:9092
And make sure the kafka container running on the same port so that it can communicate with container




