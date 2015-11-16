package com.letv.portal.dao;

import java.util.List;
import java.util.Map;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.HclusterModel;

/**Program Name: IHostDao <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IHclusterDao extends IBaseDao<HclusterModel> {
	public List<Map<String, String>> selectByHclusterId(Long hclusterId);
	public List<HclusterModel> selectByName(Map<String,String> map);
	public List<HclusterModel> isExistHostOnHcluster(Map<String,Object> map);
	public HclusterModel selectHclusterById(Long hclusterId);
	public List<HclusterModel> selectHclusterByStatus(HclusterModel hclusterModel);
	public void updateStatus(HclusterModel hclusterModel);
	public Integer selectCountByType(String _parameter);
}
