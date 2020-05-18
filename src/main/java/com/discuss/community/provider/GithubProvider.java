package com.discuss.community.provider;

import com.alibaba.fastjson.JSON;
import com.discuss.community.dto.AccessTokenDTO;
import com.discuss.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;

/**
 * @author JY Lin
 * @time -2020-05-17-19:34
 * 用OkHttp给github提交参数
 **/
@Component
public class GithubProvider {
    //想access_token接口传递参数，并返回access_token
    public String  getAccessToken(AccessTokenDTO accessTokenDTO){ //方法参数就是实体类中定义的类
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create( mediaType,JSON.toJSONString(accessTokenDTO));    //将参数类转换成json
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")  //这里输入要post的url
                .post(body)             //把json文件post出去
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string =response.body().string();
            String token=string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    //携带accessToken参数访问user接口,返回user信息
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            //返回值
            Response response = client.newCall(request).execute();
            //从response中取出返回值，此时string是一个json
            String string = response.body().string();
            //json转换为java对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;

        } catch (IOException e) {

        }
        return null;

    }
}
