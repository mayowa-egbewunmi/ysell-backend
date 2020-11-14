package com.ysell.common;

import com.ysell.modules.common.utilities.email.EmailSender;
import com.ysell.modules.common.utilities.email.models.EmailModel;

public class MockEmailSender implements EmailSender {

	public boolean messageSent = false;
	
	@Override
	public void Send(EmailModel model) {
		messageSent = true;
	}
	
}
