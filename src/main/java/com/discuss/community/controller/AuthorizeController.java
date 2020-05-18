package com.discuss.community.controller;

import com.discuss.community.dto.AccessTokenDTO;
import com.discuss.community.dto.GithubUser;
import com.discuss.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author JY Lin
 * @time -2020-05-17-19:18
 * 用于接收返回callback的code和state
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
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state") String state){
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
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        //经过一系列流程后最后还是返回主界面
        return "index";

    }

}
