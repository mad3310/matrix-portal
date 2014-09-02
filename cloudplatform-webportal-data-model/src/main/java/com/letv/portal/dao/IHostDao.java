package com.letv.portal.dao;

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
}
