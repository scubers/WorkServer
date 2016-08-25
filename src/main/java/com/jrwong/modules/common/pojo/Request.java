package com.jrwong.modules.common.pojo;

import java.util.Map;

public class Request {
	private PublicInfo publicInfo;
	private Map parameter;
	public PublicInfo getPublicInfo() {
		return publicInfo;
	}
	public void setPublicInfo(PublicInfo publicInfo) {
		this.publicInfo = publicInfo;
	}
	public Map getParameter() {
		return parameter;
	}
	public void setParameter(Map parameter) {
		this.parameter = parameter;
	}
	
}
