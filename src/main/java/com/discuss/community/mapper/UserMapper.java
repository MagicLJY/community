package com.discuss.community.mapper;

import com.discuss.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author JY Lin
 * @time -2020-05-21-14:44
 **/
//通过这个类中的这个insert方法与数据库交互
@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);  //这里是一个类所以不用加注解
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);   //只是一个形参的时候需要加@Param注解

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer creator);
    //这里的user只是指他们形参类型，所以不需要new一个
}
