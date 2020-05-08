package com.haoan.community.mapper;

import com.haoan.community.bean.Question;
import com.haoan.community.dto.QuestionQueryDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QuestionExMapper {
    void incView(Question question);
    void incComment(Question question);
    @Select("SELECT LAST_INSERT_ID()")
    Long lastInsertId();

    @Select("select * from question where   id!=#{id} and tag regexp #{tag}")
    List<Question> selectRelated(String tag,Long id);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}