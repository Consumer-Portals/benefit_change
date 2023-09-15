package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlanSearchMembers implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<PlanSearchMember> planSearchMemberList;

	public List<PlanSearchMember> getPlanSearchMemberList() {
		return planSearchMemberList;
	}

	public void setPlanSearchMemberList(
			List<PlanSearchMember> planSearchMemberList) {
		this.planSearchMemberList = planSearchMemberList;
	}

}
