package org.igot.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRespParam {
    private String resmsgid;
	private String msgid;
	private String err;
	private String status;
	private String errmsg;

    public ApiRespParam() {
	}

	public ApiRespParam(String id) {
		resmsgid = id;
		msgid = id;
	}
}
