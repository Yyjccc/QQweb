package com.fas.dto;

import com.fas.entity.User;
import com.fas.util.EncodeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull
    //@Pattern(regexp = "^[a-zA-Z0-9_]{9,15}$")
    private String qoNum;
    @NotNull
    //@Pattern(regexp = "^[a-zA-Z0-9]{6,18}$")
    private String password;
   // @NotNull
    private String code;
    //@NotNull
    private String codeKey;

    public User getUser() {
        User user = new User();
        user.setQoNum(qoNum);
        user.setPassword(EncodeUtil.encode(password));
        return user;
    }
}
