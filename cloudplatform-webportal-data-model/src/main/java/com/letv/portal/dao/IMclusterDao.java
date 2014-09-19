package com.letv.portal.dao;

import java.util.List;

import com.letv.portal.model.DbModel;
import com.letv.portal.model.MclusterModel;


/**Program Name: IMclusterDao <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IMclusterDao extends IBaseDao<MclusterModel> {

	/**Methods Name: audit <br>
	 * Description: 改变status状态<br>
	 * @author name: liuhao1
	 * @param mclusterModel
	 */
	void audit(MclusterModel mclusterModel);

	/**Methods Name: selectByClusterName <br>
	 * Description: 根据mcluster名查询数据：用于验证mcluster名是否重复<br>
	 * @author name: liuhao1
	 * @param mclusterName
	 * @return
	 */
	List<DbModel> selectByClusterName(String mclusterName);
	
}
