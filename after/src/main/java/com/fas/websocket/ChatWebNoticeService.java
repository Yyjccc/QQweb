package com.fas.websocket;


import com.alibaba.fastjson.JSON;
import com.fas.entity.User;
import com.fas.service.GroupService;
import com.fas.util.UserWsUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Component
@Data
public class ChatWebNoticeService {

	@Autowired
	private WebSocketAPIHandler webSocketAPIHandler;

	@Autowired
	private UserWsUtil userWsUtil;

	@Autowired
	private GroupService groupService;

	public void noticeUser(@NotNull String toUser) throws IOException {
		String userQo = userWsUtil.getUserQo();
		//通知发送者
		WebSocketSession sender = getOnline(userQo);
		APIResponse response = new APIResponse("notice", "/user", toUser);
		sender.sendMessage(new TextMessage(JSON.toJSONString(response)));
		//通知接收者
		WebSocketSession session = getOnline(toUser);

		if(session!=null){
			APIResponse apiResponse = new APIResponse("notice", "/user", userQo);
			session.sendMessage(new TextMessage(JSON.toJSONString(apiResponse)));
		}
	}

	private WebSocketSession getOnline(String qoNum){
		HashMap<String, WebSocketSession> sessions = webSocketAPIHandler.getSessions();
		return sessions.get(qoNum);

	}

	public void noticeGroup(Long toGroup) throws IOException {
		String userQo = userWsUtil.getUserQo();
		//通知发送者
		WebSocketSession sender = getOnline(userQo);
		APIResponse response = new APIResponse("notice", "/group", toGroup);
		TextMessage textMessage = new TextMessage(JSON.toJSONString(response));
		sender.sendMessage(textMessage);
		List<User> groupUserList = groupService.getGroupUserList(toGroup);
		for (User user : groupUserList) {
			WebSocketSession session = getOnline(user.getQoNum());
			if(session!=null){
				session.sendMessage(textMessage);
			}
		}
	}
}
