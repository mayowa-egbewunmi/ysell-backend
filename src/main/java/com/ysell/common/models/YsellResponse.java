package com.ysell.common.models;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ysell.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class YsellResponse<M, T> {
	
    private final M meta;
    
    private final T body;

    public static <T> YsellResponse<Meta, T> createSuccess(T model) {
    	return new YsellResponse(Meta.createSuccess(), model);
    }
        
    public static <T> YsellResponse<Meta, T> createError(String... errorMsgs) {
    	return new YsellResponse(Meta.createError(errorMsgs), null);
    }
    
    public static <T> YsellResponse<Meta, T> createError(List<String> errorMsgs) {
    	return new YsellResponse(Meta.createError(errorMsgs), null);
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private static class Meta {

        @JsonIgnore
        private Status status;
        private List<String> errorMessages;

        private Meta(Status status, List<String> errorMessages) {
            this.status = status;
            this.errorMessages = errorMessages;
        }

        private static Meta createSuccess() {
            return new Meta(Status.SUCCESS, null);
        }

        private static Meta createError(String... errorMsgs) {
            return new Meta(Status.ERROR, Arrays.asList(errorMsgs));
        }

        private static Meta createError(List<String> errorMsgs) {
            return new Meta(Status.ERROR, errorMsgs);
        }

        public boolean isSuccessful() {
            return status == Status.SUCCESS;
        }
    }
}