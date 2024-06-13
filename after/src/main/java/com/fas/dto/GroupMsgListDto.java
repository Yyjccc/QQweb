package com.fas.dto;

import com.fas.entity.Group;
import com.fas.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMsgListDto extends Group {
    private Record record;
}
