package com.letv.portal.service;

import java.util.Map;

import com.letv.common.paging.impl.Page;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.MclusterModel;

/**Program Name: IMclusterService <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IMclusterService extends IBaseService<MclusterModel> {
	
	/**Methods Name: findPagebyParams <br>
	 * Description: 根据查询条件查出分页数据<br>
	 * @author name: liuhao1
	 * @param params
	 * @param page
	 * @return
	 */
	public Page findPagebyParams(Map<String,Object> params,Page page);
	
	
	
	/**Methods Name: build <br>
	 * Description: 创建mcluster<br>
	 * @author name: liuhao1
	 * @param mclusterId
	 * @return
	 */
	public String build(String mclusterId);
	

	/**Methods Name: insert <br>
	 * Description: 创建完整mcluster<br>
	 * @author name: liuhao1
	 * @param mclusterId
	 * @param hostIds
	 * @param dbName
	 * @param createUser
	 * @return
	 */
	public String insert(String mclusterId,String[] hostIds,String dbName,String createUser);



	/**Methods Name: buildNotice <br>
	 * Description: 手动创建 通知<br>
	 * @author name: liuhao1
	 * @param clusterId
	 * @param flag
	 */
	public void buildNotice(String clusterId,String flag);
	
}