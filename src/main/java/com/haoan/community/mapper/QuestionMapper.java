package com.haoan.community.mapper;

import com.haoan.community.bean.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface QuestionMapper {


    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    void saveQuestion(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> findAll(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(*) from question")
    Integer count();

    @Select("select * from question where creator=#{id} limit #{offset},#{size}")
    List<Question> findByUser(@Param("id") String id,@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(*) from question where creator=#{id}")
    Integer userCount(@Param("id") String id);
}