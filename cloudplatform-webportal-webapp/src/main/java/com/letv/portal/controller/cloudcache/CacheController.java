package com.letv.portal.controller.cloudcache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.exception.ValidateException;
import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.HttpUtil;
import com.letv.common.util.StringUtil;
import com.letv.portal.model.cbase.CbaseBucketModel;
import com.letv.portal.proxy.ICbaseProxy;
import com.letv.portal.service.cbase.ICbaseBucketService;

@Controller
@RequestMapping("/cache")
public class CacheController {

	@Autowired(required = false)
	private SessionServiceImpl sessionService;
	@Autowired
	private ICbaseBucketService cbaseBucketService;
	@Autowired
	private ICbaseProxy cbaseProxy;

	private final static Logger logger = LoggerFactory
			.getLogger(CacheController.class);

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResultObject list(Page page,
			HttpServletRequest request, ResultObject result) {
		Map<String, Object> params = HttpUtil.requestParam2Map(request);
		params.put("createUser", sessionService.getSession().getUserId());
		String bucketName = (String) params.get("bucketName");
		if (!StringUtils.isEmpty(bucketName))
			params.put("bucketName", StringUtil.transSqlCharacter(bucketName));
		result.setData(this.cbaseBucketService.selectPageByParams(page, params));
		return result;
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody ResultObject save(CbaseBucketModel cbaseBucketModel,
			ResultObject obj) {
		if (cbaseBucketModel == null
				|| StringUtils.isEmpty(cbaseBucketModel.getBucketName())
				|| cbaseBucketModel.getBucketName().length() < 4
				|| cbaseBucketModel.getBucketName().length() > 16
				|| Integer.valueOf(cbaseBucketModel.getRamQuotaMB()) < 1 * 1024
				|| Integer.valueOf(cbaseBucketModel.getRamQuotaMB()) > 30 * 1024
				|| cbaseBucketModel.getHclusterId() == null
				|| cbaseBucketModel.getBucketType() == null) {
			throw new ValidateException("参数不合法");
		}
		cbaseBucketModel.setCreateUser(this.sessionService.getSession()
				.getUserId());
		this.cbaseProxy.saveAndBuild(cbaseBucketModel);
		return obj;
	}

	@RequestMapping(value = "/{cacheId}", method = RequestMethod.GET)
	public @ResponseBody ResultObject detail(@PathVariable Long cacheId) {
		isAuthorityCache(cacheId);
		ResultObject obj = new ResultObject();
		CbaseBucketModel bucket = this.cbaseBucketService.bucketList(cacheId);
		obj.setData(bucket);
		return obj;
	}

	@RequestMapping(value = "/moxiConfig/{cacheId}", method = RequestMethod.GET)
	public @ResponseBody ResultObject getMoxiConfig(@PathVariable Long cacheId) {
		isAuthorityCache(cacheId);
		ResultObject obj = new ResultObject();
		Map<String, Object> config = this.cbaseBucketService
				.getMoxiConfig(cacheId);
		obj.setData(config);
		return obj;
	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> validate(String bucketName,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(bucketName))
			throw new ValidateException("参数不合法");
		List<CbaseBucketModel> list = this.cbaseBucketService
				.selectByBucketNameForValidate(bucketName, sessionService
						.getSession().getUserId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("valid", list.size() > 0 ? false : true);
		return map;
	}

	private void isAuthorityCache(Long cacheId) {
		if (cacheId == null)
			throw new ValidateException("参数不合法");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", cacheId);
		map.put("createUser", sessionService.getSession().getUserId());
		List<CbaseBucketModel> cbases = this.cbaseBucketService
				.selectByMap(map);
		if (cbases == null || cbases.isEmpty())
			throw new ValidateException("参数不合法");
	}

}
