package com.ween.sentinel.example.controller;

import javax.annotation.Resource;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

@RestController
public class TestController {

	@Resource
	private RestTemplate restTemplate;
	@Resource
	private CircuitBreakerFactory circuitBreakerFactory;

	@GetMapping("/hello")
	@SentinelResource("resource")
	public String hello(){
		return "hello";
	}

	@GetMapping("/aa")
	@SentinelResource("aa")
	public String aa(int a,int b){
		return "hello test";
	}

	@GetMapping("/template")
	public String client(){
		return restTemplate.getForObject("https://baidu.com",String.class);
	}

	@GetMapping("/slow")
	public String slow(){
		return circuitBreakerFactory.create("slow").run(()->{
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "slow";
		});
	}
}
