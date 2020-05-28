package com.discuss.community.service;

import com.discuss.community.dto.QuestionDTO;
import com.discuss.community.mapper.QuestionMapper;
import com.discuss.community.mapper.UserMapper;
import com.discuss.community.model.Question;
import com.discuss.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JY Lin
 * @time -2020-05-28-15:58
 **/
//用于组装question和user实体类，组合成questionDTO，返回到首页
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> list() {
        List<Question> questions=questionMapper.list();
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questions) {
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将得到的question和user放到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
