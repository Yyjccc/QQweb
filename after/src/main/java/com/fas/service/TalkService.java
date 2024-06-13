package com.fas.service;

import com.fas.dto.MessageListDto;

import java.util.List;

public interface TalkService {
    List<MessageListDto> getMessage(String nickname);
}
