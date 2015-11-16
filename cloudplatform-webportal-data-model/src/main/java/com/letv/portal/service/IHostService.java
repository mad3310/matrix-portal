package com.letv.portal.service;

import java.util.List;
import java.util.Map;

import com.letv.common.paging.impl.Page;
import com.letv.portal.model.HostModel;



/**Program Name: IHostService <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月27日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IHostService extends IBaseService<HostModel> {

	/**Methods Name: updateNodeCount <br>
	 * Description: 改变node数量<br>
	 * @author name: liuhao1
	 * @param hostId
	 * @param type  +增加  -减少    
	 */
	public void updateNodeCount(Long hostId,String type); 
	
	/**Methods Name: findPagebyParams <br>
	 * Description: 根据查询条件查出分页数据<br>
	 * @author name: liuhao1
	 * @param params
	 * @param page
	 * @return
	 */
	public Page findPagebyParams(Map<String,Object> params,Page page);
	/**
	 * Methods Name: selectByHclusterId <br>
	 * Description: <br>
	 * @author name: wujun
	 * @param hclusterId
	 * @return
	 */
	public List<HostModel> selectByHclusterId(Long hclusterId);
	
	public List<HostModel> selectByIpOrHostName(HostModel hostModel);

	public HostModel selectByIp(String hostIp);
	/**
	 * Methods Name: isExitContainerOnHost <br>
	 * Description: host上是否存在container<br>
	 * @author name: wujun
	 * @param hostModel
	 * @return
	 */
	public List<HostModel> isExistContainerOnHost(HostModel hostModel);
	
	
	public HostModel getHostByHclusterId(Long hclusterId);

	Integer selectCountByHclusterType(String rds);
}
