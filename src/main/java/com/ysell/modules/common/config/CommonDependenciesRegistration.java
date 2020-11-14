package com.ysell.modules.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ysell.modules.common.utilities.email.SmtpEmailSender;
import com.ysell.modules.common.utilities.email.EmailSender;

@Configuration
public class CommonDependenciesRegistration {

    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
