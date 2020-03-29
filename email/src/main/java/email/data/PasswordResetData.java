package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.util.List;


@Getter
public class PasswordResetData extends MailData {

    private final String link;
    private final String name;

    @Builder
    public PasswordResetData(List<String> sendTo, String link, String name) {
        super("chenjiandong666@hotmail.com", "Cawaii Account", sendTo, "Reset your password");
        this.link = link;
        this.name = name;
    }

    @Override
    public String generateText() {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("password-reset");

        st.add("name", this.name);
        st.add("link", this.link);
        return st.render();
    }

}
