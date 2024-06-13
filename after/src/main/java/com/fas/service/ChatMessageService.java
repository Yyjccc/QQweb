package com.fas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fas.entity.ChatMessage;

public interface ChatMessageService extends IService<ChatMessage> {
	boolean save(ChatMessage chatMessage);
}
