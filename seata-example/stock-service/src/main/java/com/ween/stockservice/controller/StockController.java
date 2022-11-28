package com.ween.stockservice.controller;

import com.ween.stockservice.entity.Stock;
import com.ween.stockservice.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@RestController
@RequestMapping("/stock")
public class StockController {

	@Resource
	private StockService stockService;

	@GetMapping("/deduct")
	public ResponseEntity deduct(String code,Integer count){
		stockService.deduct(code,count);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/initdata")
	public void init(){
		System.out.println("initdata");
		for (int i = 0; i < 10; i++) {
			Stock stock=new Stock();
			stock.setCode("s"+i);
			stock.setCount((long) (i+10));
			stockService.save(stock);
		}
	}
}
