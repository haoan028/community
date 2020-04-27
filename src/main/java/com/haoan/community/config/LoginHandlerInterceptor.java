package com.haoan.community.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user=request.getSession().getAttribute("user");
        if (user==null){
            //未登录，返回登录页面并提醒登录
            request.setAttribute("msg","权限不足请登录");
//            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else {
            //已经登录放行
            return true;
        }
    }
}
