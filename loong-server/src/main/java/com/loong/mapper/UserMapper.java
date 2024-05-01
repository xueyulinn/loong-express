package com.loong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.loong.entity.User;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User selectByOpenId(String openid);

    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User selectByUserId(Long userId);

    @Select("select count(*) from user where date(create_time) = #{date}")
    Integer selectByCreateDate(String date);

    @Select("select count(*) from user where date(create_time) <= #{date}")
    Integer selectBeforeDate(String date);

    
}
    
