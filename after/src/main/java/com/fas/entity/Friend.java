package com.fas.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private String fromUser;
    private String toUser;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private String isAgree;
}
