package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.IOException;
import java.util.List;

@Getter
public class WelcomeEmailData extends MailData {

    private final String voucherAmount;
    private final String voucherCode;

    @Builder
    public WelcomeEmailData(List<String> sendTo, String voucherAmount, String voucherCode) {
        super("info@noodle-monster.co.uk", "Noodle Monster Account", sendTo, "Welcome to Noodle Monster!");
        this.voucherAmount = voucherAmount;
        this.voucherCode = voucherCode;
    }

    @Override
    public String generateText() throws IOException {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("welcome-email");

        st.add("voucherAmount", this.voucherAmount);
        st.add("voucherCode", this.voucherCode);
        return st.render();
    }
}
