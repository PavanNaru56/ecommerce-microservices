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


