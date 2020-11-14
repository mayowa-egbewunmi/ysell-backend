package com.ysell.modules.user.models.dto.input;

import java.util.Date;

import com.ysell.modules.common.models.LookupDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateResetCodeDto {
	
	private String resetCode;
	
	private Date expiryTimestamp;
	
    protected LookupDto user;
}
