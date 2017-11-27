package com.dicksoy.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dicksoy.common.service.UserService;

@Controller
@RequestMapping
public class TestController {

	@Resource
	private UserService userService;
	
	@RequestMapping("test")
	public String index() {
		System.out.println("12312321312");
		System.out.println(null == userService.selectById(1L));
		return "index";
	}
}
