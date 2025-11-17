package org.igot.common;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private String id;
	private String ver;
	private String ts;
	private ApiRespParam params;
	private HttpStatus responseCode;

	private transient Map<String, Object> response = new HashMap<>();

	public ApiResponse() {
		this.ver = "v1";
		this.ts = Instant.now().toString();
		this.params = new ApiRespParam(UUID.randomUUID().toString());
	}
	
	public ApiResponse(String id) {
		this();
		this.id = id;
	}
}
