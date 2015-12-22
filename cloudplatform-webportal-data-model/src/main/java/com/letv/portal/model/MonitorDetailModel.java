package com.letv.portal.model;

import java.util.Date;

import com.letv.common.model.BaseModel;
/**
 * Program Name: MonitorDetailModel <br>
 * Description: 监控服务数据表 <br>
 * @author name: wujun <br>
 * Written Date: 2014年11月11日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class MonitorDetailModel extends BaseModel implements Comparable {

	private static final long serialVersionUID = 5918229179935808282L;
	private String detailName;
	private float detailValue;
	private String dbName;
	private Date monitorDate;
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDetailName() {
		return detailName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}


	public float getDetailValue() {
		return detailValue;
	}

	public void setDetailValue(float detailValue) {
		this.detailValue = detailValue;
	}

	public Date getMonitorDate() {
		return monitorDate;
	}

	public void setMonitorDate(Date monitorDate) {
		this.monitorDate = monitorDate;
	}

    @Override
    public int compareTo(Object o) {
        float detailValue = ((MonitorDetailModel)o).getDetailValue();
        return Float.compare(this.detailValue, detailValue);
    }
}
