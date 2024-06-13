package com.fas.my_enum;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(200, "操作成功！"),
    FAIL(500, "未知异常！"),
    BAD_REQUEST(1001, "无效的认证，非法请求！"),
    TOKEN_TIMEOUT(1002, "身份过期，请重新登录！"),
    DATA_INVALID(1003, "无效的数据！"),
    HAS_USER(1004, "账号名已存在！"),
    USER_ERROR(1006, "用户不存在！"),
    MYSQL_ERROR(1007, "数据库异常！"),
    CODE_FAIL(1008, "验证码错误！"),
    CODE_TIMEOUT(1009, "验证码已过期，请重新获取！"),
    EMAIL_CODE_FAIL(1010, "邮箱验证码错误！"),
    EMAIL_CODE_NOT(1011, "请输入正确的邮箱！"),
    EMAIL_EFFECTIVE(1012, "邮箱验证码还处于有效期中！"),
    EMAIL_HAS_USER(1013, "此邮箱已绑定账号！"),
    EMAIL_NOT_USER(1014, "此邮箱未绑定账号！"),
    EMAIL_NOT_SEND(1015, "请先获取邮箱验证码！"),
    FORGET_PWD_EQ(1016, "新密码不能与旧密码相同！"),
    GROUP_USER_FAIL(1017, "对方已拒绝，不可再次申请！"),
    USER_FAIL(1005, "用户名或密码错误！");


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
