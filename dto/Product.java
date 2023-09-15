package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private String planId;
	private String region;
	private Date effectiveDate;
	private Date termDate;
	private String groupId;
	private String planName;
	private String metalLevel;
	private String network;
	private String deductible;
	private String officevisitcopay;
	private String specialistcopay;
	private String oopmax;
	private String prescriptioncoverage;
	private double totalrate;
	private String sbcEnglishLoc;
	private String productType; // This will be populated with D or V only while
								// returning dental/vision plans
	private int ratingArea; // thsi is applicable only for searching
							// dental/vision plan rates.

	private String sbcPlanId; 

	public String getSbcPlanId() {
		return sbcPlanId;
	}
	public void setSbcPlanId(String sbcPlanId) {
		this.sbcPlanId = sbcPlanId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getTermDate() {
		return termDate;
	}

	public void setTermDate(Date termDate) {
		this.termDate = termDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getMetalLevel() {
		return metalLevel;
	}

	public void setMetalLevel(String metalLevel) {
		this.metalLevel = metalLevel;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getDeductible() {
		return deductible;
	}

	public void setDeductible(String deductible) {
		this.deductible = deductible;
	}

	public String getOfficevisitcopay() {
		return officevisitcopay;
	}

	public void setOfficevisitcopay(String officevisitcopay) {
		this.officevisitcopay = officevisitcopay;
	}

	public String getSpecialistcopay() {
		return specialistcopay;
	}

	public void setSpecialistcopay(String specialistcopay) {
		this.specialistcopay = specialistcopay;
	}

	public String getOopmax() {
		return oopmax;
	}

	public void setOopmax(String oopmax) {
		this.oopmax = oopmax;
	}

	public String getPrescriptioncoverage() {
		return prescriptioncoverage;
	}

	public void setPrescriptioncoverage(String prescriptioncoverage) {
		this.prescriptioncoverage = prescriptioncoverage;
	}

	public double getTotalrate() {
		return totalrate;
	}

	public void setTotalrate(double totalrate) {
		this.totalrate = totalrate;
	}

	public String getSbcEnglishLoc() {
		return sbcEnglishLoc;
	}

	public void setSbcEnglishLoc(String sbcEnglishLoc) {
		this.sbcEnglishLoc = sbcEnglishLoc;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getRatingArea() {
		return ratingArea;
	}

	public void setRatingArea(int ratingArea) {
		this.ratingArea = ratingArea;
	}

}
