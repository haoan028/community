package com.haoan.community.dto;

import com.haoan.community.bean.Comment;
import com.haoan.community.bean.Question;
import com.haoan.community.bean.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Comment comment;
    private User user;
}
