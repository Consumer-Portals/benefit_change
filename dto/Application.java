package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Application implements Serializable{
	/**
	 * 
	 */ 
	private static final long serialVersionUID = 1L;
	private String applicationId;
	private Date applSubmittedDate;
	private String applStatusCode;
	private Date subscriberDateOfBirth;
	private String tobaccoUsageSubscriberInd;
	private String subscriberGender;

	public String getSubscriberGender() {
		return subscriberGender;
	}



	public void setSubscriberGender(String subscriberGender) {
		this.subscriberGender = subscriberGender;
	}



	public Date getSubscriberDateOfBirth() {
		return subscriberDateOfBirth;
	}

	public void setSubscriberDateOfBirth(Date subscriberDateOfBirth) {
		this.subscriberDateOfBirth = subscriberDateOfBirth;
	}

	public String getTobaccoUsageSubscriberInd() {
		return tobaccoUsageSubscriberInd;
	}

	public void setTobaccoUsageSubscriberInd(String tobaccoUsageSubscriberInd) {
		this.tobaccoUsageSubscriberInd = tobaccoUsageSubscriberInd;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Date getApplSubmittedDate() {
		return applSubmittedDate;
	}

	public void setApplSubmittedDate(Date applSubmittedDate) {
		this.applSubmittedDate = applSubmittedDate;
	}

	public String getApplStatusCode() {
		return applStatusCode;
	}

	public void setApplStatusCode(String applStatusCode) {
		this.applStatusCode = applStatusCode;
	}

}
