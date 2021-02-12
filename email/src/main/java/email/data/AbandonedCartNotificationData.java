package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.IOException;
import java.util.List;

@Getter
public class AbandonedCartNotificationData extends MailData {

    private String cartUid;
    private List<CartItemData> cartItems;

    @Builder
    public AbandonedCartNotificationData(List<String> sendTo,
                                         List<String> bccTo,
                                         String cartUid,
                                         List<CartItemData> cartItems) {
        super("info@noodle-monster.co.uk", "Noodle Monster", sendTo, bccTo, "Did you forget something?");
        this.cartUid = cartUid;
        this.cartItems = cartItems;
    }

    @Override
    public String generateText() throws IOException {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("abandoned-cart-notification");
        st.add("cartUid", this.cartUid);
        st.add("cartItems", this.cartItems);
        return st.render();
    }

    @Getter
    @Builder
    public static class CartItemData {
        private String name;
        private String description;
        private String quantity;
        private String imageUrl;
    }
}
