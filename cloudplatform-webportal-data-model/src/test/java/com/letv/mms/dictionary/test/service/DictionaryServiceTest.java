/*
 * @Title: DictionaryServiceTest.java
 * @Package com.letv.mms.dictionary.test.service
 * @Description:字典service相关测试类
 * @author 陈光 
 * @date 2012-12-14 上午11:03:41
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-14                          
 */
package com.letv.mms.dictionary.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.util.Assert;

import com.letv.mms.base.AbstractTestCase;
import com.letv.portal.model.DictionaryModel;
import com.letv.portal.service.IDictionaryService;


public class DictionaryServiceTest extends AbstractTestCase {
	@Resource
	private IDictionaryService dictionaryProxy;
	@Test
	public void testSelectById()
	{
		DictionaryModel model=null;
		model=dictionaryProxy.selectById("100001");
		Assert.notNull(model);
	}
	@Test
	public void testGetDicInfoListByDictype()
	{
		List <DictionaryModel> list = dictionaryProxy.getDicInfoListByDictype(1);
		System.out.println(list);
//		List <DictionaryModel> list  = service.getDicInfoListByDictype(1000000);
//		System.out.println(list);
	}
	@Test
	public void testSelectByMap()
	{
		Map<String,Object> map1 =new HashMap<String,Object>();
		map1.put("value", "武侠");
		List <DictionaryModel> list = dictionaryProxy.selectByMap(map1);
//		Assert.notEmpty(list);
		System.out.println(list);
		Map<String,Object> map2 =new HashMap<String,Object>();
		map2.put("valueId", 3186);
		List <DictionaryModel> list2 = dictionaryProxy.selectByMap(map2);
//		Assert.isTrue(list2.size()==1);
		System.out.println(list2);
		 
	}
	@Test
	public void testSelectByMapCount()
	{
		Map<String,Object> map1 =new HashMap<String,Object>();
		map1.put("value", "武侠");
		Integer count = dictionaryProxy.selectByMapCount(map1);
		System.out.println(count);
	}
	
	
	@Test
	public void testSelectByModel()
	{
		DictionaryModel model = new DictionaryModel();
		model.setParentValueId(3132);
		List <DictionaryModel> list = dictionaryProxy.selectByModel(model);
		System.out.println(list);
//		Assert.isTrue(list.size()>0);
	}
	@Test
	public void testSelectByModelCount()
	{
		DictionaryModel model = new DictionaryModel();
		model.setParentValueId(3132);
		Integer count = dictionaryProxy.selectByModelCount(model);
		System.out.println(count);
	}
	@Test
	public void testInitDicInfoDataToCache()
	{
		dictionaryProxy.initDicInfoDataToCache();
	}

}
