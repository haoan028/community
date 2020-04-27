package com.haoan.community.controller;

import com.haoan.community.dto.PageInfoDTO;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.service.QuestionService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    QuestionService questionService;
    @GetMapping("/")
    public String sayHello(@RequestParam(value = "page",defaultValue = "1") Integer page,
                            @RequestParam(value = "size",defaultValue = "2") Integer size,
                            Model model){

//        List<QuestionUserDTO> questionUserDTOS = questionService.findAll();
        PageInfoDTO pageInfoDTO = questionService.findAll(page,size);
        model.addAttribute("pageInfo",pageInfoDTO);
        return "index";
    }
}
