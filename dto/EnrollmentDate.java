package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class EnrollmentDate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date startDate;
	private Date endDate;
	private Date planEffectiveDate;
	private Date planTermDate;
	private Date effectiveDate;
	private ServiceError serviceError;
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getPlanEffectiveDate() {
		return planEffectiveDate;
	}
	public void setPlanEffectiveDate(Date planEffectiveDate) {
		this.planEffectiveDate = planEffectiveDate;
	}
	public Date getPlanTermDate() {
		return planTermDate;
	}
	public void setPlanTermDate(Date planTermDate) {
		this.planTermDate = planTermDate;
	}
}
