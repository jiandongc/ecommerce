package email.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class MailData {
    private final String from;
    private final String fromAlias;
    private final List<String> sendTo;
    private final String subject;
    public abstract String generateText() throws IOException;
}
