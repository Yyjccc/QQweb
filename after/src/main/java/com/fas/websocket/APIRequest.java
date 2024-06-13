package com.fas.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class APIRequest {
	private Object param;
	private Object body;
	private String path;
	private String method;
}
