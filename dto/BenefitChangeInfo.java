package com.bcbst.benefitchange.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Vijay Narsingoju
 * 
 */
@XmlRootElement
public class BenefitChangeInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String applicationId;
	private String groupId;
	private String subscriberId;
	private String subscriberLastName;
	private String subscriberFirstName;
	private String subscriberMiddleName;
	private Date subscriberDateOfBirth;
	private String updatedSubscriberLastName;
	private String updatedSubscriberFirstName;
	private String updatedSubscriberMiddleName;
	private String subscriberReasonForNameChange;
	private String changePhoneInd;
	private String emailAddress;
	private Date subscriberPolicyCancelDate;
	private String subscriberPolicyCancelReason;
	private Date subscriberMedicalCancelDate; // For medical cancel date
	private String subscriberMedicalCancelReason; // medical cancel reason
	private Date subscriberDentalCancelDate; // For dental cancel date
	private String subscriberDentalCancelReason; // dental cancel reason
	private Date subscriberVisionCancelDate; // For vision cancel date
	private String subscriberVisionCancelReason; // vision cancel reason
	private String cancelDentalVisionInd;
	private String tobaccoUsageInd;
	private String tobaccoUsageSubscriberInd;
	private String tobaccoUsageSpouseInd;
	private String termSubscriberInd;
	private String termSubscriberNewIdRsnCd;
	private String termSubscriberNewIdRsnDesc;
	private String subscAddDelSecInd;
	private String termLifeCovInd;
	private String addDelAncPrdInd;
	private String subscriberVisionInd;
	private String subscriberVisionChangeInd;
	private String subscriberVisionExamOnlyInd;
	private String subscriberVisExamMatrlsInd;
	private String dentalInd;
	private String dentalAddInd;
	private String dentalRemInd;
	private String changeBenInd;
	private String benifitPlan;
	private String benefitNetwork;
	private Date benefitChangeEventDate;
	private String rsnOpenEnrollInd;
	private String rsnBrthAdpStrCrInd;
	private String rsnPermanentMoveInd;
	private String rsnNonCalyrPolExpInd;
	private String rsnMrgInd;
	private String rsnLossOfDepInd;
	private String rsnLossOfMnHlthInd;
	private String rsnRedOfHrsInd;
	private String rsnGainDepInd;
	private String rsnAccessToICHRAInd;
	private String stdEffGuidelinesInd;
	private String firstMonthEffDateInd;
	private String eventDateInd;
	private String firstDayMthFollowingSubmInd;
	private String changePInfoInd;
	private String changeNameInd;
	private String changeEmailAddrInd;
	private String subscriberDaytimePhone;
	private String termPolicyInd;
	private String addRemDepInd;
	private Date applSubmittedDate;
	private String applStatusCode;
	private float medicalRatePerMonth;
	private float dentalRatePerMonth;
	private float visionRatePerMonth;
	private List<Dependent> dependents; // can have multiple dependents
	private List<Address> addresses; // can have multiple addresses
	private List<PlanSearchMember> planSearchMembers; // it will have multiple members information for plan search.
	private boolean updated;
	private String action; // type of action: Save -> Database persistence. Submit -> Database persistence and generates PDF.
	private String medicalPlanName;
	private String dentalPlanName;
	private String visionPlanName;
	private String changePolicyind;
	private String changeAddressind;
	private String visionDelInd;
	private String medicalSBCLoc;
	private String dentalSBCLoc;
	private String visionSBCLoc;
	private String userType;
	private String repId;
	private String representativeName;
	private String representativeEmail;

	private ServiceError serviceError;

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getSubscriberLastName() {
		return subscriberLastName;
	}

	public void setSubscriberLastName(String subscriberLastName) {
		this.subscriberLastName = subscriberLastName;
	}

	public String getSubscriberFirstName() {
		return subscriberFirstName;
	}

	public void setSubscriberFirstName(String subscriberFirstName) {
		this.subscriberFirstName = subscriberFirstName;
	}

	public String getSubscriberMiddleName() {
		return subscriberMiddleName;
	}

	public void setSubscriberMiddleName(String subscriberMiddleName) {
		this.subscriberMiddleName = subscriberMiddleName;
	}

	public Date getSubscriberDateOfBirth() {
		return subscriberDateOfBirth;
	}

	public void setSubscriberDateOfBirth(Date subscriberDateOfBirth) {
		this.subscriberDateOfBirth = subscriberDateOfBirth;
	}

	public String getUpdatedSubscriberLastName() {
		return updatedSubscriberLastName;
	}

	public void setUpdatedSubscriberLastName(String updatedSubscriberLastName) {
		this.updatedSubscriberLastName = updatedSubscriberLastName;
	}

	public String getUpdatedSubscriberFirstName() {
		return updatedSubscriberFirstName;
	}

	public void setUpdatedSubscriberFirstName(String updatedSubscriberFirstName) {
		this.updatedSubscriberFirstName = updatedSubscriberFirstName;
	}

	public String getUpdatedSubscriberMiddleName() {
		return updatedSubscriberMiddleName;
	}

	public void setUpdatedSubscriberMiddleName(
			String updatedSubscriberMiddleName) {
		this.updatedSubscriberMiddleName = updatedSubscriberMiddleName;
	}

	public String getSubscriberReasonForNameChange() {
		return subscriberReasonForNameChange;
	}

	public void setSubscriberReasonForNameChange(
			String subscriberReasonForNameChange) {
		this.subscriberReasonForNameChange = subscriberReasonForNameChange;
	}

	public String getChangePhoneInd() {
		return changePhoneInd;
	}

	public void setChangePhoneInd(String changePhoneInd) {
		this.changePhoneInd = changePhoneInd;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getSubscriberPolicyCancelDate() {
		return subscriberPolicyCancelDate;
	}

	public void setSubscriberPolicyCancelDate(Date subscriberPolicyCancelDate) {
		this.subscriberPolicyCancelDate = subscriberPolicyCancelDate;
	}

	public String getSubscriberPolicyCancelReason() {
		return subscriberPolicyCancelReason;
	}

	public void setSubscriberPolicyCancelReason(
			String subscriberPolicyCancelReason) {
		this.subscriberPolicyCancelReason = subscriberPolicyCancelReason;
	}

	public Date getSubscriberMedicalCancelDate() {
		return subscriberMedicalCancelDate;
	}

	public void setSubscriberMedicalCancelDate(Date subscriberMedicalCancelDate) {
		this.subscriberMedicalCancelDate = subscriberMedicalCancelDate;
	}

	public String getSubscriberMedicalCancelReason() {
		return subscriberMedicalCancelReason;
	}

	public void setSubscriberMedicalCancelReason(
			String subscriberMedicalCancelReason) {
		this.subscriberMedicalCancelReason = subscriberMedicalCancelReason;
	}

	public Date getSubscriberDentalCancelDate() {
		return subscriberDentalCancelDate;
	}

	public void setSubscriberDentalCancelDate(Date subscriberDentalCancelDate) {
		this.subscriberDentalCancelDate = subscriberDentalCancelDate;
	}

	public String getSubscriberDentalCancelReason() {
		return subscriberDentalCancelReason;
	}

	public void setSubscriberDentalCancelReason(
			String subscriberDentalCancelReason) {
		this.subscriberDentalCancelReason = subscriberDentalCancelReason;
	}

	public Date getSubscriberVisionCancelDate() {
		return subscriberVisionCancelDate;
	}

	public void setSubscriberVisionCancelDate(Date subscriberVisionCancelDate) {
		this.subscriberVisionCancelDate = subscriberVisionCancelDate;
	}

	public String getSubscriberVisionCancelReason() {
		return subscriberVisionCancelReason;
	}

	public void setSubscriberVisionCancelReason(
			String subscriberVisionCancelReason) {
		this.subscriberVisionCancelReason = subscriberVisionCancelReason;
	}

	public String getCancelDentalVisionInd() {
		return cancelDentalVisionInd;
	}

	public void setCancelDentalVisionInd(String cancelDentalVisionInd) {
		this.cancelDentalVisionInd = cancelDentalVisionInd;
	}

	public String getTobaccoUsageInd() {
		return tobaccoUsageInd;
	}

	public void setTobaccoUsageInd(String tobaccoUsageInd) {
		this.tobaccoUsageInd = tobaccoUsageInd;
	}

	public String getTobaccoUsageSubscriberInd() {
		return tobaccoUsageSubscriberInd;
	}

	public void setTobaccoUsageSubscriberInd(String tobaccoUsageSubscriberInd) {
		this.tobaccoUsageSubscriberInd = tobaccoUsageSubscriberInd;
	}

	public String getTobaccoUsageSpouseInd() {
		return tobaccoUsageSpouseInd;
	}

	public void setTobaccoUsageSpouseInd(String tobaccoUsageSpouseInd) {
		this.tobaccoUsageSpouseInd = tobaccoUsageSpouseInd;
	}

	public String getTermSubscriberInd() {
		return termSubscriberInd;
	}

	public void setTermSubscriberInd(String termSubscriberInd) {
		this.termSubscriberInd = termSubscriberInd;
	}

	public String getTermSubscriberNewIdRsnCd() {
		return termSubscriberNewIdRsnCd;
	}

	public void setTermSubscriberNewIdRsnCd(String termSubscriberNewIdRsnCd) {
		this.termSubscriberNewIdRsnCd = termSubscriberNewIdRsnCd;
	}

	public String getTermSubscriberNewIdRsnDesc() {
		return termSubscriberNewIdRsnDesc;
	}

	public void setTermSubscriberNewIdRsnDesc(String termSubscriberNewIdRsnDesc) {
		this.termSubscriberNewIdRsnDesc = termSubscriberNewIdRsnDesc;
	}

	public String getSubscAddDelSecInd() {
		return subscAddDelSecInd;
	}

	public void setSubscAddDelSecInd(String subscAddDelSecInd) {
		this.subscAddDelSecInd = subscAddDelSecInd;
	}

	public String getTermLifeCovInd() {
		return termLifeCovInd;
	}

	public void setTermLifeCovInd(String termLifeCovInd) {
		this.termLifeCovInd = termLifeCovInd;
	}

	public String getAddDelAncPrdInd() {
		return addDelAncPrdInd;
	}

	public void setAddDelAncPrdInd(String addDelAncPrdInd) {
		this.addDelAncPrdInd = addDelAncPrdInd;
	}

	public String getSubscriberVisionInd() {
		return subscriberVisionInd;
	}

	public void setSubscriberVisionInd(String subscriberVisionInd) {
		this.subscriberVisionInd = subscriberVisionInd;
	}

	public String getSubscriberVisionChangeInd() {
		return subscriberVisionChangeInd;
	}

	public void setSubscriberVisionChangeInd(String subscriberVisionChangeInd) {
		this.subscriberVisionChangeInd = subscriberVisionChangeInd;
	}

	public String getSubscriberVisionExamOnlyInd() {
		return subscriberVisionExamOnlyInd;
	}

	public void setSubscriberVisionExamOnlyInd(
			String subscriberVisionExamOnlyInd) {
		this.subscriberVisionExamOnlyInd = subscriberVisionExamOnlyInd;
	}

	public String getSubscriberVisExamMatrlsInd() {
		return subscriberVisExamMatrlsInd;
	}

	public void setSubscriberVisExamMatrlsInd(String subscriberVisExamMatrlsInd) {
		this.subscriberVisExamMatrlsInd = subscriberVisExamMatrlsInd;
	}

	public String getDentalInd() {
		return dentalInd;
	}

	public void setDentalInd(String dentalInd) {
		this.dentalInd = dentalInd;
	}

	public String getDentalAddInd() {
		return dentalAddInd;
	}

	public void setDentalAddInd(String dentalAddInd) {
		this.dentalAddInd = dentalAddInd;
	}

	public String getDentalRemInd() {
		return dentalRemInd;
	}

	public void setDentalRemInd(String dentalRemInd) {
		this.dentalRemInd = dentalRemInd;
	}

	public String getChangeBenInd() {
		return changeBenInd;
	}

	public void setChangeBenInd(String changeBenInd) {
		this.changeBenInd = changeBenInd;
	}

	public String getBenifitPlan() {
		return benifitPlan;
	}

	public void setBenifitPlan(String benifitPlan) {
		this.benifitPlan = benifitPlan;
	}

	public String getBenefitNetwork() {
		return benefitNetwork;
	}

	public void setBenefitNetwork(String benefitNetwork) {
		this.benefitNetwork = benefitNetwork;
	}

	public Date getBenefitChangeEventDate() {
		return benefitChangeEventDate;
	}

	public void setBenefitChangeEventDate(Date benefitChangeEventDate) {
		this.benefitChangeEventDate = benefitChangeEventDate;
	}

	public String getRsnOpenEnrollInd() {
		return rsnOpenEnrollInd;
	}

	public void setRsnOpenEnrollInd(String rsnOpenEnrollInd) {
		this.rsnOpenEnrollInd = rsnOpenEnrollInd;
	}

	public String getRsnBrthAdpStrCrInd() {
		return rsnBrthAdpStrCrInd;
	}

	public void setRsnBrthAdpStrCrInd(String rsnBrthAdpStrCrInd) {
		this.rsnBrthAdpStrCrInd = rsnBrthAdpStrCrInd;
	}

	public String getRsnPermanentMoveInd() {
		return rsnPermanentMoveInd;
	}

	public void setRsnPermanentMoveInd(String rsnPermanentMoveInd) {
		this.rsnPermanentMoveInd = rsnPermanentMoveInd;
	}

	public String getRsnNonCalyrPolExpInd() {
		return rsnNonCalyrPolExpInd;
	}

	public void setRsnNonCalyrPolExpInd(String rsnNonCalyrPolExpInd) {
		this.rsnNonCalyrPolExpInd = rsnNonCalyrPolExpInd;
	}

	public String getRsnMrgInd() {
		return rsnMrgInd;
	}

	public void setRsnMrgInd(String rsnMrgInd) {
		this.rsnMrgInd = rsnMrgInd;
	}

	public String getRsnLossOfDepInd() {
		return rsnLossOfDepInd;
	}

	public void setRsnLossOfDepInd(String rsnLossOfDepInd) {
		this.rsnLossOfDepInd = rsnLossOfDepInd;
	}

	public String getRsnLossOfMnHlthInd() {
		return rsnLossOfMnHlthInd;
	}

	public void setRsnLossOfMnHlthInd(String rsnLossOfMnHlthInd) {
		this.rsnLossOfMnHlthInd = rsnLossOfMnHlthInd;
	}

	public String getRsnRedOfHrsInd() {
		return rsnRedOfHrsInd;
	}

	public void setRsnRedOfHrsInd(String rsnRedOfHrsInd) {
		this.rsnRedOfHrsInd = rsnRedOfHrsInd;
	}

	public String getRsnGainDepInd() {
		return rsnGainDepInd;
	}

	public void setRsnGainDepInd(String rsnGainDepInd) {
		this.rsnGainDepInd = rsnGainDepInd;
	}	

	public String getRsnAccessToICHRAInd() {
		return rsnAccessToICHRAInd;
	}

	public void setRsnAccessToICHRAInd(String rsnAccessToICHRAInd) {
		this.rsnAccessToICHRAInd = rsnAccessToICHRAInd;
	}

	public String getStdEffGuidelinesInd() {
		return stdEffGuidelinesInd;
	}

	public void setStdEffGuidelinesInd(String stdEffGuidelinesInd) {
		this.stdEffGuidelinesInd = stdEffGuidelinesInd;
	}

	public String getFirstMonthEffDateInd() {
		return firstMonthEffDateInd;
	}

	public void setFirstMonthEffDateInd(String firstMonthEffDateInd) {
		this.firstMonthEffDateInd = firstMonthEffDateInd;
	}

	public String getEventDateInd() {
		return eventDateInd;
	}

	public void setEventDateInd(String eventDateInd) {
		this.eventDateInd = eventDateInd;
	}

	public String getFirstDayMthFollowingSubmInd() {
		return firstDayMthFollowingSubmInd;
	}

	public void setFirstDayMthFollowingSubmInd(String firstDayMthFollowingSubmInd) {
		this.firstDayMthFollowingSubmInd = firstDayMthFollowingSubmInd;
	}

	public String getChangePInfoInd() {
		return changePInfoInd;
	}

	public void setChangePInfoInd(String changePInfoInd) {
		this.changePInfoInd = changePInfoInd;
	}

	public String getChangeNameInd() {
		return changeNameInd;
	}

	public void setChangeNameInd(String changeNameInd) {
		this.changeNameInd = changeNameInd;
	}

	public String getChangeEmailAddrInd() {
		return changeEmailAddrInd;
	}

	public void setChangeEmailAddrInd(String changeEmailAddrInd) {
		this.changeEmailAddrInd = changeEmailAddrInd;
	}

	public String getSubscriberDaytimePhone() {
		return subscriberDaytimePhone;
	}

	public void setSubscriberDaytimePhone(String subscriberDaytimePhone) {
		this.subscriberDaytimePhone = subscriberDaytimePhone;
	}

	public String getTermPolicyInd() {
		return termPolicyInd;
	}

	public void setTermPolicyInd(String termPolicyInd) {
		this.termPolicyInd = termPolicyInd;
	}

	public String getAddRemDepInd() {
		return addRemDepInd;
	}

	public void setAddRemDepInd(String addRemDepInd) {
		this.addRemDepInd = addRemDepInd;
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

	public float getMedicalRatePerMonth() {
		return medicalRatePerMonth;
	}

	public void setMedicalRatePerMonth(float medicalRatePerMonth) {
		this.medicalRatePerMonth = medicalRatePerMonth;
	}

	public float getDentalRatePerMonth() {
		return dentalRatePerMonth;
	}

	public void setDentalRatePerMonth(float dentalRatePerMonth) {
		this.dentalRatePerMonth = dentalRatePerMonth;
	}

	public float getVisionRatePerMonth() {
		return visionRatePerMonth;
	}

	public void setVisionRatePerMonth(float visionRatePerMonth) {
		this.visionRatePerMonth = visionRatePerMonth;
	}

	public List<Dependent> getDependents() {
		return dependents;
	}

	public void setDependents(List<Dependent> dependents) {
		this.dependents = dependents;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<PlanSearchMember> getPlanSearchMembers() {
		return planSearchMembers;
	}

	public void setPlanSearchMembers(List<PlanSearchMember> planSearchMembers) {
		this.planSearchMembers = planSearchMembers;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}

	public String getDentalPlanName() {
		return dentalPlanName;
	}

	public void setDentalPlanName(String dentalPlanName) {
		this.dentalPlanName = dentalPlanName;
	}

	public String getVisionPlanName() {
		return visionPlanName;
	}

	public void setVisionPlanName(String visionPlanName) {
		this.visionPlanName = visionPlanName;
	}

	public String getChangePolicyind() {
		return changePolicyind;
	}

	public void setChangePolicyind(String changePolicyind) {
		this.changePolicyind = changePolicyind;
	}

	public String getChangeAddressind() {
		return changeAddressind;
	}

	public void setChangeAddressind(String changeAddressind) {
		this.changeAddressind = changeAddressind;
	}

	public String getVisionDelInd() {
		return visionDelInd;
	}

	public void setVisionDelInd(String visionDelInd) {
		this.visionDelInd = visionDelInd;
	}

	public String getMedicalPlanName() {
		return medicalPlanName;
	}

	public void setMedicalPlanName(String medicalPlanName) {
		this.medicalPlanName = medicalPlanName;
	}

	public String getMedicalSBCLoc() {
		return medicalSBCLoc;
	}

	public void setMedicalSBCLoc(String medicalSBCLoc) {
		this.medicalSBCLoc = medicalSBCLoc;
	}

	public String getDentalSBCLoc() {
		return dentalSBCLoc;
	}

	public void setDentalSBCLoc(String dentalSBCLoc) {
		this.dentalSBCLoc = dentalSBCLoc;
	}

	public String getVisionSBCLoc() {
		return visionSBCLoc;
	}

	public void setVisionSBCLoc(String visionSBCLoc) {
		this.visionSBCLoc = visionSBCLoc;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRepId() {
		return repId;
	}

	public void setRepId(String repId) {
		this.repId = repId;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getRepresentativeEmail() {
		return representativeEmail;
	}

	public void setRepresentativeEmail(String representativeEmail) {
		this.representativeEmail = representativeEmail;
	}
}
