package com.discuss.community.mapper;

import com.discuss.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JY Lin
 * @time -2020-05-25-17:05
 **/
//添加数据到问题表
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag,comment_count) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag},#{commentCount})")
    void create(Question question);
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
    //查找所有的问题数
    @Select("select count(1) from question")
    Integer count();
}
