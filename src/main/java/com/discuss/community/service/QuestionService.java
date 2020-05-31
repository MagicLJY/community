package com.discuss.community.service;

import com.discuss.community.dto.PaginationDTO;
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
//用于组装question和user实体类，组合成questionDTO，
// 再由quesetionDTO加各种分页信息组合成paginationDTO,返回到首页
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    //这个用来获得首页的分页信息
    public PaginationDTO list(Integer page, Integer size) {

        //先获得问题的总数，然后根据总数和分页信息，来判断每个分页该如何显示
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        //根据分页信息设置sql：limit的偏移量，找出当前页应该显示的问题
        //size*(page-1)
        Integer offset = size * (page - 1);
        if(offset<0)
            offset=1;
        List<Question> questions = questionMapper.list(offset, size);  //offset是limit的偏移量
        //将question类和user类放到questionDTO中
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将得到的question和user放到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将当前页的所有问题的信息放到分页类中
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    //这个用来获取个人提出的问题，所有有个id，用于从数据库查问题
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //先获得问题的总数，然后根据总数和分页信息，来判断每个分页该如何显示
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        //根据分页信息设置sql：limit的偏移量，找出当前页应该显示的问题
        //size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(userId,offset, size);  //offset是limit的偏移量
        //将question类和user类放到questionDTO中
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将得到的question和user放到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将当前页的所有问题的信息放到分页类中
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
