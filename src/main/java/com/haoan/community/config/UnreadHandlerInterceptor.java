package com.haoan.community.config;

import com.haoan.community.bean.User;
import com.haoan.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UnreadHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private  NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user=(User)request.getSession().getAttribute("user");
        if (user==null){
            return true;
        }else {
            //已经登录放行

            Long unreadCount = notificationService.unreadCount(user.getId());
            request.getSession().setAttribute("unreadCount",unreadCount);
            return true;
        }
    }
}
