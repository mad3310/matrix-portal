package com.letv.portal.clouddb.controller;

import java.util.HashMap;
import java.util.List;
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

import com.letv.common.result.ResultObject;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.service.IDbUserService;

/**Program Name: DbUserController <br>
 * Description:  db用户<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月13日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller
@RequestMapping("/db/user")
public class DbUserController {
	
	@Resource
	private IDbUserService dbUserService;
	
	private final static Logger logger = LoggerFactory.getLogger(DbUserController.class);
	
	@RequestMapping(value="/list/",method=RequestMethod.GET)
	public String toList(HttpServletRequest request,HttpServletResponse response){
		return "/clouddb/user_db_detail";
	}
	
	/**Methods Name: list <br>
	 * Description: db列表 http://localhost:8080/db/user/list/{dbId}<br>
	 * @author name: liuhao1
	 * @param dbId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/list/{dbId}", method=RequestMethod.GET)   
	public @ResponseBody ResultObject list(@PathVariable String dbId,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		obj.setData(this.dbUserService.selectByDbId(dbId));
		return obj;
	}
	
	/**Methods Name: save <br>
	 * Description: 保存创建信息  http://localhost:8080/db/user/save<br>
	 * @author name: liuhao1
	 * @param dbApplyStandardModel
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)   
	public String save(DbUserModel dbUserModel, HttpServletRequest request) {
		String dbId = dbUserModel.getDbId();
		String[] ips = dbUserModel.getAcceptIp().split(",");
		
		dbUserModel.setCreateUser((String)request.getSession().getAttribute("userId"));
		for (String ip : ips) {
			dbUserModel.setAcceptIp(ip);
			this.dbUserService.insert(dbUserModel);
		}
		return "redirect:/db/detail/" + dbId;
	}
	
	
	@RequestMapping(value="/validate",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> validate(DbUserModel dbUserModel,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<DbUserModel> list = this.dbUserService.selectByIpAndUsername(dbUserModel);
		if(list.size()>0) {
			map.put("valid", false);
		} else {
			map.put("valid", true);
		}
		return map;
	}
	
}
