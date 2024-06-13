package com.fas.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fas.my_interface.APIEndpoint;
import com.fas.websocket.APIRequest;
import com.fas.websocket.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class APIService {

	@Autowired
	private ApplicationContext applicationContext;

	public APIResponse handleRequest(APIRequest apiRequest) throws Exception {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Component.class);
		for (Object bean : beans.values()) {
			Method[] methods = bean.getClass().getMethods();
			for (Method method : methods) {
				APIEndpoint endpoint = method.getAnnotation(APIEndpoint.class);
				if (endpoint != null && endpoint.path().equals(apiRequest.getPath())
						&& endpoint.method().equalsIgnoreCase(apiRequest.getMethod())) {
					Object[] params = extractParams(method, apiRequest);

					return new APIResponse(method.invoke(bean, params),apiRequest.getPath());
				}
			}
		}
		throw new Exception("No matching endpoint found");
	}

	private Object[] extractParams(Method method, APIRequest apiRequest) {
		if(apiRequest.getMethod().equals("get")){
		// 根据实际情况解析参数
			return ToObjectArray((JSONObject) apiRequest.getParam()); // 仅示例，实际情况需要更复杂的处理
		}else {
			Class<?>[] parameterTypes = method.getParameterTypes();
			return parseBody((JSONObject) apiRequest.getBody(),parameterTypes[0]);
		}

	}

	private Object[] parseBody(JSONObject body,Class clazz) {
		List<Object> objectList = new ArrayList<>();
		objectList.add(JSON.toJavaObject(body, clazz));
		return objectList.toArray();
	}


	public static Object[] ToObjectArray(JSONObject jsonObject) {
		List<Object> objectList = new ArrayList<>();
		if(jsonObject==null){
			return null;
		}
		// 遍历 JSONObject 的键值对
		for (String key : jsonObject.keySet()) {
			Object value = jsonObject.get(key);
			objectList.add(value);
		}

		// 将 List 转换为数组
		return objectList.toArray();
	}
}
