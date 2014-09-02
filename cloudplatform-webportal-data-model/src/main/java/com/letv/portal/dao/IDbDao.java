package com.letv.portal.dao;

import com.letv.portal.model.DbModel;


/**Program Name: IDbDao <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IDbDao extends IBaseDao<DbModel> {
	
	/**Methods Name: audit <br>
	 * Description: 审核<br>
	 * @author name: liuhao1
	 * @param dbModel
	 */
	public void audit(DbModel dbModel);
}
