package com.bcbst.benefitchange.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringWrapper {
	private static final long serialVersionUID = 1L;
	private ServiceError serviceError;
	private String value;
	
	public ServiceError getServiceError() {
		return serviceError;
	}
	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	
}
