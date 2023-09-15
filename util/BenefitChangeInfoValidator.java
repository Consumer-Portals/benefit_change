package com.bcbst.benefitchange.util;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bcbst.benefitchange.dto.Address;
import com.bcbst.benefitchange.dto.BenefitChangeInfo;
import com.bcbst.benefitchange.dto.Dependent;
import com.bcbst.benefitchange.dto.MemberInfo;
import com.bcbst.benefitchange.dto.PlanSearchMember;
import com.bcbst.benefitchange.dto.PlansRequest;

/**
 * @objective This validator class will not allow invalid characters to be stored in database. 
 * @author Vijay Narsingoju
 *
 */
public class BenefitChangeInfoValidator {
	private static Logger logger = LogManager.getLogger(BenefitChangeInfoValidator.class);
	
	/**
	 * @objective This method is for restricting invalid characters.   
	 * @param info
	 * @return
	 */
	public static String validateBenefitChangeInfo(BenefitChangeInfo info){
		StringBuilder invalidFields = new StringBuilder();
		String fieldsString = "";
		try{
			if (info == null){
				invalidFields.append("BenefitChangeInfo");
				return invalidFields.toString();
			}			
						
			FieldsValidator.validateString(invalidFields, info.getAction());
			FieldsValidator.validateString(invalidFields, info.getAddDelAncPrdInd());
			FieldsValidator.validateString(invalidFields, info.getAddRemDepInd());
			
			FieldsValidator.validateString(invalidFields, info.getApplicationId());
			FieldsValidator.validateString(invalidFields, info.getApplStatusCode());
			FieldsValidator.validateString(invalidFields, info.getBenefitNetwork());
			FieldsValidator.validateString(invalidFields, info.getBenifitPlan());
			FieldsValidator.validateString(invalidFields, info.getCancelDentalVisionInd());
			FieldsValidator.validateString(invalidFields, info.getChangeAddressind());
			FieldsValidator.validateString(invalidFields, info.getChangeBenInd());
			FieldsValidator.validateString(invalidFields, info.getChangeEmailAddrInd());
			FieldsValidator.validateString(invalidFields, info.getChangeNameInd());
			FieldsValidator.validateString(invalidFields, info.getChangePhoneInd());
			FieldsValidator.validateString(invalidFields, info.getChangePInfoInd());
			FieldsValidator.validateString(invalidFields, info.getChangePolicyind());
			FieldsValidator.validateString(invalidFields, info.getDentalAddInd());
			FieldsValidator.validateString(invalidFields, info.getDentalInd());
			FieldsValidator.validate(invalidFields, info.getDentalPlanName());
			FieldsValidator.validateString(invalidFields, info.getDentalRemInd());
			FieldsValidator.validate(invalidFields, info.getDentalSBCLoc());
			FieldsValidator.validate(invalidFields, info.getEmailAddress());
			FieldsValidator.validateString(invalidFields, info.getEventDateInd());
			FieldsValidator.validateString(invalidFields, info.getFirstMonthEffDateInd());
			FieldsValidator.validateString(invalidFields, info.getFirstDayMthFollowingSubmInd());
			FieldsValidator.validateString(invalidFields, info.getGroupId());
			FieldsValidator.validateString(invalidFields, info.getMedicalPlanName());
			FieldsValidator.validate(invalidFields, info.getMedicalSBCLoc());
			FieldsValidator.validateString(invalidFields, info.getRsnBrthAdpStrCrInd());
			FieldsValidator.validateString(invalidFields, info.getRsnGainDepInd());
			FieldsValidator.validateString(invalidFields, info.getRsnLossOfDepInd());
			FieldsValidator.validateString(invalidFields, info.getRsnLossOfMnHlthInd());
			FieldsValidator.validateString(invalidFields, info.getRsnMrgInd());
			FieldsValidator.validateString(invalidFields, info.getRsnNonCalyrPolExpInd());
			FieldsValidator.validateString(invalidFields, info.getRsnOpenEnrollInd());
			FieldsValidator.validateString(invalidFields, info.getRsnPermanentMoveInd());
			FieldsValidator.validateString(invalidFields, info.getRsnRedOfHrsInd());
			FieldsValidator.validateString(invalidFields, info.getRsnAccessToICHRAInd());
			FieldsValidator.validateString(invalidFields, info.getStdEffGuidelinesInd());
			FieldsValidator.validateString(invalidFields, info.getSubscAddDelSecInd());
			FieldsValidator.validate(invalidFields, info.getSubscriberDaytimePhone());
			FieldsValidator.validate(invalidFields, info.getSubscriberDentalCancelReason());
			FieldsValidator.validate(invalidFields, info.getSubscriberFirstName());
			FieldsValidator.validateString(invalidFields, info.getSubscriberId());
			FieldsValidator.validate(invalidFields, info.getSubscriberLastName());
			FieldsValidator.validate(invalidFields, info.getSubscriberMedicalCancelReason());
			FieldsValidator.validate(invalidFields, info.getSubscriberMiddleName());
			FieldsValidator.validate(invalidFields, info.getSubscriberPolicyCancelReason());
			FieldsValidator.validate(invalidFields, info.getSubscriberReasonForNameChange());
			FieldsValidator.validateString(invalidFields, info.getSubscriberVisExamMatrlsInd());
			FieldsValidator.validate(invalidFields, info.getSubscriberVisionCancelReason());
			FieldsValidator.validateString(invalidFields, info.getSubscriberVisionChangeInd());
			FieldsValidator.validateString(invalidFields, info.getSubscriberVisionExamOnlyInd());
			FieldsValidator.validateString(invalidFields, info.getSubscriberVisionInd());
			FieldsValidator.validateString(invalidFields, info.getTermLifeCovInd());
			FieldsValidator.validateString(invalidFields, info.getTermPolicyInd());
			FieldsValidator.validateString(invalidFields, info.getTermSubscriberInd());
			FieldsValidator.validateString(invalidFields, info.getTermSubscriberNewIdRsnCd());
			FieldsValidator.validateString(invalidFields, info.getTobaccoUsageInd());
			FieldsValidator.validateString(invalidFields, info.getTobaccoUsageSpouseInd());
			FieldsValidator.validateString(invalidFields, info.getTobaccoUsageSubscriberInd());
			FieldsValidator.validate(invalidFields, info.getUpdatedSubscriberFirstName());
			FieldsValidator.validate(invalidFields, info.getUpdatedSubscriberLastName());
			FieldsValidator.validate(invalidFields, info.getUpdatedSubscriberMiddleName());
			FieldsValidator.validate(invalidFields, info.getUserId());
			FieldsValidator.validateString(invalidFields, info.getVisionDelInd());
			FieldsValidator.validateString(invalidFields, info.getVisionPlanName());
			FieldsValidator.validate(invalidFields, info.getVisionSBCLoc());
			if (info.getAddresses() != null && !info.getAddresses().isEmpty()){
				// validate addresses
				List<Address> addressList = info.getAddresses(); 
				for (Address address: addressList){
					FieldsValidator.validateString(invalidFields, address.getApplicationId());
					FieldsValidator.validateString(invalidFields, address.getCityName());
					FieldsValidator.validateString(invalidFields, address.getCountyName());
					FieldsValidator.validateString(invalidFields, address.getStatename());
					FieldsValidator.validate(invalidFields, address.getStreetName());
					FieldsValidator.validateString(invalidFields, address.getSubscriberAddressInd());
					FieldsValidator.validateString(invalidFields, address.getSubscriberId());
					FieldsValidator.validate(invalidFields, address.getUserId());
					FieldsValidator.validateString(invalidFields, address.getZipCode());
				}
				
			}
			if (info.getPlanSearchMembers() != null && !info.getPlanSearchMembers().isEmpty()){
				List<PlanSearchMember> memList = info.getPlanSearchMembers();
				for (PlanSearchMember member: memList){
					FieldsValidator.validateString(invalidFields, member.getApplicationId());
					FieldsValidator.validateString(invalidFields, member.getCountyName());
					FieldsValidator.validateString(invalidFields, member.getDependentSeqNum());
					FieldsValidator.validateString(invalidFields, member.getFirstName());
					FieldsValidator.validateString(invalidFields, member.getGender());
					FieldsValidator.validateString(invalidFields, member.getLastName());
					FieldsValidator.validateString(invalidFields, member.getMiddleName());
					FieldsValidator.validateString(invalidFields, member.getRelationShipTypeDesc());
					FieldsValidator.validateString(invalidFields, member.getRelationShipTypeInd());
					FieldsValidator.validateString(invalidFields, member.getSubscriberId());
					FieldsValidator.validateString(invalidFields, member.getTobaccoUsageInd());
					FieldsValidator.validateString(invalidFields, member.getUserId());
					FieldsValidator.validateString(invalidFields, member.getZipcode());
				}
			}
			if (info.getDependents() != null && !info.getDependents().isEmpty()){
				// validate dependents
				for (Dependent dependent: info.getDependents()){
					FieldsValidator.validateString(invalidFields, dependent.getApplicationId());
					FieldsValidator.validateString(invalidFields, dependent.getDependentChangeTypeInd());
					FieldsValidator.validateString(invalidFields, dependent.getDependentFirstName());
					FieldsValidator.validateString(invalidFields, dependent.getDependentGender());
					FieldsValidator.validateString(invalidFields, dependent.getDependentLastName());
					FieldsValidator.validateString(invalidFields, dependent.getDependentMiddleName());
					FieldsValidator.validateString(invalidFields, dependent.getDependentRelationShipDesc());
					FieldsValidator.validateString(invalidFields, dependent.getDependentRelationShipTypeInd());
					FieldsValidator.validateString(invalidFields, dependent.getDependentSeqNum());
					FieldsValidator.validate(invalidFields, dependent.getDependentSSN());
					FieldsValidator.validateString(invalidFields, dependent.getDependentTobaccoUsageInd());
					FieldsValidator.validateString(invalidFields, dependent.getSubscriberId());
					FieldsValidator.validate(invalidFields, dependent.getUserId());
				}
				fieldsString = invalidFields.toString(); 
				if (invalidFields.length() > 0){
					logger.error("BenefitChangeInfoValidator: validateBenefitChangeInfo: invalidFields: [{}]", StringEscapeUtils.escapeJava(fieldsString));
				}
			}
		}catch (Exception e) {
			invalidFields.append(e.getMessage());
			logger.error("BenefitChangeInfoValidator: validateBenefitChangeInfo: Exception: [{}]", e.getMessage());
		}

		return invalidFields.toString();		
		
	}
	
