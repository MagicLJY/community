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
        Cookie[] cookies = request.getCookies();
        if(cookies !=null) {
            for (Cookie cookie : cookies) {              //寻找cookies[]中名字为token的值，找到数据库中对应的user信息
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);       //把user信息加入到session中，与前端（服务器）交互
                    }
                    break;
                }
            }
        }

        //获取问题列表
        PaginationDTO pagination =questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
