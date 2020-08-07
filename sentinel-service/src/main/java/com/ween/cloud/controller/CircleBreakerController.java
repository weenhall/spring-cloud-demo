package com.ween.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ween.cloud.domain.CommonResult;
import com.ween.cloud.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 测试熔断功能
 */
@RestController
@RequestMapping("/breaker")
public class CircleBreakerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @RequestMapping("/fallback/{id}")
    @SentinelResource(value = "fallBack", fallback = "handleFallback")
    public CommonResult fallBack(@PathVariable Long id) {
        User obj= restTemplate.getForObject(  userServiceUrl+"/user/{1}", User.class, id);
        return new CommonResult(obj,"通过调用nacos-user-service服务返回",200);
    }

    public CommonResult handleFallback(Long id) {
        User defaultUser = new User(-1L, "defaultUser", 99);
        return new CommonResult<>(defaultUser, "服务降级返回", 200);
    }

    @RequestMapping("/fallbackException/{id}")
    @SentinelResource(value = "fallbackException", fallback = "handleFallback2", exceptionsToIgnore = {NullPointerException.class})
    public CommonResult fallbackException(@PathVariable Long id) {
        if (id == 1) {
            throw new IndexOutOfBoundsException();
        } else if (id == 2) {
            throw new NullPointerException();
        }
        User user= restTemplate.getForObject(userServiceUrl + "/user/{1}", User.class, id);
        return new CommonResult(user,"通过调用nacos-user-service服务返回",200);
    }

    public CommonResult handleFallback2(@PathVariable Long id, Throwable e) {
        logger.error("handleFallback2 id:{},throwable class:{}", id, e.getClass());
        User defaultUser = new User(-2L, "defaultUser2", 99);
        return new CommonResult<>(defaultUser, "服务降级返回", 200);
    }
}
