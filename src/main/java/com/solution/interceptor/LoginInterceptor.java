package com.solution.interceptor;

import com.solution.model.User;
import com.solution.threadlocal.UserThreadLocalHold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader("userId");
        Integer userId = null;
        if (userIdStr != null && userIdStr.length() != 0) {
            userId = Integer.valueOf(userIdStr);
        }

        String accountName = request.getHeader("accountName");

        String role = request.getHeader("role");

        if (userId == null || accountName == null || role == null) {
            logger.error("登录失败，没有用户信息");
            return false;
        }

        User user = new User();
        user.setUserId(userId);
        user.setAccountName(accountName);
        user.setRole(role);

        UserThreadLocalHold.threadLocal.set(user);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserThreadLocalHold.threadLocal.remove();
    }
}