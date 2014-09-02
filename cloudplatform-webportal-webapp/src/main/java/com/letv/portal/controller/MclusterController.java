package com.letv.portal.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.letv.common.util.HttpUtil;

@Controller
@RequestMapping("/mcluster")
public class MclusterController {
	
	@RequestMapping("/list/data")   //http://localhost:8080/mcluster/list/data
	public void listData(HttpServletRequest request,HttpServletResponse response) {
		//取出session中用户id，追加到http rest请求中
		Map<String,String> map = new HashMap<String,String>();
		map.put("createUser", (String) request.getSession().getAttribute("userId"));
		System.out.println(request.getParameter("mclusterName"));
		PrintWriter out;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(HttpUtil.getResultFromDBAPI(request,"/mcluster/list",map));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@RequestMapping("/list/mgrData")   //http://localhost:8080/mcluster/list/mgrData
	public void mgrData(HttpServletRequest request,HttpServletResponse response) {
		PrintWriter out;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(HttpUtil.getResultFromDBAPI(request,"/mcluster/list",null));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@RequestMapping("/mgrList")   //http://localhost:8080/mcluster/list
	public String mgr_list(HttpServletRequest request,HttpServletResponse response) {
		return "/clouddb/mgr_mcluster_list";
	}

	@RequestMapping("/save")   //http://localhost:8080/mcluster/save
	public String save(HttpServletRequest request,HttpServletResponse response) {
		
		//取出session中用户id，追加到http rest请求中
		Map<String,String> map = new HashMap<String,String>();
		map.put("createUser", (String) request.getSession().getAttribute("userId"));
		map.put("status", "1");
		
		String result = HttpUtil.getResultFromDBAPI(request,"/mcluster/save",map);
		if(result.contains("\"result\":1")) {
			return "redirect:/mcluster/list";
		}
		return "/common/error";
	}
	
	@RequestMapping(value="/mgrMclusterInfo")
	public String mgrMclusterInfo(HttpServletRequest request,HttpServletResponse response){
		String result = HttpUtil.getResultFromDBAPI(request,"/mcluster/getById",null);
		ObjectMapper resultMapper = new ObjectMapper();
		Map jsonResult = null;
		
		try {
			jsonResult = resultMapper.readValue(result, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("mclusterInfoList", jsonResult.get("data"));
		return "/clouddb/mgr_mcluster_info";
	}
}
