package com.fas.dto;

import com.fas.entity.User;
import com.fas.util.EncodeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotNull
    //@Pattern(regexp = "^[a-zA-Z0-9_]{6,13}$")
    private String qoNum;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$")
    private String password;
    //@NotNull
    @Email
    private String email;
   // @NotNull
    private String emailCode;

    public User getUser () {
        User user = new User();
        user.setQoNum(qoNum);
        user.setPassword(EncodeUtil.encode(password));
        user.setEmail(email);
        user.setNickname(qoNum);
        return user;
    }
}
