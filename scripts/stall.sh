#!/bin/bash
# start all services

java -Xmx128m -jar ../customer/target/customer-1.0-SNAPSHOT.jar & 
java -Xmx128m -jar ../product/target/product-1.0-SNAPSHOT.jar &
java -Xmx128m -jar ../shopping-cart/target/shopping-cart-1.0-SNAPSHOT.jar &
java -Xmx64m -jar ../authentication/target/authentication-1.0-SNAPSHOT.jar &
java -Xmx128m -jar ../order/target/order-1.0-SNAPSHOT.jar &
java -Xmx64m -jar ../review/target/review-1.0-SNAPSHOT.jar &

