#!/bin/bash
# start all services

java -jar ../customer/target/customer-1.0-SNAPSHOT.jar & 
java -jar ../product/target/product-1.0-SNAPSHOT.jar &
java -jar ../order/target/order-1.0-SNAPSHOT.jar &
java -jar ../authserver/target/authserver-1.0-SNAPSHOT.jar &
