package com.letv.common.util;

import java.io.IOException;
import java.util.Properties;

/**Program Name: ConfigUtil <br>
 * Description:  获取配置文件中对应的值<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月27日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class ConfigUtil {

	private static String FSP = System.getProperty("file.separator");
	private static Properties properties = new Properties();
	// 配置文件名称
	private static final String configName = "/config.properties";
	private static ConfigUtil instance;
	/**
	 * 构造方法 加载配置文件
	 */
	private ConfigUtil() {
		try {
			properties.load(getClass().getResourceAsStream(configName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取单例对象
	 * 
	 * @return EmpConfig
	 */
	private synchronized static ConfigUtil getInstance() {
		if (null == instance) {
			instance = new ConfigUtil();
		}
		return instance;
	}

	/**
	 * 将属性值获取为int型
	 * 
	 * @param str
	 *            属性名
	 * @return
	 */
	public static int getint(String str) {
		try {
			if (null == instance) {
				getInstance();
			}
			return Integer.parseInt(properties.getProperty(str));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将属性值获取为long型
	 * 
	 * @param str
	 *            属性名
	 * @return
	 */
	public static long getlong(String str) {
		try {
			if (null == instance) {
				getInstance();
			}
			return Long.parseLong(properties.getProperty(str));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将属性值获取为double型
	 * 
	 * @param str
	 *            属性名
	 * @return
	 */
	public static double getdouble(String str) {
		try {
			if (null == instance) {
				getInstance();
			}
			return Double.parseDouble(properties.getProperty(str));

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将属性值获取为String型
	 * 
	 * @param str
	 *            属性名
	 * @return
	 */
	public static String getString(String str) {
		try {
			if (null == instance) {
				getInstance();
			}
			return properties.getProperty(str);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将属性值获取为boolean型
	 * 
	 * @param str
	 *            属性名
	 * @return
	 */
	public static boolean getBoolean(String str) {
		try {
			if (null == instance) {
				getInstance();
			}
			return Boolean.parseBoolean(properties.getProperty(str));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
