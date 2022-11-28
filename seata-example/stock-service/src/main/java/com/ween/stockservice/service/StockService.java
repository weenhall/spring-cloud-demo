package com.ween.stockservice.service;

import javax.annotation.Resource;
import com.ween.stockservice.entity.Stock;
import org.springframework.stereotype.Service;
import com.ween.stockservice.repository.StockRepository;
import org.springframework.transaction.annotation.Transactional;

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
		repository.saveAndFlush(stock);
	}

	public void save(Stock stock){
		repository.save(stock);
	}
}
