package com.fas.util;

import com.fas.exception.ValidException;
import com.fas.my_enum.ResultEnum;
import org.springframework.validation.BindingResult;

public class ValidUtil {
    public static void handler (BindingResult bindingResult){
        if (bindingResult.getErrorCount() != 0) throw new ValidException(ResultEnum.DATA_INVALID);
    }
}