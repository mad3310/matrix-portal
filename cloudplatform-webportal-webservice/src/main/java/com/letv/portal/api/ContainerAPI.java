
package com.letv.portal.api;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbService;

/**Program Name: ContainerAPI <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月20日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller
@RequestMapping("/container")
public class ContainerAPI {
	@Resource
	private IContainerService containerService;
	@Resource
	private IDbService dbService;
	
	private final static Logger logger = LoggerFactory.getLogger(ContainerAPI.class);
	
	
	
}
