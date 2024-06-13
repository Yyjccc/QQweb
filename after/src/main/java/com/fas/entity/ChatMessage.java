package com.fas.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("messages")
public class ChatMessage {
	private String senderNickName;
	private String senderId;
	@TableId
	private Long id;
	private String toId;
	private String toNickName;
	private String type;
	private int isGroup;
	private String content;
	private Long createTime;
	private int send;
}
