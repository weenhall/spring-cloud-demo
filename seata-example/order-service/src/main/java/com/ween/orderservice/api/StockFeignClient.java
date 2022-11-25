package com.ween.orderservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stock-service")
public interface StockFeignClient {

	@GetMapping("/stock/deduct")
	ResponseEntity deduct(@RequestParam("code") String code, @RequestParam("count") Integer count);
}
