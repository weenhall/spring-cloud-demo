package com.ween.cloud.controller;

import com.ween.cloud.domain.User;
import com.ween.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserFeignController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PostMapping
    public String create(@RequestBody User user){
        userService.add(user);
        return "1";
    }
}
