package com.haoan.community.controller;

import com.haoan.community.bean.Question;
import com.haoan.community.bean.User;
import com.haoan.community.cache.TagCache;
import com.haoan.community.dto.GithubUser;
import com.haoan.community.dto.QuestionUserDTO;
import com.haoan.community.mapper.QuestionMapper;
import com.haoan.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    QuestionService questionService;


    @GetMapping("/publish")
    public String toPublish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String saveQuestion(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("tag") String tag,
                               @RequestParam(value = "id",required = false) Long id,
                               HttpServletRequest request,
                               Model model) {
       User user= (User) request.getSession().getAttribute("user");


        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());

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

        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","输入非法标签"+invalid);
            return "publish";
        }
        Question question = new Question();
        question.setCreator(user.getId());
        question.setDescription(description);
        question.setTag(tag);
        question.setTitle(title);
        question.setId(id);
        Long questionId = questionService.saveOrUpdate(question);

        return "redirect:/question/"+questionId;
    }

    @GetMapping("/publish/{id}")
    public String toUpdate(@PathVariable("id") Long id,
                           Model model){
        QuestionUserDTO byId = questionService.findById(id);
        model.addAttribute("title",byId.getQuestion().getTitle());
        model.addAttribute("description",byId.getQuestion().getDescription());
        model.addAttribute("tag",byId.getQuestion().getTag());
        model.addAttribute("id",id);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
