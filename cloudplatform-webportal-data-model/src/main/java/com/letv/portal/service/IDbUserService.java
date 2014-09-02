package com.letv.portal.service;

import java.util.List;

import com.letv.portal.model.DbUserModel;

/**Program Name: IDbUserService <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月1日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IDbUserService extends IBaseService<DbUserModel> {
	
	public List<DbUserModel> selectByDbId(String dbId);
}
