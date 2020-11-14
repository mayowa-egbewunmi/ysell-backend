package com.ysell.modules.common.utilities.email.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmailModel {

	private final String recipient;
	
	private final String subject;
	
	private final String message;
	
	private final List<String> bccRecipients;
}
