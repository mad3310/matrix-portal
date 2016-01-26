package com.letv.portal.controller.clouddb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.functors.TruePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.letv.portal.model.adminoplog.AoLogType;
import com.letv.portal.proxy.IDbProxy;
import com.letv.portal.proxy.IMclusterProxy;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IMclusterService;
import com.letv.portal.service.adminoplog.AoLog;
import com.letv.portal.service.adminoplog.ClassAoLog;
import com.letv.portal.view.DbAuditView;
import com.mysql.jdbc.StringUtils;

/**Program Name: DbController <br>
 * Description:  db数据库的相关操作<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月9日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@ClassAoLog(module="RDS管理/数据库管理")
@Controller
@RequestMapping("/db")
public class DbController {
	
	@Autowired
	private IDbProxy dbProxy;
	@Autowired
	private IDbService dbService;
	@Autowired
	private IMclusterService mclusterService;
	
	private final static Logger logger = LoggerFactory.getLogger(DbController.class);

	
	/**Methods Name: list <br>
	 * Description: 查询db列表<br>
	 * @author name: liuhao1
	 * @param currentPage
	 * @param recordsPerPage
	 * @param dbName
	 */
	@RequestMapping(value="/{currentPage}/{recordsPerPage}/{dbName}",method=RequestMethod.GET)  
	public @ResponseBody ResultObject oldList(@PathVariable int currentPage,@PathVariable int recordsPerPage,@PathVariable String dbName,ResultObject obj) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbName", StringUtil.transSqlCharacter(dbName));		
		obj.setData(this.dbProxy.selectPageByParams(currentPage,recordsPerPage,params));
		return obj;
	}
	
	/**Methods Name: list <br>
	 * Description: 查询db列表<br>
	 * @author name: yaokuo
	 * @param request
	 */
	@RequestMapping(method=RequestMethod.GET)   
	public @ResponseBody ResultObject list(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		obj.setData(this.dbService.selectPageByParams(page, params));
		return obj;
	}
	
	/**Methods Name: detail <br>
	 * Description: 查看Db详情<br>
	 * @author name: liuhao1
	 * @param dbId
	 * @return
	 */
	@RequestMapping(value="/{dbId}",method=RequestMethod.GET)
	public @ResponseBody ResultObject detail(@PathVariable Long dbId){
		ResultObject obj = new ResultObject();	
		obj.setData(this.dbService.dbList(dbId));
		return obj;
	}
	
	/**Methods Name: save <br>
	 * Description:审批db
	 * @author name: liuhao1
	 * @return
	 */
	@AoLog(desc="审批db",type=AoLogType.UPDATE)
	@RequestMapping(value="/audit",method=RequestMethod.POST)   
	public @ResponseBody ResultObject save(DbAuditView dbAuditView) {
		if(!StringUtils.isNullOrEmpty(dbAuditView.getMclusterName())) {
			Boolean isExist= this.mclusterService.isExistByName(dbAuditView.getMclusterName());
			if(!isExist) {
				throw new ValidateException("集群名称已存在");
			}
		}
		ResultObject obj = new ResultObject();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbId", dbAuditView.getDbId());
		params.put("mclusterId", dbAuditView.getMclusterId());
		params.put("auditInfo", dbAuditView.getAuditInfo());
		params.put("status", dbAuditView.getAuditType());
		params.put("mclusterName", dbAuditView.getMclusterName());
		
		this.dbProxy.auditAndBuild(params);		
		return obj;
		
	}

}
