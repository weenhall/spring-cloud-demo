package com.ween.cloud.controller;

import com.ween.cloud.pojo.User;
import com.ween.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @RestController=@Controller+@ReponseBody
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PostMapping(value = "/add")
    public String add(@RequestBody User user) {
        userService.add(user);
        logger.info("添加用户,用户名称为: {}", user.getName());
        return "添加成功";
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable Long id) {
        User user = userService.findById(id);
        logger.info("查找用户,用户名称为: {}", user.getName());
        return user;
    }

    @GetMapping("/getUserByIds")
    public List<User> getUserByIds(@RequestParam List<Long> ids){
        List users=new LinkedList();
        ids.forEach(id->{
            users.add(userService.findById(id));
        });
        return users;
    }

    @PostMapping(value = "/update")
    public String update(@RequestBody User user) {
        userService.update(user);
        logger.info("更新用户,用户名称为: {}", user.getName());
        return "更新成功";
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        logger.info("删除成功");
        return "删除成功";
    }

}
