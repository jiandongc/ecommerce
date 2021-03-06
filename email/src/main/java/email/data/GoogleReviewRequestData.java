package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.IOException;
import java.util.List;

@Getter
public class GoogleReviewRequestData extends MailData {

    private final String customerName;
    private final String voucherCode;

    @Builder
    public GoogleReviewRequestData(List<String> sendTo,
                                   List<String> bccTo,
                                   String customerName,
                                   String voucherCode) {
        super("info@noodle-monster.co.uk", "Noodle Monster Review", sendTo, bccTo, "How is your recent experience with Noodle Monster?");
        this.customerName = customerName;
        this.voucherCode = voucherCode;
    }

    @Override
    public String generateText() throws IOException {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("google-review-request");
        st.add("customerName", this.customerName);
        st.add("voucherCode", this.voucherCode);
        return st.render();
    }
}
