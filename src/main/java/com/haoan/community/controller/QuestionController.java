package com.haoan.community.controller;

import com.haoan.community.bean.Question;
import com.haoan.community.dto.CommentDTO;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.enums.CommentTypeEnum;
import com.haoan.community.service.CommentService;
import com.haoan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;
    @GetMapping("/question/{id}")
    public String toQuestion(@PathVariable("id") Long id,
                              Model model){

        QuestionUserDTO questionUserDTO = questionService.findById(id);
        questionService.incView(id);
        model.addAttribute("question",questionUserDTO);
        List<CommentDTO> commentDTOS=commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        List<Question> questions = questionService.selectRelated(questionUserDTO.getQuestion());
        model.addAttribute("relates",questions);
        model.addAttribute("comments",commentDTOS);
        return "question";
    }
}
