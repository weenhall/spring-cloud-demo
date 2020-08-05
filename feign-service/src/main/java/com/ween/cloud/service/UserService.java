package com.ween.cloud.service;

import com.ween.cloud.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign是声明式的服务调用工具，我们只需创建一个接口并用注解的方式来配置它，
 * 就可以实现对某个服务接口的调用，简化了直接使用RestTemplate来调用服务接口的开发量。
 * Feign具备可插拔的注解支持，同时支持Feign注解、JAX-RS注解及SpringMvc注解。
 * 当使用Feign时，Spring Cloud集成了Ribbon和Eureka以提供负载均衡的服务调用及基于Hystrix的服务容错保护功能
 */
@FeignClient(value = "user-service",fallback = UserFallbackService.class)
public interface UserService {

    @PostMapping("/user/add")
    void create(@RequestBody User user);

    @GetMapping("/user/{id}")
    User getUser(@PathVariable Long id);

    @PostMapping("/user/update")
    void update(@RequestBody User user);

    @PostMapping("user/delete/{id}")
    void delete(@PathVariable Long id);
}
