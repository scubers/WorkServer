package com.jrwong.modules.common.pojo;

public class PublicInfo {
	private String serialNumber;//流水号
	private String busiCode;	//业务编码，必传
	private String date;		//时间
	private String regionCode;	//地区编码
	private String token;		//口令,必传
	private String retType;		//数据返回方式，string,stream
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getBusiCode() {
		return busiCode;
	}
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRetType() {
		return retType;
	}
	public void setRetType(String retType) {
		this.retType = retType;
	}
	
}
