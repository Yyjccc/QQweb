package com.fas.dto;

import com.fas.entity.User;
import com.fas.util.EncodeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSelfDto {
    @NotNull
    private String avatar;
    @Pattern(regexp = ".{0}|(^[a-zA-Z0-9]{6,18}$)")
    private String password;
    @NotNull
    @Pattern(regexp = "^.{1,10}$")
    private String nickname;
    private String phone;
    @NotNull
    private String sex;
    private String description;

    public User getUser() {
        User user = new User();
        if (StringUtils.hasText(password)) user.setPassword(EncodeUtil.encode(password));
        user.setAvatar(avatar);
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setSex(sex);
        user.setDescription(description);
        return user;
    }
}
