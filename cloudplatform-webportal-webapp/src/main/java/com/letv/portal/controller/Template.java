package com.letv.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/template")
public class Template {
	@RequestMapping(value="/show")
	public ModelAndView test(){
		System.out.println("a"); 
		return new ModelAndView("clouddb/template");
	}
	
}
