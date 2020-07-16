package com.ween.cloud.controller;

import com.ween.cloud.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/user")
public class UserRibbonController {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @PostMapping(value = "/add")
    public String add(@RequestBody User user) {
        return restTemplate.postForObject(userServiceUrl+"/user/add",user,String.class);
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl+"/user/{1}",User.class,id);
    }

    @PostMapping(value = "/update")
    public String update(@RequestBody User user) {
        return restTemplate.postForObject(userServiceUrl+"/user/update",user,String.class);
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id) {
        return restTemplate.postForObject(userServiceUrl+"/user/delete{1}",null,String.class,id);
    }
}
