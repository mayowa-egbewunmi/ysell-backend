package com.ysell.modules.user.models.dto.output;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResetCodeDto {

    protected long id;
	
    protected long userId;
	
	private String resetCode;
	
	private Date expiryTimestamp;
	
	private boolean active;
}
