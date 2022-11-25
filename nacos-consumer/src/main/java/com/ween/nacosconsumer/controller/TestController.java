package com.ween.nacosconsumer.controller;

import com.ween.nacosconsumer.client.EchoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestTemplate restTemplate1;

	@Autowired
	private EchoApi echoApi;

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/echo-rest/{str}")
	public String rest(@PathVariable String str) {
		return restTemplate.getForObject("http://service-provider/echo/" + str,
				String.class);
	}

	@GetMapping("/index")
	public String index() {
		return restTemplate1.getForObject("http://service-provider", String.class);
	}

	@GetMapping("/test")
	public String test() {
		return restTemplate1.getForObject("http://service-provider/test", String.class);
	}

	@GetMapping("/sleep")
	public String sleep() {
		return restTemplate1.getForObject("http://service-provider/sleep", String.class);
	}

	@GetMapping("/notFound-feign")
	public String notFound() {
		return echoApi.notFound();
	}

	@GetMapping("/divide-feign")
	public String divide(@RequestParam Integer a, @RequestParam Integer b) {
		return echoApi.divide(a, b);
	}

	@GetMapping("/divide-feign2")
	public String divide(@RequestParam Integer a) {
		return echoApi.divide(a);
	}

	@GetMapping("/echo-feign/{str}")
	public String feign(@PathVariable String str) {
		return echoApi.echo(str);
	}

	@GetMapping("/services/{service}")
	public Object client(@PathVariable String service) {
		return discoveryClient.getInstances(service);
	}

	@GetMapping("/services")
	public Object services() {
		return discoveryClient.getServices();
	}
}
