package org.aptech.metube.personal.controller;

import org.aptech.metube.personal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class TestController {
    @Autowired
    UserService userService;

//    @GetMapping("/test1")
//    public String test1 () throws NotFoundException {
//        userService.followUser("1");
//        return "Ok";
//    }

    @GetMapping("/test2")
    public String test2 (){
        return "Ok";
    }
}
