package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlanSearchMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String applicationId;
	private String dependentSeqNum;
	private String lastName;
	private String middleName;
	private String firstName;
	private String gender;
	private Date dateofBirth;
	private String tobaccoUsageInd;
	private String relationShipTypeInd;
	private String relationShipTypeDesc;
	private String subscriberId;
	private String countyName;
	private Date appSubmittedDate;
	private String zipcode;

	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Date getAppSubmittedDate() {
		return appSubmittedDate;
	}

	public void setAppSubmittedDate(Date appSubmittedDate) {
		this.appSubmittedDate = appSubmittedDate;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getDependentSeqNum() {
		return dependentSeqNum;
	}

	public void setDependentSeqNum(String dependentSeqNum) {
		this.dependentSeqNum = dependentSeqNum;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getTobaccoUsageInd() {
		return tobaccoUsageInd;
	}

	public void setTobaccoUsageInd(String tobaccoUsageInd) {
		this.tobaccoUsageInd = tobaccoUsageInd;
	}

	public String getRelationShipTypeInd() {
		return relationShipTypeInd;
	}

	public void setRelationShipTypeInd(String relationShipTypeInd) {
		this.relationShipTypeInd = relationShipTypeInd;
	}

	public String getRelationShipTypeDesc() {
		return relationShipTypeDesc;
	}

	public void setRelationShipTypeDesc(String relationShipTypeDesc) {
		this.relationShipTypeDesc = relationShipTypeDesc;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

}
