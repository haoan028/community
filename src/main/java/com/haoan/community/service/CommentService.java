package com.haoan.community.service;

import com.haoan.community.bean.*;
import com.haoan.community.dto.CommentDTO;
import com.haoan.community.enums.CommentTypeEnum;
import com.haoan.community.enums.NotificationTypeEnum;
import com.haoan.community.exception.CustomizeErrorCode;
import com.haoan.community.exception.CustomizeException;
import com.haoan.community.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private  CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExMapper questionExMapperr;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExMapper commentExMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User user) {
        //判断我们传过来的值是否正确
        if(comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TAEGET_PARAM_NOT_FOUND);
        }
        //判断我们的回复是类型是否正确
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //判断我们回复的类型 进行操作
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论  首先判断评论是否存在
            Comment onecomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(onecomment==null){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加一级评论的回复数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExMapper.incComment(parentComment);
            //增加通知
            Question question = questionMapper.selectByPrimaryKey(onecomment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_COMMENT_OT_FOUND);
            }
            createNotify(comment,onecomment.getCommentator(),NotificationTypeEnum.REPLY_COMMENT,question.getTitle(),user.getName(),question.getId());
        }else {
            //回复问题  首先判断问题是够存在
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_COMMENT_OT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExMapperr.incComment(question);
            //增加通知
            createNotify(comment,question.getCreator(),NotificationTypeEnum.REPLY_QUESTION,question.getTitle(),user.getName(),question.getId());
        }

    }

    public List<CommentDTO> listByTargetId(Long id,CommentTypeEnum type) {
        //查出改问题的所有评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取所有评论人id
        Set<Long> commentors = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentors);
        //获取所有评论人
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment(comment);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }


    public void createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationType,String tile,String name,Long questionId){
        if(comment.getCommentator()==receiver){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setOuterId(questionId);
        notification.setReceiver(receiver);
        notification.setNotifiter(comment.getCommentator());
        notification.setType(notificationType.getType());
        notification.setNotifierName(name);
        notification.setOuterTitle(tile);
        notification.setStatus(0);
        notificationMapper.insert(notification);
    }


}
