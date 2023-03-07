package com.github.linyuzai.concept.sample.cloud.web;

import com.github.linyuzai.cloud.web.core.result.annotation.ResultMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {

    @ResultMessage(success = "登录成功")
    @GetMapping("/login")
    public void login() {

    }
}
