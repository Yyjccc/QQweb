package com.fas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {
    @NotNull
    private String toUser;
    @NotNull
    private String recordContent;
    @NotNull
    private String recordType;
}
