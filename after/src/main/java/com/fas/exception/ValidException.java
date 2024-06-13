package com.fas.exception;

import com.fas.my_enum.ResultEnum;

// 自定义枚举类抛错
public class ValidException extends RuntimeException {
    private final ResultEnum resultEnum;

    public ValidException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public ResultEnum getResultEnum() {
        return resultEnum;
    }
}
