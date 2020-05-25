package com.discuss.community.controller;

import com.discuss.community.mapper.QuestionMapper;
import com.discuss.community.mapper.UserMapper;
import com.discuss.community.model.Question;
import com.discuss.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author JY Lin
 * @time -2020-05-23-19:41
 **/
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    //get类型时，渲染页面
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    //当页面向服务器post信息的时候，执行请求
    //后面的参数指的是在哪个页面接收信息，此处接收publish页面的信息，至于最后再跳转到哪个页面由return决定
    //接收前端信息，保存到数据库中
    @PostMapping("/publish")
    public String  doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,                 //通过request获取当前登录的用户信息
            Model model){                               //model用于给前端传递信息
        //先将值放到model中，这样下次输入的时候可以在前端下拉框里找到上次的内容
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        //当前登录的话就获取user信息，未登录就向model中添加未登录
        User user=null;
        Cookie[] cookies = request.getCookies();
        if(cookies !=null) {
            for (Cookie cookie : cookies) {              //寻找cookies[]中名字为token的值，找到数据库中对应的user信息
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);       //把user信息加入到session中，与前端（服务器）交互
                    }
                    break;
                }
            }
        }
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";    //没有用户登录，就在本页显示未登录，此时前端界面已经改了
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";     //发布成功返回首页
    }
}
