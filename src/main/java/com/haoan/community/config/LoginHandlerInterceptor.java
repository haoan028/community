package com.haoan.community.config;

import com.haoan.community.bean.User;
import com.haoan.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user=(User)request.getSession().getAttribute("user");
        if (user==null){
            //未登录，返回登录页面并提醒登录
            request.setAttribute("msg","权限不足请登录");
            return false;
        }else {
            //已经登录放行
            return true;
        }
    }



}
