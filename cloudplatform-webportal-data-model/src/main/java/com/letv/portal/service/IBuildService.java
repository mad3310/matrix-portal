package com.letv.portal.service;

import java.util.List;

import com.letv.portal.model.BuildModel;




/**Program Name: IBuildService <br>
 * Description:  python api 创建mcluster及db等创建过程记录<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月12日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IBuildService extends IBaseService<BuildModel> {

	public List<BuildModel> selectByMclusterId(String mclusterId);

	public void initStatus(String mclusterId);
	
	public void updateStatusFail(BuildModel buildModel);

	public void deleteByCluster(String clsuterId);
}
