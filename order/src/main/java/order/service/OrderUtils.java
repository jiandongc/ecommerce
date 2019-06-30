package order.service;

import java.util.Calendar;
import java.util.Date;

public class OrderUtils {

    public static String generateOrderNumber() {
        Date today = Calendar.getInstance().getTime();
        String orderNumber = Long.toString(today.getTime());
        int randomDigit = (int) Math.floor(Math.random() * 10);
        orderNumber = orderNumber.substring(0, randomDigit) + randomDigit + orderNumber.substring(randomDigit, orderNumber.length());
        orderNumber = String.join("-", orderNumber.substring(0, 4), orderNumber.substring(4, 10), orderNumber.substring(10, 14));
        return orderNumber;
    }
}
