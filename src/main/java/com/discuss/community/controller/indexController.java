package com.discuss.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author JY Lin
 * @time -2020-05-17-12:22
 **/
@Controller
public class indexController {
    @GetMapping("/") //代表根目录，就是localhost
    public String index(){ return "index"; }
}
