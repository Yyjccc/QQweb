package com.fas.handler;

import com.fas.entity.User;
import com.fas.exception.ValidException;
import com.fas.my_enum.ResultEnum;
import com.fas.my_interface.HaveAuth;
import com.fas.my_interface.HavePack;
import com.fas.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
@Component
public class PreInterceptor implements HandlerInterceptor {

    private final RedisTemplate<Object, Object> redisTemplate;

    public PreInterceptor(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 对虚拟请求放行
        if ("OPTIONS".equals(request.getMethod())) return true;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Class<?> beanType = handlerMethod.getBeanType();
            // 是否需要封装
            if (method.isAnnotationPresent(HavePack.class) || beanType.isAnnotationPresent(HavePack.class)) {
                request.setAttribute("HavePack", HavePack.class);
            }
            // 是否需要token
            if (method.isAnnotationPresent(HaveAuth.class) || beanType.isAnnotationPresent(HaveAuth.class)) {
                // 从redis中获取登录信息
                String token = request.getHeader("token");
                // 使用jwt解析token
                String qoNum;
                try {
                    qoNum = JwtUtil.parseJWT(token).getSubject();
                } catch (Exception e) {
                    throw new ValidException(ResultEnum.BAD_REQUEST);
                }
                String num = qoNum.split("#")[1];
                String qo = qoNum.split("#")[0];
                // 比对redis序号
                Integer redisNum = (Integer) redisTemplate.opsForValue().get("num:" + qo);
                if (
                    !Optional.ofNullable(redisNum).isPresent() ||
                    !num.equals(redisNum.toString())
                ) throw new ValidException(ResultEnum.TOKEN_TIMEOUT);
                // 获取redis中的用户信息
                User user = (User) redisTemplate.opsForValue().get("user:" + qo);
                if (!Optional.ofNullable(user).isPresent()) throw new ValidException(ResultEnum.TOKEN_TIMEOUT);
                request.setAttribute("user", user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("{}已通过拦截器！", request.getRequestURI());
    }
}
