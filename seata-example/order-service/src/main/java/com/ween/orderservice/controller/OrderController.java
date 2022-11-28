package com.ween.orderservice.controller;

import com.ween.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Resource
	private OrderService orderService;

	@RequestMapping("/commit")
	public Boolean placeOrderCommit(String product){
		orderService.placeOrder("test",product,2);
		return true;
	}
}
