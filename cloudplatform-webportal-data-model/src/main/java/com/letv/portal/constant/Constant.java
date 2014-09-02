package com.letv.portal.constant;

public class Constant {

	public static int PAGE_SIZE = 10;
	
	//container node节点类型
	public static String MCLUSTER_NODE_TYPE_VIP = "VIP";
	public static String MCLUSTER_NODE_TYPE_NORMAL = "normal";
	
	//db审批状态
	public static String DB_AUDIT_STATUS_FALSE = "-1"; 
	public static String DB_AUDIT_STATUS_TRUE_BUILD_OLD_MCLUSTER = "1"; 
	public static String DB_AUDIT_STATUS_TRUE_BUILD_NEW_MCLUSTER = "2"; 
	
	public static String DB_AUDIT_STATUS_BUILD_SUCCESS = "3";  //创建成功
	public static String DB_AUDIT_STATUS_BUILD_FAIL = "4";  //创建失败
	
	
	public static String IS_DELETE_FALSE="0";
	public static String IS_DELETE_TRUE="1";
	
	public static String STATUS_DEFAULT="0";
	
	public static final int MCLUSTER_CONTAINERS_COUNT = 4;

	public static final String IPRESOURCE_STATUS_USERD= "1";
	
	
	
}
