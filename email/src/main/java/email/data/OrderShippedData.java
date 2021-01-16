package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Getter
public class OrderShippedData extends MailData {

    private final String orderNumber;
    private final String orderDeliveryMethod;
    private final AddressData shippingAddress;
    private final List<OrderItemData> orderItems;

    @Builder
    public OrderShippedData(List<String> sendTo,
                            List<String> bccTo,
                            String orderNumber,
                            String orderDeliveryMethod,
                            AddressData shippingAddress,
                            List<OrderItemData> orderItems) {
        super("info@noodle-monster.co.uk", "Noodle Monster Order", sendTo, bccTo, format("Your order has shipped #%s", orderNumber));
        this.orderNumber = orderNumber;
        this.orderDeliveryMethod = orderDeliveryMethod;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
    }

    @Override
    public String generateText() throws IOException {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("order-shipped");

        st.add("orderNumber", this.orderNumber);
        st.add("orderDeliveryMethod", this.orderDeliveryMethod);
        st.add("shippingAddress", this.shippingAddress);
        st.add("orderItems", this.orderItems);
        return st.render();
    }

    @Getter
    @Builder
    public static class AddressData {
        private String name;
        private String title;
        private String mobile;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private String city;
        private String country;
        private String postcode;
    }
    @Getter
    @Builder
    public static class OrderItemData {
        private String sku;
        private String code;
        private String name;
        private String description;
        private String price;
        private String quantity;
        private String subTotal;
        private String imageUrl;
    }

}
