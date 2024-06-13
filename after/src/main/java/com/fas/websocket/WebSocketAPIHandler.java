package com.fas.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fas.service.GroupService;
import com.fas.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Data
public class WebSocketAPIHandler  implements WebSocketHandler {

	private WebSocketSession current;
	//聊天记录存储服务
	@Autowired
	private APIService apiService;
	//在线用户session
	private HashMap<String,WebSocketSession> sessions=new HashMap<>();
	@Autowired
	private GroupService groupService;
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String query = session.getUri().getQuery();
		Map<String, String> queryParams = splitQuery(query);
		String token = queryParams.get("token");
		String qoNum = JwtUtil.parseJWT(token).getSubject().split("#")[0];
		sessions.put(qoNum,session);

	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		String payload = (String) message.getPayload();
		JSONObject request = (JSONObject) JSON.parse(payload);
		String id = request.get("id").toString();
		APIRequest apiRequest = JSON.parseObject(request.get("request").toString(), APIRequest.class);

		try {
			current=session;
			APIResponse apiResponse = apiService.handleRequest(apiRequest);
			apiResponse.setId(id);
			if(apiResponse.getResponse()!=null){
				session.sendMessage(new TextMessage(JSON.toJSONString(apiResponse)));
			}
		}catch (Exception e){
			e.printStackTrace();
			APIResponse apiResponse=new APIResponse();
			apiResponse.setStatus(500);
			apiResponse.setId(id);
		session.sendMessage(new TextMessage(JSON.toJSONString(apiResponse)));
		}


	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		sessions.remove(session.getId());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	private Map<String, String> splitQuery(String query) {
		return Stream.of(query.split("&"))
				.map(param -> param.split("="))
				.collect(Collectors.toMap(pair -> URLDecoder.decode(pair[0]), pair -> URLDecoder.decode(pair[1])));
	}
}
