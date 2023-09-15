package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegionCodes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> regionCodeList;
	private ServiceError serviceError;	

	public List<String> getRegionCodeList() {
		return regionCodeList;
	}

	public void setRegionCodeList(List<String> regionCodeList) {
		this.regionCodeList = regionCodeList;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}
}
