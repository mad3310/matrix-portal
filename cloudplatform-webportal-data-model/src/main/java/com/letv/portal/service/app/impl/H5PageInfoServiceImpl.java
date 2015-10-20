package com.letv.portal.service.app.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.app.IH5PageInfoDao;
import com.letv.portal.model.app.H5PageInfoModel;
import com.letv.portal.service.app.IH5PageInfoService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("h5PageInfoService")
public class H5PageInfoServiceImpl extends BaseServiceImpl<H5PageInfoModel> implements IH5PageInfoService{
	
	private final static Logger logger = LoggerFactory.getLogger(H5PageInfoServiceImpl.class);
	
	@Resource
	private IH5PageInfoDao h5PageInfoDao;
	
	public H5PageInfoServiceImpl() {
		super(H5PageInfoModel.class);
	}

	@Override
	public IBaseDao<H5PageInfoModel> getDao() {
		return this.h5PageInfoDao;
	}
	
	public Map<String, Object> getH5PageInfos() {
		Map<String, Object> rets = new HashMap<String, Object>();
		List<Object> lists = new ArrayList<Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("descn", "service");
		List<H5PageInfoModel> pages = super.selectByMap(params);
		StringBuffer buffer = new StringBuffer();
		for (H5PageInfoModel h5PageInfoModel : pages) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", buffer.append("matrix://").append(h5PageInfoModel.getType()).append("::").append(h5PageInfoModel.getPageId()).append("::").
					append(h5PageInfoModel.getWebUrl()).append("::").append(h5PageInfoModel.getTitle()).append("::").append(h5PageInfoModel.getIconUrl()).toString());
			lists.add(map);
			buffer.setLength(0);
		}
		rets.put("data", lists);
		return rets;
	}

	@Override
	public Map<String, String> getH5Urls() {
		List<H5PageInfoModel> pages = selectByMap(null);
		Map<String, String> h5Urls = new HashMap<String, String>();
		StringBuffer buffer = new StringBuffer();
		for (H5PageInfoModel h5PageInfoModel : pages) {
			if("10001".equals(h5PageInfoModel.getPageId())) {//cluster-h5页面
				buffer.setLength(0);
				h5Urls.put("clusterUrl", buffer.append("matrix://").append(h5PageInfoModel.getType()).append("::").append(h5PageInfoModel.getPageId()).append("::").
						append(h5PageInfoModel.getWebUrl()).append("::").append(h5PageInfoModel.getTitle()).append("::").append(h5PageInfoModel.getIconUrl()).toString());
			} else if("10002".equals(h5PageInfoModel.getPageId())) {//node-h5页面
				buffer.setLength(0);
				h5Urls.put("nodeUrl", buffer.append("matrix://").append(h5PageInfoModel.getType()).append("::").append(h5PageInfoModel.getPageId()).append("::").
						append(h5PageInfoModel.getWebUrl()).append("::").append(h5PageInfoModel.getTitle()).append("::").append(h5PageInfoModel.getIconUrl()).toString());
			} else if("10003".equals(h5PageInfoModel.getPageId())) {//db-h5页面
				buffer.setLength(0);
				h5Urls.put("dbUrl", buffer.append("matrix://").append(h5PageInfoModel.getType()).append("::").append(h5PageInfoModel.getPageId()).append("::").
						append(h5PageInfoModel.getWebUrl()).append("::").append(h5PageInfoModel.getTitle()).append("::").append(h5PageInfoModel.getIconUrl()).toString());
			} else if("10004".equals(h5PageInfoModel.getPageId())) {//待审核db-h5页面
				buffer.setLength(0);
				h5Urls.put("pendingAuditDbUrl", buffer.append("matrix://").append(h5PageInfoModel.getType()).append("::").append(h5PageInfoModel.getPageId()).append("::").
						append(h5PageInfoModel.getWebUrl()).append("{id}").append("::").append(h5PageInfoModel.getTitle()).append("::").append(h5PageInfoModel.getIconUrl()).toString());
			} else if("10000".equals(h5PageInfoModel.getPageId())) {//任务流异常-h5页面（暂未实现）
				buffer.setLength(0);
				h5Urls.put("taskErrorUrl", buffer.append("matrix://").append(h5PageInfoModel.getType()).append("::").append(h5PageInfoModel.getPageId()).append("::").
						append(h5PageInfoModel.getWebUrl()).append("::").append(h5PageInfoModel.getTitle()).append("::").append(h5PageInfoModel.getIconUrl()).toString());
			}
		}
		return h5Urls;
	}
}
