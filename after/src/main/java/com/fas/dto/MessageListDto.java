package com.fas.dto;

import com.fas.entity.Record;
import com.fas.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageListDto {
    private User user;
    private Record record;
}
