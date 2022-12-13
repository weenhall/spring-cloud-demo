package com.ween;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.Callable;

@Slf4j
@RestController
public class SampleController implements ApplicationListener<ServletWebServerInitializedEvent> {

	@Resource
	private RestTemplate restTemplate;
	@Autowired
	private Tracer tracer;
	@Resource
	private SampleBackground sampleBackground;

	private Random random=new Random();
	private int port;

	@RequestMapping("/")
	public String hello() throws InterruptedException {
		Thread.sleep(this.random.nextInt(1000));
		log.info("Home page");
		String s=this.restTemplate.getForObject("http://localhost:"+this.port+"/hello2",String.class);
		return "hello"+s;
	}

	@RequestMapping("/call")
	public Callable<String> call(){
		return () -> {
			int millis=SampleController.this.random.nextInt(1000);
			Thread.sleep(millis);
			Span currentSpan=SampleController.this.tracer.currentSpan();
			currentSpan.tag("callable-sleep-millis",String.valueOf(millis));
			return "async hello:"+currentSpan;
		};
	}

	@RequestMapping("/hello2")
	public String hello2() throws InterruptedException {
		log.info("hello2");
		int millis=this.random.nextInt(1000);
		Thread.sleep(millis);
		this.tracer.currentSpan().tag("random-sleep-millis",String.valueOf(millis));
		return "hello2";
	}

	@RequestMapping("/async")
	public String async() throws InterruptedException {
		log.info("async");
		this.sampleBackground.background();
		return "ho";
	}

	@RequestMapping("/traced")
	public String traced() throws InterruptedException {
		Span span=this.tracer.nextSpan().name("http:customTraceEndpoint").start();
		int millis=this.random.nextInt(1000);
		log.info(String.format("Sleeping for [%d] millis",millis));
		Thread.sleep(millis);
		this.tracer.currentSpan().tag("random-sleep-millis",String.valueOf(millis));
		String s=this.restTemplate.getForObject("http://localhost"+this.port+"/call",String.class);
		span.end();
		return "traced/"+s;
	}

	@RequestMapping("/start")
	public String start() throws InterruptedException {
		int millis=this.random.nextInt(1000);
		log.info(String.format("Sleeping for [%d] millis",millis));
		Thread.sleep(millis);
		this.tracer.currentSpan().tag("random-sleep-millis",String.valueOf(millis));
		String s=this.restTemplate.getForObject("http://localhost:" + this.port + "/call", String.class);
		return "start/"+s;
	}
	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		this.port=event.getSource().getPort();
	}
}
