package com.ween.cloud.controller;

import com.ween.cloud.pojo.User;
import com.ween.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/user")
public class UserHystrixController {

    @Autowired
    private UserService userService;

    //测试服务不可用时返回默认值
    @GetMapping("/testFallback/{id}")
    public User testFallback(@PathVariable Long id) {
        return userService.get(id);
    }

    //测试HystrixCommand中的参数
    @GetMapping("/testCommand/{id}")
    public User testCommand(@PathVariable Long id) {
        return userService.getUserCommand(id);
    }

    //测试调用服务异常时
    @GetMapping("/testException/{id}")
    public User testException(@PathVariable Long id) {
        return userService.getUserException(id);
    }

    //测试从缓存中获取数据
    @GetMapping("/testCache/{id}")
    public User testCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.getUserCache(id);
        return userService.getUserCache(id);
    }

    //测试移除缓存
    @GetMapping("/testRemoveCache/{id}")
    public User testRemoveCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.removeCache(id);
        return userService.getUserCache(id);
    }

    //测试合并请求
    @GetMapping("/tsetCollapser")
    public String testCollapser() throws ExecutionException, InterruptedException {
        Future<User> f1 = userService.getUserFuture(1L);
        Future<User> f2 = userService.getUserFuture(2L);
        f1.get();
        f2.get();
        Thread.sleep(200);
        Future<User> f3 = userService.getUserFuture(3L);
        f3.get();
        return "1";
    }
}
