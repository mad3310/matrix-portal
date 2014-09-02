/*
 * @Title: DictionaryControl.java
 * @Package com.letv.mms.api
 * @Description: 字典信息API接口
 * @author 陈光
 * @date 2012-12-11 下午5:28:42
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-11                          
 */
package com.letv.portal.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.letv.common.exception.CommonException;
import com.letv.common.exception.ValidateException;
import com.letv.common.result.ResultObject;
import com.letv.portal.model.DictionaryModel;
import com.letv.portal.service.IDictionaryService;

@Controller
@RequestMapping("/dictionary")
public class DictionaryAPI {
	@Resource
	private IDictionaryService dictionaryProxy;
	/**
	 * 根据字典分类查询字典信息集合
	 * @param dicType
	 * @return
	 */
	@RequestMapping("/{dicType}")
	public ResultObject getDictionaryByDictype(@PathVariable int dicType) {
		
		if(dicType < 0)
			throw new ValidateException("字典信息分类:"+dicType+"无效!");
		ResultObject obj = new ResultObject(1);
		List<DictionaryModel> list = new ArrayList<DictionaryModel>();
		try {
			list = dictionaryProxy.getDicInfoListByDictype(dicType);
			obj.setData(list);
		} catch (CommonException e) {
			throw new CommonException("执行根据字典分类查询字典信息出错!",e);
		}
		return obj;
	}
	
	/**
	 * 根据字典分类查询字典信息集合
	 * @param dicType
	 * @return
	 */
	@RequestMapping("/getById/{dictId}")   //http://localhost:8080/api/dictionary/getById/{id}
	public ResultObject getDictionaryById(@PathVariable String dictId) {
		
		if(null == dictId){
			throw new ValidateException("字典信息分类:"+dictId+"无效!");
		}
		ResultObject obj = new ResultObject(1);
		List<DictionaryModel> list = new ArrayList<DictionaryModel>();
		try {
			 obj.setData(dictionaryProxy.getDataFromDbByKey(dictId));
		} catch (CommonException e) {
			throw new CommonException("执行根据字典分类查询字典信息出错!",e);
		}
		System.out.println("b");
		return obj;
	}
}
