package com.letv.webportal.api.demo;

import junit.framework.Assert;

import org.junit.Test;

import com.letv.common.util.HttpUtil;
import com.letv.webportal.api.base.AbstractTestCase;

public class DeomTest extends AbstractTestCase {

	@Test
	public void testDeomo() {
		String url = "http://localhost:8080/web-service/api/demo/test.do?name=111";
		String result = HttpUtil.readContent(url);
		Assert.assertEquals("{\"callback\":null,\"data\":null,\"msgs\":[\"系统出现异常，请稍后再试\"],\"result\":0}", result.trim());
	}
}
