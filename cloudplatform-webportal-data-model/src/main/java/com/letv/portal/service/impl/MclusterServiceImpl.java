package com.letv.portal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.letv.common.dao.QueryParam;
import com.letv.common.paging.impl.Page;
import com.letv.common.util.ConfigUtil;
import com.letv.portal.constant.Constant;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IContainerDao;
import com.letv.portal.dao.IIpResourceDao;
import com.letv.portal.dao.IMclusterDao;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.IpResourceModel;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.model.Result;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.IMclusterService;

/**Program Name: MclusterServiceImpl <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月14日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Service("mclusterService")
public class MclusterServiceImpl extends BaseServiceImpl<MclusterModel> implements
		IMclusterService{
	
	@Resource
	private IMclusterDao mclusterDao;
	
	@Resource
	private IContainerDao containerDao;
	
	@Resource
	private IIpResourceDao ipResourceDao;
	@Resource
	private IHostService hostService;
	
	private static final String PYTHON_URL = "";
	private static final String SUCCESS_CODE = "";
	private static final String LETV_MCLUSTER_NAME_PREFIX = ConfigUtil.getString("letv_mcluster_name_prefix");		
	private static final String LETV_MCLUSTER_MOUNTDIRS_PREFIX = ConfigUtil.getString("letv_mcluster_mountDirs_prefix");		
	private static final String LETV_MCLUSTER_MOUNTDIRS_SUFFIX = ConfigUtil.getString("letv_mcluster_mountDirs_suffix");		
	private static final String LETV_MCLUSTER_NODENAME_PREFIX = ConfigUtil.getString("letv_mcluster_nodeName_prefix");		
	private static final String LETV_MCLUSTER_NODENAME_SUFFIX = ConfigUtil.getString("letv_mcluster_nodeName_suffix");

	public MclusterServiceImpl() {
		super(MclusterModel.class);
	}

	@Override
	public IBaseDao<MclusterModel> getDao() {
		return this.mclusterDao;
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.mclusterDao.selectPageByMap(param));
		page.setTotalRecords(this.mclusterDao.selectByMapCount(params));
		return page;
		
	}

	@Override
	public String build(String mclusterId) {
		
		MclusterModel mcluster = this.mclusterDao.selectById(mclusterId);
		//判断是否已创建
		if("0".equals(mcluster.getStatus())) {
			
			List<ContainerModel> containers = this.containerDao.selectByClusterId(mclusterId);
			String json=null;
			ObjectMapper mapper = null;
			RestTemplate  rest = new RestTemplate();
			try {
				mapper = new ObjectMapper();
				json = mapper.writeValueAsString(containers);// 把map或者是list转换成
				//创建
				String result = rest.postForObject("", null, String.class);
			
				Result r = mapper.readValue(result, Result.class);
				
				if(SUCCESS_CODE.equals(r.getCode())){
					//初始化
					result = rest.postForObject("", null, String.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String insert(String mclusterId,String[] hostIds, String dbName, String createUser) {
		String mclusterName = LETV_MCLUSTER_NAME_PREFIX + dbName;
		this.insert(new MclusterModel(mclusterId,mclusterName,Constant.STATUS_DEFAULT,createUser));
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", Constant.STATUS_DEFAULT);
		map.put("count", ConfigUtil.getint("mcluster_containers_count"));
		
		List<IpResourceModel> ips = this.ipResourceDao.selectByStatus(map);
		
		for (int i = 0; i < hostIds.length; i++) {
			ContainerModel t = new ContainerModel();
			t.setHostId(hostIds[i]);
			t.setContainerName(mclusterName);
			t.setMountDir(LETV_MCLUSTER_MOUNTDIRS_PREFIX + dbName + LETV_MCLUSTER_MOUNTDIRS_SUFFIX);
			
			if(i == 0) {
				t.setClusterNodeName(LETV_MCLUSTER_NODENAME_PREFIX + dbName + LETV_MCLUSTER_NODENAME_SUFFIX + Constant.MCLUSTER_NODE_TYPE_VIP);
				t.setZookeeperId("");
				t.setType(Constant.MCLUSTER_NODE_TYPE_VIP);
			} else {
				t.setClusterNodeName(LETV_MCLUSTER_NODENAME_PREFIX + dbName + LETV_MCLUSTER_NODENAME_SUFFIX + i);
				t.setZookeeperId(i + "");
				t.setType(Constant.MCLUSTER_NODE_TYPE_NORMAL);
			}
			t.setAssignName("");
			t.setOriginName("");
			
			t.setIpAddr(ips.get(i).getIp());
			t.setGateAddr(ips.get(i).getGateWay());
			t.setIpMask(ips.get(i).getMask());
			
			
			t.setClusterId(mclusterId);
			t.setCreateUser(createUser);
			this.containerDao.insert(t);
			this.hostService.updateNodeCount(hostIds[i],"+");
		}
		//改变使用状态
		for (IpResourceModel ipResourceModel : ips) {
			ipResourceModel.setStatus(Constant.IPRESOURCE_STATUS_USERD);
			this.ipResourceDao.updateStatus(ipResourceModel);
		}
		return null;
	}

	@Override
	public void buildNotice(String clusterId,String flag) {
		this.mclusterDao.audit(new MclusterModel(clusterId,flag));
	}
	
}
