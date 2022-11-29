package com.ween.nacosconsumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-provider", fallback = EchoApi.EchoServiceFallback.class,
		configuration = EchoApi.FeignConfiguration.class)
public interface EchoApi {

	@GetMapping("/echo/{str}")
	String echo(@PathVariable("str") String str);

	@GetMapping("/divide")
	String divide(@RequestParam("a") Integer a, @RequestParam("b") Integer b);

	default String divide(Integer a) {
		return divide(a, 0);
	}

	@GetMapping("/notFound")
	String notFound();

	class FeignConfiguration {

		@Bean
		public EchoServiceFallback echoServiceFallback() {
			return new EchoServiceFallback();
		}

	}

	class EchoServiceFallback implements EchoApi {

		@Override
		public String echo(@PathVariable("str") String str) {
			return "echo fallback";
		}

		@Override
		public String divide(@RequestParam Integer a, @RequestParam Integer b) {
			return "divide fallback";
		}

		@Override
		public String notFound() {
			return "notFound fallback";
		}

	}
}
