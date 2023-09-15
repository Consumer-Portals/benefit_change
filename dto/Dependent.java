package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date; 

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Vijay Narsingoju
 *
 */
@XmlRootElement
public class Dependent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String applicationId;
	private String dependentSeqNum;
	private String dependentLastName;
	private String dependentFirstName;
	private String dependentMiddleName;
	private String dependentGender;
	private Date dependentDateOfBirth;
	private String dependentSSN;
	private String dependentTobaccoUsageInd;
	private String dependentRelationShipTypeInd; // N = NaturalChild/StepChild , A = Adopted , O = Other, P= spouse
	private String dependentRelationShipDesc;
	private String dependentChangeTypeInd; // A = Add, D = Delete, T = Transfer
	private String dependentMedicareInd;
	private Date dependentTermDate;
	private String dependentTermReason;
	private String subscriberId;
	private Date appSubmittedDate;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getDependentLastName() {
		return dependentLastName;
	}
	public void setDependentLastName(String dependentLastName) {
		this.dependentLastName = dependentLastName;
	}
	public String getDependentFirstName() {
		return dependentFirstName;
	}
	public void setDependentFirstName(String dependentFirstName) {
		this.dependentFirstName = dependentFirstName;
	}
	public String getDependentMiddleName() {
		return dependentMiddleName;
	}
	public void setDependentMiddleName(String dependentMiddleName) {
		this.dependentMiddleName = dependentMiddleName;
	}
	public String getDependentGender() {
		return dependentGender;
	}
	public void setDependentGender(String dependentGender) {
		this.dependentGender = dependentGender;
	}
	
	public Date getDependentDateOfBirth() {
		return dependentDateOfBirth;
	}

	public void setDependentDateOfBirth(Date dependentDateOfBirth) {
		this.dependentDateOfBirth = dependentDateOfBirth;
	}

	public String getDependentSSN() {
		return dependentSSN;
	}
	public void setDependentSSN(String dependentSSN) {
		this.dependentSSN = dependentSSN;
	}
	public String getDependentTobaccoUsageInd() {
		return dependentTobaccoUsageInd;
	}
	public void setDependentTobaccoUsageInd(String dependentTobaccoUsageInd) {
		this.dependentTobaccoUsageInd = dependentTobaccoUsageInd;
	}
	public String getDependentRelationShipTypeInd() {
		return dependentRelationShipTypeInd;
	}
	public void setDependentRelationShipTypeInd(String dependentRelationShipTypeInd) {
		this.dependentRelationShipTypeInd = dependentRelationShipTypeInd;
	}
	public String getDependentRelationShipDesc() {
		return dependentRelationShipDesc;
	}
	public void setDependentRelationShipDesc(String dependentRelationShipDesc) {
		this.dependentRelationShipDesc = dependentRelationShipDesc;
	}
	public String getDependentChangeTypeInd() {
		return dependentChangeTypeInd;
	}
	public void setDependentChangeTypeInd(String dependentChangeTypeInd) {
		this.dependentChangeTypeInd = dependentChangeTypeInd;
	}
	
	public String getDependentMedicareInd() {
		return dependentMedicareInd;
	}

	public void setDependentMedicareInd(String dependentMedicareInd) {
		this.dependentMedicareInd = dependentMedicareInd;
	}

	public Date getDependentTermDate() {
		return dependentTermDate;
	}

	public void setDependentTermDate(Date dependentTermDate) {
		this.dependentTermDate = dependentTermDate;
	}

	public String getDependentTermReason() {
		return dependentTermReason;
	}

	public void setDependentTermReason(String dependentTermReason) {
		this.dependentTermReason = dependentTermReason;
	}

	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public Date getAppSubmittedDate() {
		return appSubmittedDate;
	}
	public void setAppSubmittedDate(Date appSubmittedDate) {
		this.appSubmittedDate = appSubmittedDate;
	}
	
	

}
