package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlRootElement
public class PlanRateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private XMLGregorianCalendar rateDate;
	private String planName;
	private List<MemberInfo> memberInfoList;
	private int ratingArea;

	public XMLGregorianCalendar getRateDate() {
		return rateDate;
	}

	public void setRateDate(XMLGregorianCalendar rateDate) {
		this.rateDate = rateDate;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public List<MemberInfo> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<MemberInfo> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	public int getRatingArea() {
		return ratingArea;
	}

	public void setRatingArea(int ratingArea) {
		this.ratingArea = ratingArea;
	}

}
