package com.bcbst.benefitchange.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar; 

@XmlRootElement
public class MemberInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	    protected int countyCode;
	    protected int age;
	    protected String memberID;
	    protected String gender;
	    protected XMLGregorianCalendar dob;
	    protected String relationship;
	    protected boolean tobaccoUse;
	    protected String groupId;

		public int getCountyCode() {
			return countyCode;
		}
		public void setCountyCode(int countyCode) {
			this.countyCode = countyCode;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getMemberID() {
			return memberID;
		}
		public void setMemberID(String memberID) {
			this.memberID = memberID;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public XMLGregorianCalendar getDob() {
			return dob;
		}
		public void setDob(XMLGregorianCalendar dob) {
			this.dob = dob;
		}
		public String getRelationship() {
			return relationship;
		}
		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}
		public boolean isTobaccoUse() {
			return tobaccoUse;
		}
		public void setTobaccoUse(boolean tobaccoUse) {
			this.tobaccoUse = tobaccoUse;
		}
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}	 
	 

}
