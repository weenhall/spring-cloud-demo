package com.ween.sentinel.example.config;

import com.alibaba.cloud.circuitbreaker.sentinel.SentinelCircuitBreakerFactory;
import com.alibaba.cloud.circuitbreaker.sentinel.SentinelConfigBuilder;
import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ween.sentinel.example.util.ExceptionUtil;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Configuration
public class BeanConfig {

	@Bean
	@SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate restTemplate2() {
		return new RestTemplate();
	}

	@Bean
	public Converter<String, List<FlowRule>> myConverter() {
		return new JsonFlowRuleListConverter();
	}

	@Bean
	public Customizer<SentinelCircuitBreakerFactory> defaultConfig() {
		return factory -> factory.configureDefault(id ->
				new SentinelConfigBuilder().resourceName(id)
						.rules(Collections.singletonList(new DegradeRule(id)
								.setGrade(RuleConstant.DEGRADE_GRADE_RT).setCount(100)
								.setTimeWindow(10))
						).build());
	}

	static class JsonFlowRuleListConverter implements Converter<String, List<FlowRule>>{

		@Override
		public List<FlowRule> convert(String s) {
			return JSON.parseObject(s,new TypeReference<List<FlowRule>>(){});
		}
	}
}
