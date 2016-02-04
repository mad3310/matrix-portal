package com.letv.portal.fixedPush;

import java.util.List;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.fixed.FixedPushModel;


/**Program Name: IFixedPushService <br>
 * Description:  与固资系统交互实现<br>
 * @author name: wujun <br>
 * Written Date: 2014年10月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */

public interface IFixedPushService {
	/**
	 * Methods Name: createMutilContainerPushFixedInfo <br>
	 * Description: 备案多个container<br>
	 * @author name: wujun
	 * @param containers
	 */
	Boolean createMutilContainerPushFixedInfo(List<ContainerModel> containers);
	Boolean deleteMutilContainerPushFixedInfo(List<ContainerModel> containers);

	/**
	 *固资推送接口
	 * @param serverTag 该物理机ip
	 * @param name 该container名称
	 * @param ip 该contaienr ip
	 * @param type add or delete
	 * @return  true,如果推送失败 ,发送邮件,运维手动推送.
	 */
	Boolean sendFixedInfo(String serverTag,String name,String ip,String type);
}
