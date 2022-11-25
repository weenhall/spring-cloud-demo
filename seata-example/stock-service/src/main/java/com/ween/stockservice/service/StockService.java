package com.ween.stockservice.service;

import com.ween.stockservice.entity.Stock;
import com.ween.stockservice.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class StockService {

	@Resource
	private StockRepository repository;

	@Transactional(rollbackFor = Exception.class)
	public void deduct(String code,int count){
		if("product-1".equals(code)){
			throw new RuntimeException("模拟业务异常");
		}
		Stock stock=repository.findByCode(code);
		stock.setCount(stock.getCount()-count);
		repository.save(stock);
	}
}
