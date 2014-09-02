
package com.letv.portal.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IMclusterService;
import com.mysql.jdbc.StringUtils;

/**Program Name: MclusterAPI <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller
@RequestMapping("/mcluster")
public class MclusterAPI {
	@Resource
	private IMclusterService mclusterService;
	@Resource
	private IContainerService containerService;
	
	private final static Logger logger = LoggerFactory.getLogger(MclusterAPI.class);
	
	/**Methods Name: list <br>
	 * Description: 根据查询条件及分页信息获取分页数据<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")   //http://localhost:8080/api/mcluster/list?currentPage=1&recordsPage=2&mcluster=
	public ResultObject list(HttpServletRequest request) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		
		Page page = new Page();
		String currentPage = (String) params.get("currentPage");
		String recordsPerPage = (String) params.get("recordsPerPage");
		page.setCurrentPage(StringUtils.isNullOrEmpty(currentPage)?1:Integer.parseInt(currentPage));
		page.setRecordsPerPage(StringUtils.isNullOrEmpty(recordsPerPage)?10:Integer.parseInt(recordsPerPage));
	
		ResultObject obj = new ResultObject();
		obj.setData(this.mclusterService.findPagebyParams(params, page));
		return obj;
	}
	
	@RequestMapping("/save")   //http://localhost:8080/api/mcluster/save
	public ResultObject save(MclusterModel mclusterModel, HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		
		/*mclusterModel.setCreateUser(createUser);
		mclusterModel.setCreateTime(createTime);
		mclusterModel.setUpdateUser(updateUser);
		mclusterModel.setUpdateTime(updateTime);*/

		if(StringUtils.isNullOrEmpty(mclusterModel.getId())) {
			this.mclusterService.insert(mclusterModel);
		} else {
			this.mclusterService.updateBySelective(mclusterModel);
		}
		return obj;
	}
	
	@RequestMapping("/getById") //http://localhost:8080/api/mcluster/getById
	public ResultObject getInfoById(String clusterId,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		
		List<ContainerModel> containers = this.containerService.selectByClusterId(clusterId);
		obj.setData(containers);
		return obj;
	}
	
	
	
	
	
}
