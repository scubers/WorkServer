package com.jrwong.modules.common.pojo;


public class Response {
	private ErrorInfo errorInfo;
	private Object retInfo;
	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}
	public Object getRetInfo() {
		return retInfo;
	}
	public void setRetInfo(Object retInfo) {
		this.retInfo = retInfo;
	}
	
}
