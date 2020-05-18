package com.baeldung.aspect;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AspectLogPojo {
	
	String status;
	String id;

}
