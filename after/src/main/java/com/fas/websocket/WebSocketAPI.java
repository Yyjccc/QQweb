package com.fas.websocket;

import com.fas.dto.GroupMsgDto;
import com.fas.dto.MessageListDto;
import com.fas.dto.RecordDto;
import com.fas.dto.RecordListDto;
import com.fas.entity.Group;
import com.fas.entity.User;
import com.fas.my_interface.APIEndpoint;
import com.fas.service.GroupService;
import com.fas.service.RecordService;
import com.fas.service.TalkService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class WebSocketAPI {

	@Autowired
	private TalkService talkService;
	@Autowired
	private RecordService recordService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ChatWebNoticeService chatWebNoticeService;

	/**
	 * 获取用户的消息列表
	 * @return
	 */
	@APIEndpoint(method = "get",path = "/user/message")
	public List<MessageListDto> getMessage(String nickname) {
		try {
			return talkService.getMessage(nickname);
		}catch (Exception e){
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	/**
	 * 获取两个人的消息记录，一直到指定页数（包括）
	 */
	@APIEndpoint(method = "get",path = "/user/record/all")
	public List<RecordListDto> getRecordAll (
			 Integer current,
			 String qoNum
	) {
		return recordService.getRecordAll(current, qoNum);
	}

	/**
	 * 获取一个群聊的聊天记录（直到）
	 * @param current
	 * @param groupNum
	 * @return
	 */
	@APIEndpoint(method = "get",path = "/user/group/record/all")
	public List<RecordListDto> getGroupRecordALl (
			 Integer current, Long groupNum
	) {
		return groupService.getGroupRecordAll(current, groupNum);
	}

	/**
	 * 发送消息
	 * @param recordDto
	 */
	@APIEndpoint(method = "post",path = "/user/send")
	public void sendMsg (@Valid RecordDto recordDto) throws IOException {

		recordService.send(recordDto);
		//通知在线用户
		chatWebNoticeService.noticeUser(recordDto.getToUser());
	}

	/**
	 * 在群聊中发送消息
	 * @param groupMsgDto
	 */
	@APIEndpoint(method = "post",path = "/user/group/send")
	public void sendToGroup (@Valid GroupMsgDto groupMsgDto) throws IOException {
		groupService.send(groupMsgDto);
		chatWebNoticeService.noticeGroup(groupMsgDto.getToGroup());
	}

	/**
	 * 获取待同意用户列表
	 */
	@APIEndpoint(method = "get",path = "/user/wait/user")
	public List<User> waitUserList () {
		return groupService.waitUserList();
	}

	/**
	 * 获取待同意群组列表
	 * @return
	 */

	@APIEndpoint(method = "get",path = "/user/wait/group")
	public List<Group> waitGroupList () {
		return groupService.waitGroupList();
	}
}
