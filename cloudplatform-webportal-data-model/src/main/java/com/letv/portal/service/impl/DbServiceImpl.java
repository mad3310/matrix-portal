package com.letv.portal.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.letv.common.dao.QueryParam;
import com.letv.common.email.SimpleTextEmailSender;
import com.letv.common.paging.impl.Page;
import com.letv.common.util.ConfigUtil;
import com.letv.portal.constant.Constant;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IContainerDao;
import com.letv.portal.dao.IDbApplyStandardDao;
import com.letv.portal.dao.IDbDao;
import com.letv.portal.dao.IIpResourceDao;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.IpResourceModel;
import com.letv.portal.model.Result;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IMclusterService;

@Service("dbService")
public class DbServiceImpl extends BaseServiceImpl<DbModel> implements
		IDbService{
	
	private final static Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);
	
	private static final String PYTHON_URL = "";
	private static final String SUCCESS_CODE = "";
	private static final String DB_CREATE_TYPE = ConfigUtil.getString("db.create.type");
	
	@Resource
	private IDbDao dbDao;
	@Resource
	private IDbApplyStandardDao dbApplyStandardDao;
	
	@Resource
	private IContainerDao containerDao;
	
	@Resource
	private IMclusterService mclusterService;
	
	@Resource
	private IIpResourceDao ipResourceDao;
	@Resource
	private SimpleTextEmailSender simpleTextEmailSender;

	public DbServiceImpl() {
		super(DbModel.class);
	}

	@Override
	public IBaseDao<DbModel> getDao() {
		return this.dbDao;
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.dbDao.selectPageByMap(param));
		page.setTotalRecords(this.dbDao.selectByMapCount(params));
		return page;
		
	}

	/*@Override
	public void audit(String dbId, String dbApplyStandardId,List<ContainerModel> containers) {
		
		this.dbDao.audit(new DbModel(dbId,"1")); //审核成功
		this.dbApplyStandardDao.audit(new DbApplyStandardModel(dbApplyStandardId,"1"));
		
		//保存container信息	
		for (ContainerModel containerModel : containers) {
			this.containerDao.insert(containerModel);
		}
	}*/
	
	@Override
	public void audit(String dbId,String dbApplyStandardId,String status,String mclusterId,String auditInfo) {
		this.dbDao.audit(new DbModel(dbId,status,mclusterId));
		this.dbApplyStandardDao.audit(new DbApplyStandardModel(dbApplyStandardId,status,auditInfo));
	}

	@Override
	public String build(DbModel dbModel) {
		
		//判断手动创建 还是调用API创建
		
		
		//判断是否已创建mcluster
			//创建mcluster
			//初始化mcluster
		this.mclusterService.build(dbModel.getClusterId());
			
		
		//创建db
		//创建db用户
		this.buildDb(dbModel);
		return null;
	}
	@Override
	public void build(String auditType, String mclusterId, String dbId,
			String dbApplyStandardId,String auditUser) {
		if(DB_CREATE_TYPE.equals(auditType)) {
			String content = " mcluster创建信息：           \r\n db创建信息：             \r\n 创建成功后，点击链接通知webportal系统 http://localhost:8080/build/notice/success/" + dbId;
			//手动创建 发送邮件
			this.simpleTextEmailSender.send("WebPortal管理系统-系统通知", auditUser, content, auditUser);
			
		} else if("1".equals(DB_CREATE_TYPE)) {
			
		}
		
	}
	
	private String buildDb(DbModel dbModel) {
		
		String json=null;
		ObjectMapper mapper = null;
		RestTemplate  rest = new RestTemplate();
		try {
			mapper = new ObjectMapper();
			json = mapper.writeValueAsString(dbModel);// 把map或者是list转换成
			//创建db
			String result = rest.postForObject("", null, String.class);
		
			Result r = mapper.readValue(result, Result.class);
			
			if(SUCCESS_CODE.equals(r.getCode())){
				//创建db用户
				result = rest.postForObject("", null, String.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void buildNotice(String dbId,String buildFlag) {
		buildFlag = "success".equals(buildFlag)?Constant.DB_AUDIT_STATUS_BUILD_SUCCESS:Constant.DB_AUDIT_STATUS_BUILD_FAIL;
		DbModel dbModel = this.selectById(dbId);
		if(Constant.DB_AUDIT_STATUS_TRUE_BUILD_NEW_MCLUSTER.equals(dbModel.getStatus())) {
			this.mclusterService.buildNotice(dbModel.getClusterId(),Constant.DB_AUDIT_STATUS_BUILD_SUCCESS);
		}
		this.dbDao.audit(new DbModel(dbId,Constant.DB_AUDIT_STATUS_BUILD_SUCCESS));
	}

}
