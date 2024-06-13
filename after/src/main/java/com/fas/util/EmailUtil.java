package com.fas.util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EmailUtil {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private JavaMailSender javaMailSender;

    // 邮箱标题
    @Value("${spring.mail.subject}")
    private String subject;

    // 发件人
    @Value("${spring.mail.username}")
    private String emailForm;

    private String contentBefore = "<html lang='en'>\n" +
            "<body>\n" +
            "<div style='text-align: center;font-weight: bold;'>\n" +
            "    <p style='color: #647E68;font-size: 30px;'>欢迎注册QO在线聊天</p>\n" +
            "    <p style='font-size: 20px;'>与QQ只差“<span style='font-size: 25px;color: #AF0171;'>一点</span>”</p>\n" +
            "    <div style='width: 200px;height: 50px;line-height: 50px;margin: 0 auto;border-radius: 10px;background-color: #EEF1FF;color: #182747;'>\n" +
            "        <span>验证码：</span>\n" +
            "        <span style='color: #562B08;'>";
    private String contentAfter = "</span>\n" +
            "    </div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";

    public void send(String email) {
        // 生成六位大写字母和数字的验证码
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        // 使用自定模板发送邮箱
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(emailForm);
            mimeMessageHelper.setText(contentBefore + code + contentAfter, true);
            javaMailSender.send(mimeMessage);
            log.info("已发送邮箱验证码至：" + email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 缓存email到redis，五分钟过期
        String redisKey = "email:" + email;
        redisTemplate.opsForValue().set(redisKey, code);
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
    }

    @Async
    public void info(String email){
        send(email);
    }
}

