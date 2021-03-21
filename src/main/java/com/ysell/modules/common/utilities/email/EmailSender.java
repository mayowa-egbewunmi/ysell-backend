package com.ysell.modules.common.utilities.email;

import com.ysell.common.models.YsellResponse;
import com.ysell.modules.common.utilities.email.models.EmailModel;

import java.util.concurrent.CompletableFuture;

public interface EmailSender {

	CompletableFuture<YsellResponse<String>> send(EmailModel model, String emailType);
}
