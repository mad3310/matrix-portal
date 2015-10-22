package com.letv.portal.controller.cloudgce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.result.ResultObject;
import com.letv.portal.model.task.service.ITaskChainService;
import com.letv.portal.service.IBuildService;

@Controller("gcebuild")
@RequestMapping("/build")
public class BuildController {
	
	@Autowired
	private ITaskChainService taskChainService;
	
	private final static Logger logger = LoggerFactory.getLogger(BuildController.class);
	
	/**Methods Name: list4Gce <br>
	 * Description: 查看gce创建进度<br>
	 * @author name: liuhao1
	 * @param gceId
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/gce/{gceId}", method=RequestMethod.GET)   
	public @ResponseBody ResultObject list4Gce(@PathVariable Long gceId,ResultObject result) {
		result.setData(this.taskChainService.getStepByGceId(gceId));
		return result;
	}	
}
