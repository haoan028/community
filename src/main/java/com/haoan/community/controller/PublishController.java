package com.haoan.community.controller;

import com.haoan.community.bean.Question;
import com.haoan.community.bean.User;
import com.haoan.community.dto.GithubUser;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.mapper.QuestionMapper;
import com.haoan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    QuestionService questionService;


    @GetMapping("/publish")
    public String toPublish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String saveQuestion(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("tag") String tag,
                               @RequestParam(value = "id",required = false) Integer id,
                               HttpServletRequest request,
                               Model model) {
       User user= (User) request.getSession().getAttribute("user");


        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if(title==null || title ==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null|| description==""){
            model.addAttribute("error","补充内容不能为空");
            return "publish";
        }
        if(tag==null || tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }


        Question question = new Question();
        question.setCreator(user.getAccount_id());
        question.setDescription(description);
        question.setTag(tag);
        question.setTitle(title);
        question.setId(id);
        Integer questionId = questionService.saveOoUpdate(question);

        return "redirect:/question/"+questionId;
    }

    @GetMapping("/publish/{id}")
    public String toUpdate(@PathVariable("id") Integer id,
                           Model model){
        QuestionUserDTO byId = questionService.findById(id);
        model.addAttribute("title",byId.getQuestion().getTitle());
        model.addAttribute("description",byId.getQuestion().getDescription());
        model.addAttribute("tag",byId.getQuestion().getTag());
        model.addAttribute("id",id);
        return "publish";
    }
}
