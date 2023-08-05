package com.shop.app.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequestMapping("/member")
@Controller
public class MemberController {

	@GetMapping("/login.do")
	public void memberLogin() {}
	
}