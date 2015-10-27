package com.letv.common.result;

import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装api调用的返回结果
 * 
 * @author liunaikun
 * 
 */
public class ResultObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1000726224584456251L;

	/**
	 * 操作结果。成功：1，失败：0
	 */
	private int result;
	/**
	 * jsonp callback方法名
	 */
	private String callback;
	/**
	 * 附加信息。
	 */
	private List<String> msgs = new ArrayList<String>();
	/**
	 * 用户提示信息
	 */
	private String alertMessage;
	/**
	 * 附加数据
	 */
	private Object data;

	/**
	 * Constructor
	 * 
	 * @param result
	 *            result
	 */
	public ResultObject() {
		this.result = 1;
	}
	public ResultObject(int result) {
		this.result = result;
	}
	public ResultObject(List<ObjectError> errors) {
		for (ObjectError error : errors) {
			this.msgs.add(error.getDefaultMessage());
		}
		this.result = 0;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public List<String> getMsgs() {
		return msgs;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String getAlertMessage() {
		return alertMessage;
	}
	
	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}
	/**
	 * 添加信息
	 * 
	 * @param m
	 *            消息内容
	 */
	public void addMsg(String m) {
		msgs.add(m);
	}

	/**
	 * 清空消息
	 */
	public void clearMsg() {
		msgs.clear();
	}

	/**
	 * 消息是否为空
	 * 
	 * @return result
	 */
	public boolean empty() {
		return msgs.isEmpty();
	}

	/**
	 * 消息数量
	 * 
	 * @return result
	 */
	public int size() {
		return msgs.size();
	}

}
