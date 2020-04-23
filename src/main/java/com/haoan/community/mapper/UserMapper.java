package com.haoan.community.mapper;


import com.haoan.community.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modfied) values (#{account_id},#{name},#{token},#{gmt_create},#{gmt_modfied})")
    public void saveUser(User user);
}
