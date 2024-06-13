package com.fas.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class APIResponse {
	private String id;
	private Object response;
	private String path;
	private int status;
	private String type;


	public APIResponse(Object data,String path){
		type="api";
		this.response=data;
		this.path=path;
		this.status=200;
	}

	public APIResponse(String type, String path, Object data) {
		this.type=type;
		this.path=path;
		this.response=data;
		this.status=200;
	}
}
