package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Counties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> countyList;
	private ServiceError serviceError;

	
	public List<String> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<String> countyList) {
		this.countyList = countyList;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}
	
	

}
