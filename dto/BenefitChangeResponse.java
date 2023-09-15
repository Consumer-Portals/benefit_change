package com.bcbst.benefitchange.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BenefitChangeResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private String applicationId;
	private boolean updated;
	private ServiceError serviceError;
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public ServiceError getServiceError() {
		return serviceError;
	}
	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}

	
}
