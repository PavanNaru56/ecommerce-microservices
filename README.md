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

