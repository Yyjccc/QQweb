package com.fas.service;

import com.fas.dto.RecordDto;
import com.fas.dto.RecordListDto;
import com.fas.entity.User;

import java.util.List;

public interface RecordService {
    List<RecordListDto> getRecord(Integer current, String qoNum);

    List<RecordListDto> getRecordAll(Integer current, String qoNum);

    void send(RecordDto recordDto);
}
