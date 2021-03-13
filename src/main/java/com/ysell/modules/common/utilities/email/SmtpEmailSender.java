package com.ysell.modules.common.utilities.email;

import com.ysell.common.models.YsellResponse;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.utilities.email.models.EmailModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from.email}")
    private String senderEmail;


    @Override
    public void send(EmailModel model, String emailType) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(model.getRecipient());
            msg.setSubject(model.getSubject());
            msg.setText(model.getMessage());
            msg.setFrom(senderEmail);

            if (model.getBccRecipients() != null)
                msg.setBcc(model.getBccRecipients().toArray(new String[0]));

            javaMailSender.send(msg);
        }
        catch (Exception ex) {
            log.error(format("Error occurred while sending %s email: ", emailType), ex);
            if (ex instanceof MailSendException) {
                throw new YSellRuntimeException(Arrays.stream(((MailSendException) ex).getMessageExceptions())
                        .map(e -> YsellResponse.Error.from(e.getMessage()))
                        .collect(Collectors.toList()));
            }
            throw new YSellRuntimeException(format("Error occurred while sending %s email. Please meet with administrator", emailType));
        }
    }
}
