package com.ysell.common.advices;

import com.ysell.common.annotations.IgnoreWrapResponse;
import com.ysell.common.models.YsellResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//@RestControllerAdvice
public class WrapResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterClass) {
        return methodParameter.getMethod() == null ||
                methodParameter.getMethod().getDeclaredAnnotation(IgnoreWrapResponse.class) == null ||
                methodParameter.getDeclaringClass().getDeclaredAnnotation(IgnoreWrapResponse.class) == null;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return object instanceof YsellResponse ? object : YsellResponse.createSuccess(object);
    }
}