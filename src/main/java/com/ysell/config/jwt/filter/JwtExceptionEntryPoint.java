package com.ysell.config.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysell.common.models.YsellErrorCode;
import com.ysell.common.models.YsellResponse;
import com.ysell.common.models.YsellResponse.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class JwtExceptionEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	private final ObjectMapper objectMapper;


	@Override
	public void commence(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
	) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		Error error = new Error(authException.getMessage(), YsellErrorCode.UNAUTHORIZED_ERROR);
		YsellResponse<Object> errorResponse = YsellResponse.createError(error);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}