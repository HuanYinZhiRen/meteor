package com.nilin.meteor.web.controller.auth;

import com.nilin.meteor.common.util.ThreadUtils;
import com.nilin.meteor.entity.Result;
import com.sun.deploy.net.HttpRequest;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/loginAuth")
public class LoginAuthController {

    @RequestMapping(value = "login")
    public Result login(HttpServletRequest request){

        Result result = new Result();
        return result;
    }
}
