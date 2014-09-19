package com.letv.portal.clouddb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.result.ResultObject;
import com.letv.portal.service.IHostService;

@Controller
@RequestMapping("/host")
public class HostController {
	
	@Resource
	private IHostService hostService;
	
	private final static Logger logger = LoggerFactory.getLogger(HostController.class);
	
	/**Methods Name: list <br>
	 * Description: 根据查询条件及分页信息获取分页数据  http://localhost:8080/host/list<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/list/{hostName}",method=RequestMethod.GET)   
	
	public @ResponseBody ResultObject list(@PathVariable String hostName,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("hostName", hostName);
		ResultObject obj = new ResultObject();
		obj.setData(this.hostService.selectByMap(map));
		return obj;
	}
	
}
