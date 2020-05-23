package com.discuss.community.controller;

import com.discuss.community.dto.AccessTokenDTO;
import com.discuss.community.dto.GithubUser;
import com.discuss.community.mapper.UserMapper;
import com.discuss.community.model.User;
import com.discuss.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.Name;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author JY Lin
 * @time -2020-05-17-19:18
 * 用于接收按下登录按钮后，返回callback的code和state
 * 然后用返回的信息获取user信息，并保存cookie
 **/
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider; //把用post传参的实体类加载进来
    //引入配置文件信息
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/callback")
    //session是通过HttpServletRequest得到的
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){
        //给post传递的参数赋值
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        //下面方法中包含了将参数打包成json用post传递的过程,这步执行完之后会返回access_token值
        //在controller中想要用get或者post访问某个URL，必须要用到HttpClient
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);                 //此处返回了user信息，并转换成了java对象
        if(githubUser!=null){
            //信息获取成功后，将信息保存到database
            User user = new User();   //这个类并没有添加标签，所以这里直接new一个
            String token = UUID.randomUUID().toString();
            user.setToken(token);//用UUID的方式写token
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));//强制转换一下类型
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //将token添加到cookie，这个cookie保存在浏览器，由此就可以根据浏览器的cookie来判断是否可以免密登录
            //浏览器中的cookie通过request来获取
            //当cookie里的token在数据库中时，则从数据库中读取user信息，不必再登录
            response.addCookie(new Cookie("token",token));

            //登录成功写session,用request
            //request.getSession().setAttribute("user",githubUser);
            //不采用redirect的话地址不会改变，还是callback，但是会渲染成return的页面
            //采用redirect，地址url和页面都改变
            return "redirect:/";
        } else{
            System.out.println("kong");
            //cookie的判断在index页面里，所以返回“/”之后会改变。
            return "redirect:/";
        }


    }

}
