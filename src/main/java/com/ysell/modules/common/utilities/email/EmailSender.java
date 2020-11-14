package com.ysell.modules.common.utilities.email;

import com.ysell.modules.common.utilities.email.models.EmailModel;

public interface EmailSender {

	public void Send(EmailModel model);
}
