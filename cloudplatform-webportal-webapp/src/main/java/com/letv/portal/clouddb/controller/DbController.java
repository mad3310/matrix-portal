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
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbApplyStandardService;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IDbUserService;
import com.letv.portal.service.IMclusterService;

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
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String toList(HttpServletRequest request,HttpServletResponse response){
		return "/clouddb/user_db_list";
	}
	
	/**Methods Name: list <br>
	 * Description: http://localhost:8080/db/list/${currentPage}/${recordsPerPage}/${dbName}<br>
	 * @author name: liuhao1
	 * @param currentPage
	 * @param recordsPerPage
	 * @param dbName
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/list/{currentPage}/{recordsPerPage}/{dbName}", method=RequestMethod.GET)   
	public @ResponseBody ResultObject list(@PathVariable int currentPage,@PathVariable int recordsPerPage,@PathVariable String dbName,HttpServletRequest request) {
		Page page = new Page();
		page.setCurrentPage(currentPage);
		page.setRecordsPerPage(recordsPerPage);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbName", dbName);
		params.put("createUser", request.getSession().getAttribute("userId"));
		
		ResultObject obj = new ResultObject();
		obj.setData(this.dbService.findPagebyParams(params, page));
		return obj;
	}
	
	/**Methods Name: save <br>
	 * Description: 保存创建信息  http://localhost:8080/db/save<br>
	 * @author name: liuhao1
	 * @param dbApplyStandardModel
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)   
	public String save(DbApplyStandardModel dbApplyStandardModel, HttpServletRequest request) {
		
		if(StringUtils.isEmpty(dbApplyStandardModel.getId())) {
			dbApplyStandardModel.setCreateUser((String)request.getSession().getAttribute("userId"));
			dbApplyStandardModel.setStatus("1");
			this.dbApplyStandardService.insert(dbApplyStandardModel);
		} else {
			this.dbApplyStandardService.updateBySelective(dbApplyStandardModel);
		}
		return "redirect:/db/list";
	}
	
	/**Methods Name: detail <br>
	 * Description: 根据id获取db信息 http://localhost:8080/db/detail/{dbId}<br>
	 * @author name: liuhao1
	 * @param dbId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/detail/{dbId}",method=RequestMethod.GET) //http://localhost:8080/db/detail/{dbId}
	public ModelAndView detail(@PathVariable String dbId,HttpServletRequest request) {
		DbModel dbModel = this.dbService.selectById(dbId);
		ModelAndView mav = new ModelAndView();
		mav.addObject("containers", this.containerService.selectByClusterId(dbModel.getClusterId()));
		mav.addObject("dbUsers", this.dbUserService.selectByDbId(dbId));
		mav.addObject("dbApplyStandard", this.dbApplyStandardService.selectByDbId(dbId));
		mav.addObject("db", dbModel);
		mav.setViewName("/clouddb/user_db_detail");
		return mav;
	}
	@RequestMapping(value="/validate",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> validate(String applyCode,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<DbModel> list = this.dbService.selectByDbName(applyCode);
		if(list.size()>0) {
			map.put("valid", false);
		} else {
			map.put("valid", true);
		}
		return map;
	}
	
}
