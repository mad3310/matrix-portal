package com.letv.portal.clouddb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.portal.constant.Constant;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbApplyStandardService;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IDbUserService;
import com.letv.portal.service.IMclusterService;
import com.letv.portal.view.DbInfoView;
import com.mysql.jdbc.StringUtils;

/**Program Name: DbController <br>
 * Description:  db数据库的相关操作<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月9日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller
@RequestMapping("/db")
public class DbController {
	
	@Resource
	private IDbService dbService;
	@Resource
	private IContainerService containerService;
	@Resource
	private IMclusterService mclusterService;
	@Resource
	private IDbUserService dbUserService;
	@Resource
	private IDbApplyStandardService dbApplyStandardService;
	
	private final static Logger logger = LoggerFactory.getLogger(DbController.class);
	
	/*@RequestMapping(value="/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		return "/clouddb/user_db_list";
	}
	
	@RequestMapping(value="/mgrList")
	public String mgrList(HttpServletRequest request,HttpServletResponse response){
		return "/clouddb/mgr_db_list";
	}
	
	@RequestMapping(value="/toForm")
	public String toForm(HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("clusterId", request.getParameter("clusterId"));
		return "/clouddb/user_db_applyform";
	}	
	
	@RequestMapping("/list/data")   //http://localhost:8080/db/list/data
	public void listData(HttpServletRequest request,HttpServletResponse response) {
		Map<String,String> map = new HashMap<String,String>();
		String flag = request.getParameter("flag");
		if("self".equals(flag)){
			//取出session中用户id，追加到http rest请求中
			map.put("createUser", (String) request.getSession().getAttribute("userId"));
		}
				
		PrintWriter out;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(HttpUtil.getResultFromDBAPI(request,"/db/list",map));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@RequestMapping("/save")   //http://localhost:8080/mcluster/save
	public String save(HttpServletRequest request,HttpServletResponse response) {
		
		//取出session中用户id，追加到http rest请求中
		Map<String,String> map = new HashMap<String,String>();
		map.put("createUser", (String) request.getSession().getAttribute("userId"));
		map.put("status", "1");
		
		String result = HttpUtil.getResultFromDBAPI(request,"/dbApplyStandard/save",map);
		if(result.contains("\"result\":1")) {
			return "redirect:/db/list";
		}
		return "common/error";
	}
	
	@RequestMapping(value="/dbApplyInfo")
	public String dbApplyInfo(HttpServletRequest request,HttpServletResponse response){
		String result = HttpUtil.getResultFromDBAPI(request,"/db/getById",null);
		ObjectMapper resultMapper = new ObjectMapper();
		Map jsonResult = null;
		Map data = null;
		
		try {
			jsonResult = resultMapper.readValue(result, Map.class);
			data =  (Map) jsonResult.get("data");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("containers", data.get("containers"));
		request.setAttribute("dbUsers", data.get("dbUsers"));
		request.setAttribute("dbApplyStandard", data.get("dbApplyStandard"));
		request.setAttribute("db", data.get("dbModel"));

		return "/clouddb/user_db_apply_info";
	}
	
	@RequestMapping(value="/mgr/dbApplyInfo")
	public String mgrDbApplyInfo(HttpServletRequest request,HttpServletResponse response){
		String result = HttpUtil.getResultFromDBAPI(request,"/db/getById",null);
		ObjectMapper resultMapper = new ObjectMapper();
		Map jsonResult = null;
		Map data = null;
		
		try {
			jsonResult = resultMapper.readValue(result, Map.class);
			data =  (Map) jsonResult.get("data");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("containers", data.get("containers"));
		request.setAttribute("dbUsers", data.get("dbUsers"));
		request.setAttribute("dbApplyStandard", data.get("dbApplyStandard"));
		request.setAttribute("db", data.get("dbModel"));
		return "/clouddb/mgr_db_apply_info";
	}
	
	@RequestMapping(value="/toMgrAudit")
	public String toMgrAduit(HttpServletRequest request,HttpServletResponse response){
		String getHostResult = HttpUtil.getResultFromDBAPI(request,"/host/list",null);
		String getMclusterResult = HttpUtil.getResultFromDBAPI(request,"/mcluster/list",null);
		String dbApplyResult = HttpUtil.getResultFromDBAPI(request,"/db/getById",null);
		
		ObjectMapper hostMapper = new ObjectMapper();
		ObjectMapper mclusterMapper = new ObjectMapper();
		ObjectMapper dbApplyMapper = new ObjectMapper();
		Map hostMap = null;
		Map mclusterMap = null;
		Map dbApplyMap = null;
		try {
			hostMap = hostMapper.readValue(getHostResult, Map.class);
			mclusterMap = mclusterMapper.readValue(getMclusterResult, Map.class);
			dbApplyMap = dbApplyMapper.readValue(dbApplyResult, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("hostList", hostMap.get("data"));
		request.setAttribute("mclusterList", ((Map)mclusterMap.get("data")).get("data"));
		request.setAttribute("dbApplyStandard", ((Map)dbApplyMap.get("data")).get("dbApplyStandard"));
		return "/clouddb/mgr_audit_db";
	}

	@RequestMapping("/toMgrAudit/save")   //http://localhost:8080/mcluster/save
	public String toMgrAuditSave(HttpServletRequest request,HttpServletResponse response) {
		
		//取出session中用户id，追加到http rest请求中
		Map<String,String> map = new HashMap<String,String>();
		map.put("createUser", (String) request.getSession().getAttribute("userId"));
		map.put("status", "1");
		
		String result = HttpUtil.getResultFromDBAPI(request,"/db/audit/save",map);
		if(result.contains("\"result\":1")) {
			return "redirect:/db/mgrList";
		}
		return "common/error";
	}
	@RequestMapping("/createDb/onOldCluster/save")   //http://localhost:8080/mcluster/save
	public String createDbOnOldClusterSave(HttpServletRequest request,HttpServletResponse response) {
		
		//取出session中用户id，追加到http rest请求中
		Map<String,String> map = new HashMap<String,String>();
		map.put("createUser", (String) request.getSession().getAttribute("userId"));
		map.put("status", "1");
		
		String result = HttpUtil.getResultFromDBAPI(request,"/db/audit/save",map);
		if(result.contains("\"result\":1")) {
			return "redirect:/db/mgrList";
		}
		return "common/error";
	}
	
	
	
	
	*//**Methods Name: list <br>
	 * Description: 根据查询条件及分页信息获取分页数据<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/list", method=RequestMethod.POST)   //http://localhost:8080/api/db/list
	@ResponseBody
	public ResultObject list1(@RequestBody Map<String,Object> params,HttpServletRequest request) {
		Page page = new Page();
		String currentPage = (String) params.get("currentPage");
		String recordsPerPage = (String) params.get("recordsPerPage");
		page.setCurrentPage(StringUtils.isNullOrEmpty(currentPage)?1:Integer.parseInt(currentPage));
		page.setRecordsPerPage(StringUtils.isNullOrEmpty(recordsPerPage)?10:Integer.parseInt(recordsPerPage));
	
		ResultObject obj = new ResultObject();
		obj.setData(this.dbService.findPagebyParams(params, page));
		return obj;
	}
	
	*//**Methods Name: auditList <br>
	 * Description: 根据查询条件及分页信息获取分页数据:未审核数据<br>
	 * @author name: liuhao1
	 * @param params
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/audit/list", method=RequestMethod.POST)   //http://localhost:8080/api/db/audit/list
	@ResponseBody
	public ResultObject auditList(@RequestBody Map<String,Object> params,HttpServletRequest request) {
		params.put("status", "0");//未审核
		Page page = new Page();
		String currentPage = (String) params.get("currentPage");
		String recordsPerPage = (String) params.get("recordsPerPage");
		page.setCurrentPage(StringUtils.isNullOrEmpty(currentPage)?1:Integer.parseInt(currentPage));
		page.setRecordsPerPage(StringUtils.isNullOrEmpty(recordsPerPage)?10:Integer.parseInt(recordsPerPage));
		ResultObject obj = new ResultObject();
		obj.setData(this.dbService.findPagebyParams(params, page));
		return obj;
	}
	
	*//**Methods Name: save <br>
	 * Description: 保存db<br>
	 * @author name: liuhao1
	 * @param dbModel
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/save",method=RequestMethod.POST)   //http://localhost:8080/api/db/save
	@ResponseBody
	public ResultObject save(@RequestBody DbModel dbModel, HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		
		if(StringUtils.isNullOrEmpty(dbModel.getId())) {
			this.dbService.insert(dbModel);
		} else {
			this.dbService.updateBySelective(dbModel);
		}
		return obj;
	}
	
	*//**Methods Name: save <br>
	 * Description: 保存审批信息<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/audit/save",method=RequestMethod.POST)   //http://localhost:8080/api/db/audit/save
	@ResponseBody
	public ResultObject save(@RequestBody Map<String,Object> params,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
//		String auditType = request.getParameter("auditType");
//		String auditInfo = request.getParameter("auditInfo");
//		
//		String mclusterId = request.getParameter("mclusterId");
//		
//		String dbId = request.getParameter("dbId");
//		String dbName = request.getParameter("applyCode");
//		String dbApplyStandardId = request.getParameter("dbApplyStandardId");
//		
//		String hostIds = request.getParameter("hostIds");
//		String createUser = request.getParameter("createUser");
		
		String auditType = (String) params.get("auditType");
		String auditInfo = (String) params.get("auditInfo");
		String mclusterId = (String) params.get("mclusterId");
		String dbId = (String) params.get("dbId");
		String dbName = (String) params.get("applyCode");
		String dbApplyStandardId = (String) params.get("dbApplyStandardId");
		String hostIds = (String) params.get("hostIds");
		String createUser = (String) params.get("createUser");
		
		logger.debug("dbIds==>" + dbId);
		logger.debug("dbName==>" + dbName);
		logger.debug("mclusterId==>" + mclusterId);
		logger.debug("auditType==>" + auditType);
		logger.debug("auditInfo==>" + auditInfo);
		
		if(Constant.DB_AUDIT_STATUS_TRUE_BUILD_NEW_MCLUSTER.equals(auditType)) {
			logger.debug("hostIds==>" + hostIds.toString());
			//审核通过，已有mcluster创建db
			mclusterId = UUID.randomUUID().toString();
			this.mclusterService.insert(mclusterId,hostIds.split("\\|"),dbName,createUser);
		}
		//保存审批信息
		this.dbService.audit(dbId,dbApplyStandardId,auditType,mclusterId,auditInfo);
		
		//创建mcluster db
//		this.dbService.build(auditType,mclusterId,dbId,dbApplyStandardId);
		
		return obj;
	}
	
	@RequestMapping("/build")   //http://localhost:8080/api/db/build
	public ResultObject build(DbModel dbModel,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		
		this.dbService.build("0", null, null, null,"liuhao1@letv.com");
		
		return obj;
	}
	
	@RequestMapping("/build/notice/{buildFlag}/{dbId}")   //http://localhost:8080/api/db/build/notice/{success/fail}/{dbId}
	public ResultObject notice(@PathVariable String buildFlag,@PathVariable String dbId,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		this.dbService.buildNotice(dbId,buildFlag);
		return obj;
	}
	
	*//**Methods Name: getInfoById <br>
	 * Description: 根据id获取db信息<br>
	 * @author name: liuhao1
	 * @param dbId
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/getById/{dbId}",method=RequestMethod.GET) //http://localhost:8080/api/db/getById/{dbId}
	@ResponseBody
	public ResultObject getInfoById(@PathVariable String dbId,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		
		DbModel dbModel = this.dbService.selectById(dbId);
		List<ContainerModel> containers = this.containerService.selectByClusterId(dbModel.getClusterId());
		List<DbUserModel> dbUsers = this.dbUserService.selectByDbId(dbId);
		DbApplyStandardModel dbApplyStandard = this.dbApplyStandardService.selectByDbId(dbId);
		
		DbInfoView dbInfoView = new DbInfoView(dbModel,dbApplyStandard,dbUsers,containers);
		
		obj.setData(dbInfoView);
		return obj;
	}
	
	
	
	*//**Methods Name: list <br>
	 * Description: 根据查询条件及分页信息获取分页数据<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/list",method=RequestMethod.POST)   //http://localhost:8080/api/dbApplyStandard/list
	@ResponseBody
	public ResultObject list(@RequestBody Map<String,Object> params, HttpServletRequest request) {
		Page page = new Page();
		String currentPage = (String) params.get("currentPage");
		String recordsPerPage = (String) params.get("recordsPerPage");
		page.setCurrentPage(StringUtils.isNullOrEmpty(currentPage)?1:Integer.parseInt(currentPage));
		page.setRecordsPerPage(StringUtils.isNullOrEmpty(recordsPerPage)?10:Integer.parseInt(recordsPerPage));
	
		ResultObject obj = new ResultObject();
		obj.setData(this.dbApplyStandardService.findPagebyParams(params, page));
		return obj;
	}
	
	*//**Methods Name: save <br>
	 * Description: 保存申请信息<br>
	 * @author name: liuhao1
	 * @param dbApplyStandardModel
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/save",method=RequestMethod.POST)   //http://localhost:8080/api/dbApplyStandard/save
	public ResultObject save(@RequestBody DbApplyStandardModel dbApplyStandardModel, HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		if(StringUtils.isNullOrEmpty(dbApplyStandardModel.getId())) {
			this.dbApplyStandardService.insert(dbApplyStandardModel);
		} else {
			this.dbApplyStandardService.updateBySelective(dbApplyStandardModel);
		}
		return obj;
	}
	
	*//**Methods Name: selectByDbId <br>
	 * Description: 根据db获取申请信息<br>
	 * @author name: liuhao1
	 * @param belongDb
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/getByDbId/{belongDb}",method=RequestMethod.GET)
	@ResponseBody
	public ResultObject selectByDbId(@PathVariable String belongDb,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		obj.setData(this.dbApplyStandardService.selectByDbId(belongDb));
		return obj;
	}
	*/
}
