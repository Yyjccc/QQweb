package com.fas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGroupDto {
    private String groupRemark;
    @NotNull
    private String groupAvatar;
    @NotNull
    @Size(min = 1, max = 8)
    private String groupName;
}
