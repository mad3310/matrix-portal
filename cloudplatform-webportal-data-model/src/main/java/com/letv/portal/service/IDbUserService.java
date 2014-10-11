package com.letv.portal.service;

import java.util.List;
import java.util.Map;

import com.letv.common.paging.impl.Page;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.DbUserModel;

/**Program Name: IDbUserService <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月1日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IDbUserService extends IBaseService<DbUserModel> {
	
	/**Methods Name: selectByDbId <br>
	 * Description: 根据dbId查询dbUser列表<br>
	 * @author name: liuhao1
	 * @param dbId
	 * @return
	 */
	public List<DbUserModel> selectByDbId(String dbId);
	
	/**Methods Name: findPagebyParams <br>
	 * Description: dbUser列表<br>
	 * @author name: liuhao1
	 * @param params
	 * @param page
	 * @return
	 */
	public Page findPagebyParams(Map<String,Object> params,Page page);
	
	/**Methods Name: selectCreateParams <br>
	 * Description: 查询创建用户相关参数<br>
	 * @author name: liuhao1
	 * @param id
	 * @return
	 */
	public Map<String,String> selectCreateParams(String id);
	
	/**Methods Name: updateStatus <br>
	 * Description: 更新用户状态<br>
	 * @author name: liuhao1
	 * @param dbUserModel
	 */
	public void updateStatus(DbUserModel dbUserModel);

	/**Methods Name: updateStatus <br>
	 * Description: 根据数据库用户名和ip查询数据：用于验证数据库用户名和ip是否重复<br>
	 * @author name: yaokuo
	 * @param dbUserModel
	 */
	public List<DbUserModel> selectByIpAndUsername(DbUserModel dbUserModel);

}
