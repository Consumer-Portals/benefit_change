package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Plans implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private List<String> plans;
		
		private ServiceError serviceError;
		
		public Plans() {
			super();
		}

		public List<String> getPlans() {
			return plans;
		}

		public void setPlans(List<String> plans) {
			this.plans = plans;
		}

		public ServiceError getServiceError() {
			return serviceError;
		}
		
		public void setServiceError(ServiceError serviceError) {
			this.serviceError = serviceError;
		}
}
