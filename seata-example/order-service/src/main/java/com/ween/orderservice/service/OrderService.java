package com.ween.orderservice.service;

import com.ween.orderservice.api.StockFeignClient;
import com.ween.orderservice.entity.Order;
import com.ween.orderservice.repository.OrderRepository;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderService {

	@Resource
	private OrderRepository repository;
	@Resource
	private StockFeignClient stockFeignClient;

	@GlobalTransactional
	@Transactional(rollbackFor = Exception.class)
	public void placeOrder(String userId,String code,Integer count){
		BigDecimal price=new BigDecimal(count).multiply(BigDecimal.TEN);
		Order order=new Order();
		order.setUserId(userId);
		order.setStockCode(code);
		order.setPrice(price);
		order.setStockCount(count);
		repository.save(order);
		stockFeignClient.deduct(code,count);
	}
}
