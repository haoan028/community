package com.haoan.community.mapper;

import com.haoan.community.bean.Question;
import org.apache.ibatis.annotations.*;

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

    @Select("select * from question where id=#{id}")
    Question findById(Integer id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmt_modified},tag=#{tag} where id = #{id}")
    void update(Question question);

    @Select("select last_insert_id() ")
    Integer lastInsertId();
}