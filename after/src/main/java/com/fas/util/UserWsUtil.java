package com.fas.util;

import com.fas.entity.User;
import com.fas.websocket.WebSocketAPIHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class UserWsUtil {

	@Autowired
	private WebSocketAPIHandler webSocketAPIHandler;



	// 获取当前 WebSocket 会话的用户 Qo
	public  String getUserQo() {
		try {
			WebSocketSession session = webSocketAPIHandler.getCurrent();
			User user = (User) session.getAttributes().get("user");
			return user.getQoNum();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 生成随机六位字符
	public static String getCode() {
		String s1 = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 3);
		String s2 = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8))).substring(0, 3);
		return s1 + s2;
	}
}
