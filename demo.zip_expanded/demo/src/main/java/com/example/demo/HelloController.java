package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HelloController {
	@RequestMapping("/hello")
	public String hello() {
		return "Hello, world!";
	}
}
