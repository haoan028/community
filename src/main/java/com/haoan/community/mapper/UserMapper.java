package com.haoan.community.mapper;


import com.haoan.community.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//@Mapper
public interface UserMapper {

    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modfied,avatar_url) values (#{account_id},#{name},#{token},#{gmt_create},#{gmt_modfied},#{avatar_url})")
    public void saveUser(User user);

    @Select("select * from user where account_id=#{account_id}")
    public User findById(String account_id);
}
