package com.discuss.community.controller;

import com.discuss.community.mapper.UserMapper;
import com.discuss.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author JY Lin
 * @time -2020-05-17-12:22
 **/
@Controller
public class indexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/") //代表根目录，就是localhost
    public String index(HttpServletRequest request){
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
        return "index";
    }
}
