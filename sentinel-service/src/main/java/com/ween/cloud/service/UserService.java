package com.ween.cloud.service;

import com.ween.cloud.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "nacos-user-service",fallback = UserFallbackService.class)
public interface UserService {

    @PostMapping("/user/add")
    void add(@RequestBody User user);

    @GetMapping("user/{id}")
    User getUser(@PathVariable Long id);
}
