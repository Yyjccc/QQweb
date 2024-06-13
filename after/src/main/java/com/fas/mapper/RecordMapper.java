package com.fas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fas.dto.RecordListDto;
import com.fas.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper extends BaseMapper<Record> {
    Record getEndRecord(@Param("qoNum1") String qoNum1, @Param("qoNum2") String qoNum2);

    List<RecordListDto> getRecord(
            @Param("offset") Integer offset,
            @Param("qoNum1") String qoNum1,
            @Param("qoNum2") String qoNum2
    );

    List<RecordListDto> getRecordAll(
            @Param("limit") Integer limit,
            @Param("qoNum1") String qoNum1,
            @Param("qoNum2") String qoNum2
    );
}
