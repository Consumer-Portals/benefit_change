package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement; 

@XmlRootElement
public class PlansRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String groupId;
	private String county;
	private String effectiveDate;
	private List<MemberInfo> memberInfoList;

	public String getCounty() {
		return county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	public List<MemberInfo> getMemberInfoList() {
		return memberInfoList;
	}
	public void setMemberInfoList(List<MemberInfo> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}	

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
