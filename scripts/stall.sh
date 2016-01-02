#!/bin/bash
# start all services

java -Xmx256m -jar ../customer/target/customer-1.0-SNAPSHOT.jar & 
java -Xmx256m -jar ../product/target/product-1.0-SNAPSHOT.jar &
java -Xmx256m -jar ../order/target/order-1.0-SNAPSHOT.jar &
java -Xmx256m -jar ../authserver/target/authserver-1.0-SNAPSHOT.jar &
