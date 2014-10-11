package com.letv.portal.dao;

import java.util.List;
import java.util.Map;

import com.letv.portal.model.IpResourceModel;


/**Program Name: IIPResourceDao <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月29日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IIpResourceDao extends IBaseDao<IpResourceModel> {
	
	/**Methods Name: selectByStatus <br>
	 * Description: 查出count个未使用的ip<br>
	 * @author name: liuhao1
	 * @param Map (status, count)
	 * @return
	 */
	public List<IpResourceModel> selectByStatus(Map<String,Object> map);

	/**Methods Name: updateStatus <br>
	 * Description: 修改使用状态<br>
	 * @author name: liuhao1
	 * @param ipResourceModel
	 */
	public void updateStatus(IpResourceModel ipResourceModel);
	
}
