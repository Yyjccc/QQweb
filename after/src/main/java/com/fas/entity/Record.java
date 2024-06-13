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
public class Record {
    private String recordId;
    private String recordUser;
    private String recordContent;
    private String recordType;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
