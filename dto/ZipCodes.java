package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ZipCodes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServiceError serviceError;

	private List<String> zipcodeList;

	public List<String> getZipcodeList() {
		return zipcodeList;
	}

	public void setZipcodeList(List<String> zipcodeList) {
		this.zipcodeList = zipcodeList;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}
	
	
}
