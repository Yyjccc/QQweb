package com.fas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fas.entity.ChatMessage;
import com.fas.mapper.ChatMessageMapper;
import com.fas.service.ChatMessageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
	@Transactional
	public boolean save(ChatMessage chatMessage){
		return super.save(chatMessage);
	}
}
