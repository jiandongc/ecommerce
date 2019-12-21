package email.service;

import email.data.MailData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static javax.mail.Message.RecipientType.TO;

@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMessage(MailData mailData) {
        try {
            final MimeMessage mimeMessage = emailSender.createMimeMessage();
            mimeMessage.setSubject(mailData.getSubject());
            mimeMessage.setText(mailData.generateText(), "utf-8", "html");
            for(int i = 0; i < mailData.getSendTo().size(); i++){
                mimeMessage.addRecipients(TO, mailData.getSendTo().get(i));
            }
            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