	/**
	 * @objective This method will validate PlansRequest for invalid characters.
	 * @param plansRequest
	 * @return
	 */
	public static String validatePlansRequest(PlansRequest plansRequest){
		StringBuilder invalidFields = new StringBuilder();
		String invalidFieldsString = "";
		try{			
			if (plansRequest == null){
				invalidFields.append("PlansRequest");
				return invalidFields.toString();
			}
			
			FieldsValidator.validateString(invalidFields, plansRequest.getCounty());
			
			FieldsValidator.validateString(invalidFields, plansRequest.getGroupId());
			
			List<MemberInfo> memberInfoList = plansRequest.getMemberInfoList();
			if (memberInfoList != null && !memberInfoList.isEmpty()){
				for (MemberInfo info : memberInfoList){
					FieldsValidator.validateString(invalidFields, info.getGender());
					FieldsValidator.validateString(invalidFields, info.getGroupId());
					FieldsValidator.validateString(invalidFields, info.getMemberID());
					FieldsValidator.validateString(invalidFields, info.getRelationship());
				}
			}
			invalidFieldsString = invalidFields.toString();
			if (invalidFields.length() > 0){
				logger.error("BenefitChangeInfoValidator: validatePlansRequest: invalidFields: [{}]", StringEscapeUtils.escapeJava(invalidFieldsString));
			}	

		}catch(Exception e){
			logger.error("BenefitChangeInfoValidator: validatePlansRequest: Exception: [{}]", e.getMessage());
			invalidFields.append(e.getMessage());
		}

		return invalidFields.toString();
	}
	
	
	
	public static void main(String[] args){
		BenefitChangeInfo info = new BenefitChangeInfo();
		info.setEmailAddress("< ");
		info.setAction(" ");
		logger.info("Validation results[ "+ !BenefitChangeInfoValidator.validateBenefitChangeInfo(info).isEmpty()+ "]");
	}

}
