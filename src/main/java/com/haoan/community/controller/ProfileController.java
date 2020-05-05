package com.haoan.community.controller;

import com.haoan.community.bean.User;
import com.haoan.community.dto.GithubUser;
import com.haoan.community.dto.PageInfoDTO;
import com.haoan.community.service.NotificationService;
import com.haoan.community.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    ProfileService profileService;
    @Autowired
    NotificationService notificationService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "size",defaultValue = "6") Integer size,
                          Model model, 
                          HttpServletRequest request
                          ){

        User user = (User) request.getSession().getAttribute("user");
        if(action.equals("questions")){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");

            PageInfoDTO pageInfoDTO = profileService.findAll(user.getId(), page, size);
            model.addAttribute("pageInfo",pageInfoDTO);
        }else if (action.equals("replies")){

            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            PageInfoDTO pageInfoDTO =notificationService.list(user.getId(),page,size);
            Long unreadCount=notificationService.unreadCount(user.getId());
            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("pageInfo",pageInfoDTO);
        }


        return "profile";
    }
}
