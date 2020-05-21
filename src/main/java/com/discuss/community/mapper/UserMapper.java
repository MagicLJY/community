package com.discuss.community.mapper;

import com.discuss.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author JY Lin
 * @time -2020-05-21-14:44
 **/
//通过这个类中的这个insert方法与数据库交互
@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
    //这里的user只是指他们形参类型，所以不需要new一个
}
