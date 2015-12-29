package com.letv.portal.controller.clouddb;

import java.util.HashMap;
import java.util.List;
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

import com.letv.common.exception.ValidateException;
import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.common.util.StringUtil;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.model.adminoplog.AoLogType;
import com.letv.portal.proxy.IDbUserProxy;
import com.letv.portal.python.service.IBuildTaskService;
import com.letv.portal.service.IDbUserService;
import com.letv.portal.service.adminoplog.AoLog;
import com.letv.portal.service.adminoplog.ClassAoLog;
import com.mysql.jdbc.StringUtils;

/**Program Name: DbUserController <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@ClassAoLog(module="RDS管理/数据库管理/数据库用户列表")
@Controller
@RequestMapping("/dbUser")
public class DbUserController {
	
	@Resource
	private IDbUserService dbUserService;
	@Resource
	private IDbUserProxy dbUserProxy;

	private final static Logger logger = LoggerFactory.getLogger(DbUserController.class);

	/**Methods Name: list <br>
	 * Description:  list<br>
	 * @author name: howie
	 * @param page
	 * @param request
	 * @param obj
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)  
	public @ResponseBody ResultObject list(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		obj.setData( this.dbUserService.findPagebyParams(params, page));
		return obj;
	}
	
	/**Methods Name: detail <br>
	 * Description: detail<br>
	 * @author name: howie
	 * @param dbId
	 * @return
	 */
	@RequestMapping(value="/{dbId}", method=RequestMethod.GET)   
	public @ResponseBody ResultObject detail(@PathVariable Long dbId) {
		ResultObject obj = new ResultObject();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbId", dbId);
		
		obj.setData(this.dbUserService.selectByMap(params));
		return obj;
	}
	
	/**Methods Name: save <br>
	 * Description: 用户保存<br>
	 * @author name: liuhao1 20141226
	 * @param dbUserModel
	 * @return
	 */
	@AoLog(desc="创建db用户",type=AoLogType.INSERT)
	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody ResultObject save(DbUserModel dbUserModel,String types,String ips,ResultObject obj) {
		if(StringUtils.isNullOrEmpty(types) || StringUtils.isNullOrEmpty(ips)|| types.contains("undefined") || ips.contains("undefined")) {
			throw new ValidateException("参数不合法");
		}
		this.dbUserProxy.saveAndBuild(dbUserModel,ips,types);
		return obj;
	}
	@AoLog(desc="修改db用户密码",type=AoLogType.UPDATE)
	@RequestMapping(value="/security/{username}",method=RequestMethod.POST)
	public  @ResponseBody ResultObject updateSecurity(Long dbId,String username,String password,ResultObject obj) {
		if(dbId == null || StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password)) {
			throw new ValidateException("参数不能为空");
		}
		this.dbUserProxy.updateSecurity(dbId,username,password);
		return obj;
	}


	/**Methods Name: updateDbUser <br>
	 * Description: 用户账户更新<br>
	 * @author name: liuhao1
	 * @param dbUserModel
	 * @param types
	 * @param ips
	 * @param obj
	 * @return
	 */
	@AoLog(desc="修改db用户",type=AoLogType.UPDATE)
	@RequestMapping(value="/authority/{username}",method=RequestMethod.POST)
	public @ResponseBody ResultObject updateUserAuthority(DbUserModel dbUserModel,String types,String ips,ResultObject obj) {
		if(StringUtils.isNullOrEmpty(types) || StringUtils.isNullOrEmpty(ips) || types.contains("undefined") || ips.contains("undefined")) {
			throw new ValidateException("参数不合法");
		}
		this.dbUserProxy.updateUserAuthority(dbUserModel,ips,types);
		return obj;
	}
	/**Methods Name: updateDbUser <br>
	 * Description: 修改数据库用户描述<br>
	 * @author name: liuhao1
	 * @param dbUserModel
	 * @param obj
	 * @return
	 */
	@AoLog(desc="修改db用户",type=AoLogType.UPDATE)
	@RequestMapping(value="/descn/{username}",method=RequestMethod.POST)
	public @ResponseBody ResultObject updateUserDescn(DbUserModel dbUserModel,ResultObject obj) {
		if(StringUtils.isNullOrEmpty(dbUserModel.getUsername()) || dbUserModel.getDbId() == null || StringUtils.isNullOrEmpty(dbUserModel.getDescn())) {
			throw new ValidateException("参数不能为空");
		}
		this.dbUserService.updateDescnByUsername(dbUserModel);
		return obj;
	}
	/**
	 * Methods Name: validate <br>
	 * Description: 校验dbUser用户是否存在
	 * @author name: wujun
	 * @param dbUserModel
	 * @param request
	 * @return
	 */
	@AoLog(desc="校验db用户是否存在",type=AoLogType.VALIDATE,ignore = true)
	@RequestMapping(value="/validate",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> validate(DbUserModel dbUserModel,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<DbUserModel> list = this.dbUserService.selectByIpAndUsername(dbUserModel);
		map.put("valid", list.size()>0?false:true);
		return map;
	}
	/**
	 * Methods Name: deleteDbUserById <br>
	 * Description: 删除dbUser用户
	 * @author name: wujun
	 * @param dbUserId
	 * @param dbUserModel
	 * @return
	 */
	@AoLog(desc="删除db用户",type=AoLogType.DELETE)
	@RequestMapping(value="/{dbUserId}",method=RequestMethod.DELETE)
	public  @ResponseBody ResultObject deleteDbUserById(@PathVariable String dbUserId,DbUserModel dbUserModel) {
		ResultObject obj = new ResultObject();
		this.dbUserProxy.deleteDbUser(dbUserId);
		return obj;
	}
	@AoLog(desc="删除db用户",type=AoLogType.DELETE)
	@RequestMapping(value="/{dbId}/{username}",method=RequestMethod.DELETE)
	public  @ResponseBody ResultObject deleteDbUserById(@PathVariable Long dbId,@PathVariable String username,ResultObject obj) {
		if(null == dbId || StringUtils.isNullOrEmpty(username)) {
			throw new ValidateException("参数不能为空");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dbId", dbId);
		map.put("username", username);
		this.dbUserProxy.deleteAndBuild(dbId,username);
		return obj;
	}

}
