package com.haoan.community.controller;

import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;


    @GetMapping("/question/{id}")
    public String toQuestion(@PathVariable("id") Integer id,
                              Model model){

        QuestionUserDTO questionUserDTO = questionService.findById(id);
        model.addAttribute("question",questionUserDTO);
        return "question";
    }
}
