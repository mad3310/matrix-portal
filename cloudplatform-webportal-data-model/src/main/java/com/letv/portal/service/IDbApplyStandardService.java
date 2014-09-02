package com.letv.portal.service;

import java.util.Map;

import com.letv.common.paging.impl.Page;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;



/**Program Name: IDbApplyStandardService <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月20日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IDbApplyStandardService extends IBaseService<DbApplyStandardModel> {
	
	/**Methods Name: findPagebyParams <br>
	 * Description: 根据查询条件查出分页数据<br>
	 * @author name: liuhao1
	 * @param params
	 * @param page
	 * @return
	 */
	public Page findPagebyParams(Map<String,Object> params,Page page);
	
	/**Methods Name: selectByDbId <br>
	 * Description: 根据所属Db查出申请信息<br>
	 * @author name: liuhao1
	 * @param belongDb
	 * @return
	 */
	public DbApplyStandardModel selectByDbId(String belongDb);
}
