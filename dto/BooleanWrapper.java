package com.bcbst.benefitchange.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BooleanWrapper {
	private static final long serialVersionUID = 1L;
	private ServiceError serviceError;
	private Boolean value;
	
	public ServiceError getServiceError() {
		return serviceError;
	}
	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}
	public Boolean getValue() {
		return value;
	}
	public void setValue(Boolean value) {
		this.value = value;
	}	
}
