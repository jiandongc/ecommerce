#!/bin/bash
# start all services

java -Xmx128m -jar ../customer/target/customer-1.0-SNAPSHOT.jar & 
java -Xmx128m -jar ../product/target/product-1.0-SNAPSHOT.jar &
java -Xmx128m -jar ../shopping-cart/target/shopping-cart-0.0.1-SNAPSHOT.jar &
java -Xmx64m -jar ../authserver/target/authserver-1.0-SNAPSHOT.jar &
java -Xmx128m -jar ../order/target/order-1.0-SNAPSHOT.jar &
java -Xmx64m -jar ../review/target/review-1.0-SNAPSHOT.jar &

