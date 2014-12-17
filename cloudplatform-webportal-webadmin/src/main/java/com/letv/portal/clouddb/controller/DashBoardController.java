package com.letv.portal.clouddb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.result.ResultObject;
import com.letv.portal.proxy.IDashBoardProxy;
import com.letv.portal.service.IContainerService;
import com.letv.portal.zabbixPush.IZabbixPushService;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {
	
	@Resource
	private IDashBoardProxy dashBoardProxy;

	@Resource
	public IZabbixPushService zabbixPushService;
	
	@Resource
	public IContainerService containerService;
	
	private final static Logger logger = LoggerFactory.getLogger(DashBoardController.class);
	
	/**Methods Name: list <br>
	 * Description: dashboard资源统计<br>
	 * @author name: liuhao1
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/statistics",method=RequestMethod.GET)
	public @ResponseBody ResultObject list(ResultObject result) {
		result.setData(this.dashBoardProxy.selectManagerResource());
		return result;
	}
	@RequestMapping(value="/monitor/mcluster",method=RequestMethod.GET)
	public @ResponseBody ResultObject mclusterMonitor(ResultObject result) {
		result.setData(this.dashBoardProxy.selectMclusterMonitor());
		return result;
	}
	@RequestMapping(value="/zabbix/add",method=RequestMethod.GET)
	public @ResponseBody ResultObject add(ResultObject result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mclusterId", 17);
		zabbixPushService.createMultiContainerPushZabbixInfo(this.containerService.selectByMap(map));
		return result; 
	}
	@RequestMapping(value="/zabbix/remove",method=RequestMethod.GET)
	public @ResponseBody ResultObject remove(ResultObject result) {
		Map<String, Object> map = new HashMap<String, Object>();
		   map.put("mclusterId", 17);
			
		   Boolean flag  =false;

			try {
				
		     flag =	zabbixPushService.deleteMutilContainerPushZabbixInfo(this.containerService.selectByMap(map));
		     System.out.println(flag);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result; 
	}
}
