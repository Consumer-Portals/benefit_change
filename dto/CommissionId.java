package com.bcbst.benefitchange.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommissionId {
	private static final long serialVersionUID = 1L;
	
	private List<String> commissionIdList;
	
	private ServiceError serviceError;

	public CommissionId() {
		super();
	}

	public List<String> getCommissionIdList() {
		return commissionIdList;
	}

	public void setCommissionIdList(List<String> commissionIdList) {
		this.commissionIdList = commissionIdList;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}	
}
