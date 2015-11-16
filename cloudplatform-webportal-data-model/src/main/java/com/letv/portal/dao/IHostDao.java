package com.letv.portal.dao;

import java.util.List;
import java.util.Map;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.HostModel;

/**Program Name: IHostDao <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IHostDao extends IBaseDao<HostModel> {
  
	public void updateNodesNumber(HostModel host);
	public List<HostModel> selectByHclusterId(Long hclusterId);
	public List<HostModel> selectByNameOrIp(Map<String,String> map);
	public HostModel selectByIp(String hostIp);
	public List<HostModel> isExistContainerOnHost(HostModel hostModel);
	public Integer selectCountByHclusterType(String _parameter);
}