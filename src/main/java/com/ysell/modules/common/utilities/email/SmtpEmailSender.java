package com.ysell.modules.common.utilities.email;

import com.ysell.modules.common.utilities.email.models.EmailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {
    private final JavaMailSender javaMailSender;

	@Override
	public void Send(EmailModel model) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(model.getRecipient());
        msg.setSubject(model.getSubject());
        msg.setText(model.getMessage());
        
        if(model.getBccRecipients() != null)
        	msg.setBcc(model.getBccRecipients().toArray(new String[0]));

        javaMailSender.send(msg);
	}

}
