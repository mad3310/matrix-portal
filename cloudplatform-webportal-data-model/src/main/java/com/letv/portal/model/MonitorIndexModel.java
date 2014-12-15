package com.letv.portal.model;

import com.letv.common.model.BaseModel;
/**
 * Program Name: MonitorIndexModel <br>
 * Description: 监控索引表 <br>
 * @author name: wujun <br>
 * Written Date: 2014年11月11日 <br>
 * Modified By: liuhao1<br>
 * Modified Date: 2014年11月15日 <br>
 */
public class MonitorIndexModel extends BaseModel{

	private static final long serialVersionUID = 1L;
	private String titleText;
	private String subTitleText;
	private String yAxisText;
	private String tooltipSuffix;
	private int  flushTime;
	private String  detailTable;
	private String  dataFromApi;
	private int  status;
	
	private String yAxisText1;
	private String yAxisText2;
	private String yAxisText3;
	private String monitorPoint;
	
	public String getTitleText() {
		return titleText;
	}
	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}
	public String getSubTitleText() {
		return subTitleText;
	}
	public void setSubTitleText(String subTitleText) {
		this.subTitleText = subTitleText;
	}
	public String getyAxisText() {
		return yAxisText;
	}
	public void setyAxisText(String yAxisText) {
		this.yAxisText = yAxisText;
	}
	public String getTooltipSuffix() {
		return tooltipSuffix;
	}
	public void setTooltipSuffix(String tooltipSuffix) {
		this.tooltipSuffix = tooltipSuffix;
	}
	public int getFlushTime() {
		return flushTime;
	}
	public void setFlushTime(int flushTime) {
		this.flushTime = flushTime;
	}
	public String getDetailTable() {
		return detailTable;
	}
	public void setDetailTable(String detailTable) {
		this.detailTable = detailTable;
	}
	public String getDataFromApi() {
		return dataFromApi;
	}
	public void setDataFromApi(String dataFromApi) {
		this.dataFromApi = dataFromApi;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getyAxisText1() {
		return yAxisText1;
	}
	public void setyAxisText1(String yAxisText1) {
		this.yAxisText1 = yAxisText1;
	}
	public String getyAxisText2() {
		return yAxisText2;
	}
	public void setyAxisText2(String yAxisText2) {
		this.yAxisText2 = yAxisText2;
	}
	public String getyAxisText3() {
		return yAxisText3;
	}
	public void setyAxisText3(String yAxisText3) {
		this.yAxisText3 = yAxisText3;
	}
	public String getMonitorPoint() {
		return monitorPoint;
	}
	public void setMonitorPoint(String monitorPoint) {
		this.monitorPoint = monitorPoint;
	}

}
