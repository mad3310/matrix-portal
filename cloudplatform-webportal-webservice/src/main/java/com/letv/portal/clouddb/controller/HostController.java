package com.letv.portal.clouddb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.portal.service.IHostService;

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
	
	
	@Resource
	private IHostService hostService;
	
	private final static Logger logger = LoggerFactory.getLogger(HostController.class);
	
	/**Methods Name: list <br>
	 * Description: 根据查询条件及分页信息获取分页数据<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/list",method=RequestMethod.POST)   //http://localhost:8080/api/db/list
	@ResponseBody
	public ResultObject list(@RequestBody Map<String,Object> params,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		obj.setData(this.hostService.selectByMap(params));
		return obj;
	}
}
