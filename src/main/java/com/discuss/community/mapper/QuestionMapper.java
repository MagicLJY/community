package com.discuss.community.mapper;

import com.discuss.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("select * from question")
    List<Question> list();
}
