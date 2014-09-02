package com.letv.portal.service;

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
	public void updateNodeCount(String hostId,String type); 
}
