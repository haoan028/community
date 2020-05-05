package com.haoan.community.controller;

import com.haoan.community.bean.User;
import com.haoan.community.service.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable("id") Long id, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        Long read = notificationService.read(id, user);
        return "redirect:/question/"+read;
    }
}
