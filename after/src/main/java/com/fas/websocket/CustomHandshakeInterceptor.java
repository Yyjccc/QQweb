package com.fas.websocket;

import com.fas.config.Constance;
import com.fas.entity.ChatMessage;
import com.fas.entity.User;
import com.fas.mapper.ChatMessageMapper;
import com.fas.service.ChatMessageService;
import com.fas.util.JwtUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.net.URLDecoder;


@Component
@Data
@NoArgsConstructor
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

	@Resource
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private ChatMessageService chatMessageService;

	private String userId;


	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		URI uri = request.getURI();
		String query = uri.getQuery();
		Map<String, String> queryParams = splitQuery(query);
		String token = queryParams.get("token");
		if (token != null && !token.isEmpty()) {
			String userId = JwtUtil.parseJWT(token).getSubject().split("#")[0];
			if (userId != null) {
				String key = "user:" + userId;
				User user = (User) redisTemplate.opsForValue().get(key);
				attributes.put("user", user);
				return true;
			}
		}
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

	}

	private Map<String, String> splitQuery(String query) {
		return Stream.of(query.split("&"))
				.map(param -> param.split("="))
				.collect(Collectors.toMap(pair -> URLDecoder.decode(pair[0]), pair -> URLDecoder.decode(pair[1])));
	}



}

