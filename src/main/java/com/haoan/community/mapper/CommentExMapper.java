package com.haoan.community.mapper;

import com.haoan.community.bean.Comment;
import com.haoan.community.bean.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExMapper {
    void incComment(Comment comment);
}