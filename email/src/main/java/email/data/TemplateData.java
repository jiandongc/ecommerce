package email.data;

import lombok.Builder;
import lombok.Getter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.io.IOException;
import java.util.List;

@Getter
public class TemplateData extends MailData {

    @Builder
    public TemplateData(List<String> sendTo,
                        List<String> bccTo) {
        super("info@noodle-monster.co.uk", "Noodle Monster", sendTo, bccTo,"Noodle Monster【快报】\uD83D\uDC49 来自新加坡，馬來西亞的味道 | 日本的健康有機面 | 網紅有你一面！");
    }

    @Override
    public String generateText() throws IOException {
        STGroup group = new STGroupDir("template", '$', '$');
        ST st = group.getInstanceOf("new-product-20210615");
        return st.render();
    }
}
