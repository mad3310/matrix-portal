package com.letv.portal.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.util.HttpUtil;

@Controller
@RequestMapping("/host")
public class HostController {
	@RequestMapping(value="/listAll")
	public void listAll(HttpServletRequest request,HttpServletResponse response){
		
			Map<String,String> map = new HashMap<String,String>();
			
			
			PrintWriter out;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(HttpUtil.getResultFromDBAPI(request,"/host/list",map));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
