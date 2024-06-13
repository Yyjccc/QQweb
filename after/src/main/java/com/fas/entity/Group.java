package com.fas.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @TableId(type = IdType.INPUT)
    private Long groupNum;
    private String groupUser;
    private String groupRemark;
    private String groupAvatar;
    private String groupName;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
