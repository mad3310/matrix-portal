package com.letv.portal.model;


/**Program Name: Result <br>
 * Description:  rest api 返回结果<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月26日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class Result extends BaseModel {
	
	private static final long serialVersionUID = -3084865204284301985L;
	
	private String code;   //主键ID
	private String msg;   //主键ID
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
