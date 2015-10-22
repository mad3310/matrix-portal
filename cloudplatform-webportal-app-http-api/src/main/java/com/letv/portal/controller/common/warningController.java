package com.letv.portal.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.result.ResultObject;

@Controller("warningController")
public class warningController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value ="/jettyMonitor")
	public @ResponseBody ResultObject jettyMonitor(ResultObject obj){
		return obj;
	}
	
	@RequestMapping(value ="/dbMonitor")
	public @ResponseBody ResultObject dbMonitor(ResultObject obj){
		this.jdbcTemplate.execute("select 1 from dual");
		return obj;
	}
}
