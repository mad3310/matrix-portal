
package com.letv.portal.api;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.service.IDbApplyStandardService;
import com.letv.portal.service.IDbService;
import com.mysql.jdbc.StringUtils;

/**Program Name: DbApplyStandardAPI <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月20日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller
@RequestMapping("/dbApplyStandard")
public class DbApplyStandardAPI {
	@Resource
	private IDbApplyStandardService dbApplyStandardService;
	
	private final static Logger logger = LoggerFactory.getLogger(DbApplyStandardAPI.class);
	
	/**Methods Name: list <br>
	 * Description: 根据查询条件及分页信息获取分页数据<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")   //http://localhost:8080/api/dbApplyStandard/list?currentPage=1&recordsPage=2&mcluster=
	public ResultObject list(HttpServletRequest request) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		Page page = new Page();
		String currentPage = (String) params.get("currentPage");
		String recordsPerPage = (String) params.get("recordsPerPage");
		page.setCurrentPage(StringUtils.isNullOrEmpty(currentPage)?1:Integer.parseInt(currentPage));
		page.setRecordsPerPage(StringUtils.isNullOrEmpty(recordsPerPage)?10:Integer.parseInt(recordsPerPage));
	
		ResultObject obj = new ResultObject();
		obj.setData(this.dbApplyStandardService.findPagebyParams(params, page));
		return obj;
	}
	
	@RequestMapping("/save")   //http://localhost:8080/api/dbApplyStandard/save
	public ResultObject save(DbApplyStandardModel dbApplyStandardModel, HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		
		/*mclusterModel.setCreateUser(createUser);
		mclusterModel.setCreateTime(createTime);
		mclusterModel.setUpdateUser(updateUser);
		mclusterModel.setUpdateTime(updateTime);*/
		if(StringUtils.isNullOrEmpty(dbApplyStandardModel.getId())) {
			this.dbApplyStandardService.insert(dbApplyStandardModel);
		} else {
			this.dbApplyStandardService.updateBySelective(dbApplyStandardModel);
		}
		return obj;
	}
	
	@RequestMapping("/getByDbId")
	public ResultObject selectByDbId(String belongDb,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		obj.setData(this.dbApplyStandardService.selectByDbId(belongDb));
		return obj;
	}
	
	
	
}
