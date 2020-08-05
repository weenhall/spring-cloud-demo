package com.ween.cloud.controller;

import com.ween.cloud.pojo.User;
import com.ween.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/create")
    public String add(@RequestBody User user) {
        userService.create(user);
        return "1";
    }

    @PostMapping("/update")
    public String update(@RequestBody User user) {
        userService.update(user);
        return "1";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "1";
    }
}
