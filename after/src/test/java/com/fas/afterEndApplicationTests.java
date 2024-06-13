package com.fas;

import com.fas.service.TalkService;
import com.fas.util.EncodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
class afterEndApplicationTests {

    @Resource
    private TalkService talkService;

    @Test
    void contextLoads() {
//        for (int i = 0; i < 10; i++) {
//            System.out.println("qo" + Math.abs(UUID.randomUUID().hashCode()));
//        }

//        System.out.println(EncodeUtil.encode("qoxin0313"));
       // System.out.println(talkService.getMessage(""));
    }
}
