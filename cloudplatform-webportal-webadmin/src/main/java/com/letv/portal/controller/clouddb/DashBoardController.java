package com.letv.portal.controller.clouddb;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.result.ResultObject;
import com.letv.portal.proxy.IDashBoardProxy;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.adminoplog.ClassAoLog;
import com.letv.portal.zabbixPush.IZabbixPushService;

@ClassAoLog(module="Dashboard")
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
	@RequestMapping(value="/monitor/{monitorType}",method=RequestMethod.GET)
	public @ResponseBody ResultObject mclusterMonitor(@PathVariable Long monitorType, ResultObject result) {
		result.setData(this.dashBoardProxy.selectMonitorAlertWithMultiThread(monitorType));
		return result; 
	}
}
