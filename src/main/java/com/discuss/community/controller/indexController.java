package com.discuss.community.controller;

import com.discuss.community.dto.PaginationDTO;
import com.discuss.community.dto.QuestionDTO;
import com.discuss.community.mapper.UserMapper;
import com.discuss.community.model.User;
import com.discuss.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JY Lin
 * @time -2020-05-17-12:22
 **/
@Controller
public class indexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //代表根目录，就是localhost
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "5")Integer size){
        //访问首页时，检测浏览器中的token是否与本地数据库中有对应，有的话直接免密登录
        //在拦截器中实现了，如果获取了user信息那直接显示的就是登录后的界面，如果没获取到，就显示登录按钮

        //获取问题列表
        PaginationDTO pagination =questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
