package com.haoan.community.controller;

import com.haoan.community.bean.Comment;
import com.haoan.community.bean.User;
import com.haoan.community.dto.CommentCreatDTO;
import com.haoan.community.dto.CommentDTO;
import com.haoan.community.dto.ResultDTO;
import com.haoan.community.enums.CommentTypeEnum;
import com.haoan.community.exception.CustomizeErrorCode;
import com.haoan.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;


    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreatDTO commentCreatDTO
                       , HttpServletRequest request
                       ){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
             return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreatDTO==null|| StringUtils.isBlank(commentCreatDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setCommentator(((User) request.getSession().getAttribute("user")).getId());
        comment.setContent(commentCreatDTO.getContent());
        comment.setParentId(commentCreatDTO.getParentId());
        comment.setType(commentCreatDTO.getType());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        commentService.insert(comment,user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO <List<CommentDTO>>comments(@PathVariable("id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
