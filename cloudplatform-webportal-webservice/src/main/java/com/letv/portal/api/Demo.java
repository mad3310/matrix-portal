package com.letv.portal.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.letv.portal.model.StarModel;

@Controller
@RequestMapping("/demo")
public class Demo {
	@RequestMapping
	public StarModel test(StarModel starModel) {
		System.out.println(starModel.getName());
		
		StarModel model = new StarModel();
		model.setName("刘德华");
		return model;
	}
	
	@RequestMapping(value="/show")
	public void test(){
		System.out.println("a"); 
	}
	
}
