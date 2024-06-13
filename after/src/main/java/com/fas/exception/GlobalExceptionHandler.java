package com.fas.exception;


import com.fas.dto.RespResult;
import com.fas.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public void handler(ValidException exception, HttpServletResponse response) {
        log.info("发现验证异常：{}", exception.getResultEnum().getMsg());
        WebUtil.send(RespResult.fail(exception.getResultEnum()), response);
    }
}
