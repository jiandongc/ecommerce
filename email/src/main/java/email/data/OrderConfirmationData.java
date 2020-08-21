package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

@Getter
public class OrderConfirmationData extends MailData {

    private final String customerName;
    private final String orderNumber;
    private final String orderEta;
    private final String orderDeliveryMethod;
    private final AddressData shippingAddress;
    private final List<OrderItemData> orderItems;
    private final Boolean guest;
    private final String siteName;
    private final String homePage;
    private final String registrationPage;

    @Builder
    public OrderConfirmationData(List<String> sendTo,
                                 String customerName,
                                 String orderNumber,
                                 String orderEta,
                                 String orderDeliveryMethod,
                                 AddressData shippingAddress,
                                 List<OrderItemData> orderItems,
                                 Boolean guest,
                                 String siteName,
                                 String homePage,
                                 String registrationPage) {
        super("jiandong.c@gmail.com", String.format("%s Order", siteName), sendTo, format("Your %s order confirmation #", siteName) + orderNumber);
        this.customerName = customerName;
        this.orderNumber = orderNumber;
        this.orderEta = orderEta;
        this.orderDeliveryMethod = orderDeliveryMethod;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
        this.guest = guest;
        this.siteName = siteName;
        this.homePage = homePage;
        this.registrationPage = registrationPage;
    }

    @Override
    public String generateText() throws IOException {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("order-confirmation");

        st.add("customerName", this.customerName);
        st.add("orderNumber", this.orderNumber);
        st.add("orderEta", this.orderEta);
        st.add("orderDeliveryMethod", this.orderDeliveryMethod);
        st.add("shippingAddress", this.shippingAddress);
        st.add("orderItems", this.orderItems);
        st.add("guest", this.guest);
        st.add("siteName", this.siteName);
        st.add("homePage", this.homePage);
        st.add("registrationPage", this.registrationPage);
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
