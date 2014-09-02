package com.letv.portal.dao;

import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;

/**Program Name: IDbApplyStandardDao <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>s
 * Modified Date: <br>
 */
public interface IDbApplyStandardDao extends IBaseDao<DbApplyStandardModel> {
	
	/**Methods Name: selectByDbId <br>
	 * Description: 根据所属Db查出申请信息<br>
	 * @author name: liuhao1
	 * @param belongDb
	 * @return
	 */
	public DbApplyStandardModel selectByDbId(String belongDb);
	
	
	/**Methods Name: audit <br>
	 * Description: 审核<br>
	 * @author name: liuhao1
	 * @param dbApplyStandardModel
	 */
	public void audit(DbApplyStandardModel dbApplyStandardModel);
}
