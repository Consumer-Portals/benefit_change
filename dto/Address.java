package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date; 

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Vijay Narsingoju
 * 
 */
@XmlRootElement
public class Address implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private String userId;
	private String applicationId;
	private String subscriberId;
	private String subscriberAddressInd; // R = Residence, M = Mailing, B = Billing
	private String streetName;
	private String cityName;
	private String statename;
	private String zipCode;
	private String countyName;
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

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getSubscriberAddressInd() {
		return subscriberAddressInd;
	}

	public void setSubscriberAddressInd(String subscriberAddressInd) {
		this.subscriberAddressInd = subscriberAddressInd;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public Date getAppSubmittedDate() {
		return appSubmittedDate;
	}

	public void setAppSubmittedDate(Date appSubmittedDate) {
		this.appSubmittedDate = appSubmittedDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
