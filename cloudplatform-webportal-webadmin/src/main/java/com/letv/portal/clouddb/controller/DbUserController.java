package com.letv.portal.clouddb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.python.service.IBuildTaskService;
import com.letv.portal.service.IDbUserService;

/**Program Name: DbUserController <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller
@RequestMapping("/db/user")
public class DbUserController {
	
	@Resource
	private IDbUserService dbUserService;
	@Resource
	private IBuildTaskService buildTaskService;
	
	private final static Logger logger = LoggerFactory.getLogger(DbUserController.class);
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String toList(HttpServletRequest request,HttpServletResponse response){
		return "/clouddb/mgr_dbuser_list";
	}
	
	/**Methods Name: list <br>
	 * Description: 根据dbName查询相关dbUser分页数据<br>
	 * @author name: liuhao1
	 * @param currentPage
	 * @param recordsPerPage
	 * @param dbName
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/list/{currentPage}/{recordsPerPage}/{dbName}",method=RequestMethod.GET)  
	public @ResponseBody ResultObject list(@PathVariable int currentPage,@PathVariable int recordsPerPage,@PathVariable String dbName,HttpServletRequest request) {
		Page page = new Page();
		page.setCurrentPage(currentPage);
		page.setRecordsPerPage(recordsPerPage);
	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbName", dbName);
		
		ResultObject obj = new ResultObject();
		obj.setData(this.dbUserService.findPagebyParams(params, page));
		return obj;
	}
	@RequestMapping(value="/build/{ids}",method=RequestMethod.GET)  
	public @ResponseBody ResultObject list(@PathVariable String ids,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		this.buildTaskService.buildUser(ids);
		return obj;
	}
	
}
