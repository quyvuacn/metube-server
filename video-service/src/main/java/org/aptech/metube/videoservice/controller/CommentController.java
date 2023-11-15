package org.aptech.metube.videoservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment")
public class CommentController {
    @GetMapping(value = "/get")
    public String get(){
        return "Ok";
    }
}
