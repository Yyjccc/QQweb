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
public class ForgetDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String emailCode;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$")
    private String password;

    public User getUser () {
        User user = new User();
        user.setEmail(email);
        user.setPassword(EncodeUtil.encode(password));
        return user;
    }
}
