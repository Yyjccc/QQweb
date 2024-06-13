package com.fas.dto;

import com.fas.my_enum.ResultEnum;
import lombok.Data;

@Data
public class RespResult<T> {
    private Integer code;
    private String msg;
    private T data;

    /**
     * 成功返回
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RespResult<T> success(T data) {
        RespResult<T> respResult = new RespResult<>();
        respResult.setCode(ResultEnum.SUCCESS.getCode());
        respResult.setMsg(ResultEnum.SUCCESS.getMsg());
        respResult.setData(data);
        return respResult;
    }

    /**
     * 自定义失败返回
     * @param <T>
     * @return
     */
    public static <T> RespResult<T> fail(ResultEnum resultEnum) {
        RespResult<T> respResult = new RespResult<>();
        respResult.setCode(resultEnum.getCode());
        respResult.setMsg(resultEnum.getMsg());
        return respResult;
    }
}
