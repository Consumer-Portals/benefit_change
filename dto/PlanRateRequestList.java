package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlanRateRequestList implements Serializable {

	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String county;

	private List<PlanRateRequest> rateReqList;
	public List<PlanRateRequest> getRateReqList() {
		return rateReqList;
	}

	public void setRateReqList(List<PlanRateRequest> rateReqList) {
		this.rateReqList = rateReqList;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
	

}
