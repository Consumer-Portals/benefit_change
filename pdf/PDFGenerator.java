package com.bcbst.benefitchange.pdf;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;

import com.bcbst.applogger.core.ApplicationSettings;
import com.bcbst.benefitchange.dto.Address;
import com.bcbst.benefitchange.dto.BenefitChangeInfo;
import com.bcbst.benefitchange.dto.Dependent;
import com.bcbst.benefitchange.util.BenefitChangeException;
import com.bcbst.benefitchange.util.InvalidFileException;

/**
 * @objective This class has methods to generate files for OnDemand and Transform using templates and data. There are 2 Templates(IHBC-Original.pdf and IHBC-Merge.pdf) 
 *            on WebSphere template folder and if there are more than 3 dependents or more than one address then IHBC-Merge.pdf will be used
 *            or else IHBC-Original.pdf will be used as template.	 
 * 			  All these generated files will be stored in WebSphere folders OnDemand and Transform. Using MFT they will be moved to OnDemand and Transform servers.
 * 			  1. For OnDemand, it writes 3 files for every request.
 * 			  2. For Transform, it writes pdf file.  
 * @author Vijay Narsingoju 10/02/2015
 *
 */
public class PDFGenerator {
	
	private static final String SBSB_CB_GAIN_ACCESS_TO_HRA = "SBSB_CB_GAIN_ACCESS_TO_HRA";
	private static final String SBSB_CB_FOLLOW_RECEIPT = "SBSB_CB_FOLLOW_RECEIPT";
	private static final String SBSB_CB_STAND_EFF = "SBSB_CB_STAND_EFF";
	private static final String SBSB_CB_EVENT_DATE = "SBSB_CB_EVENT_DATE";
	private static final String SBSB_CB_FOLLOW_EVENT_DATE = "SBSB_CB_FOLLOW_EVENT_DATE";
	private static final String SBSB_CB_MARRIAGE = "SBSB_CB_MARRIAGE";
	private static final String SBSB_CB_LOSS_OF_DEP = "SBSB_CB_LOSS_OF_DEP";
	private static final String SBSB_CB_GAIN_DEP = "SBSB_CB_GAIN_DEP";
	private static final String SBSB_CB_BIRTH_ADOP_FOSTER = "SBSB_CB_BIRTH_ADOP_FOSTER";
	private static final String SBSB_CB_PERMANENT_MOVE = "SBSB_CB_PERMANENT_MOVE";
	private static final String SBSB_CB_OPEN_ENROLL = "SBSB_CB_OPEN_ENROLL";
	private static final String SBSB_CB_LOSS_OF_MIN = "SBSB_CB_LOSS_OF_MIN";
	private static final String SBSB_CB_ADDDEL = "SBSB_CB_ADDDEL";
	private static final String SBSB_CB_TERMLIFE = "SBSB_CB_TERMLIFE";
	private static final String SBSB_CB_CHANGE_NAME = "SBSB_CB_CHANGE_NAME";
	private static final String SBSB_CB_TERM_SUBSC = "SBSB_CB_TERM_SUBSC";
	private static final String SBSB_CB_BEN_OPTION = "SBSB_CB_BEN_OPTION";
	private static final String SBSB_TOB_CB_PRIM_APPL_REMOVE = "SBSB_TOB_CB_PRIM_APPL_REMOVE";
	private static final String SBSB_TOB_CB_PRIM_APPL_ADD = "SBSB_TOB_CB_PRIM_APPL_ADD";
	private static final String SBSB_TOB_CB_SPOUSE_REMOVE = "SBSB_TOB_CB_SPOUSE_REMOVE";
	private static final String SBSB_TOB_CB_SPOUSE_ADD = "SBSB_TOB_CB_SPOUSE_ADD";
	private static final String SBSB_CB_CHANGE_TOBACCO = "SBSB_CB_CHANGE_TOBACCO";
	private static final String SBSB_CB_DEN_DEL = "SBSB_CB_DEN_DEL";
	private static final String SBSB_CB_DEN_ADD = "SBSB_CB_DEN_ADD";
	private static final String SBSB_CB_DEN = "SBSB_CB_DEN";
	private static final String SBSB_CB_VIS_DEL = "SBSB_CB_VIS_DEL";
	private static final String SBSB_CB_VIS_ADD = "SBSB_CB_VIS_ADD";
	private static final String WASPDF_TEMPLATE_LOC = "WASPDFTemplateLoc";
	private static final String GROUP_FIELD_VALUE = "GROUP_FIELD_VALUE:";
	private static final String ARD_OUT = ".ARD.OUT";
	private static final String MEDICARE_SUFF = "_MEDICARE";
	private static final String NO = "No";
	private static final String TOBACCO_SUFF = "_TOBACCO";
	private static final String GENDER_SUFF = "_GENDER";
	private static final String MI_NAME_SUFF = "_MI_NAME";
	private static final String LAST_NAME_SUFF = "_LAST_NAME";
	private static final String FIRST_NAME_SUFF = "_FIRST_NAME";
	private static final String REL_SUFF = "_REL";
	private static final String DEP = "DEP";
	private static final String DEPENDENT_PREF = "Dependent_";
	private static final String VISION = "Vision";
	private static final String DENTAL = "Dental";
	private static final String TYPE_SUFF = "_TYPE";
	private static final String REASON = "REASON";
	private static final String CANCEL_SUFF = "_CANCEL";
	private static final String CANCEL_DATE_SUFF = "_CANCEL_DATE";
	private static final String POLICY = "POLICY";
	private static final String SBSB_CB_VISION = "SBSB_CB_Vision";
	private static final String DAYTIME_PHONE = "Daytime_Phone";
	private static final String SUBSCRIBER_DATEOF_BIRTH = "Subscriber_DateofBirth";
	private static final String SBSB_CB_VIS_EXAM_MATERIALS = "SBSB_CB_VIS_EXAM_MATERIALS";
	private static final String YES = "Yes";
	private static final String SBSB_CB_VIS_EXAM = "SBSB_CB_VIS_EXAM";
	private static final String SBSB_CB_ADD_DEL_ANC_PROD = "SBSB_CB_ADD_DEL_ANC_PROD";
	private static final String TERM_DEP = "TERM_DEP";
	private static final String SIGNATURE_X = "Signature_X";
	
	public static final String WASPDF_TRANSFORM_LOC = "WASPDFTransformLoc";
	public static final String BNFTCHGFORM = "BNFTCHGFORM-";
	public static final String ON_DEMAND_FILE_NAME_PREFIX = "OnDemandFileNamePrefix";
	public static final String WASPDF_ON_DEMAND_LOC = "WASPDFOnDemandLoc";
	public static final String ON_DEMAND_LOC = "OnDemandLoc";
	
	public static final String METADATA_EMAIL_RECIPIENT ="METADATA_EMAIL_RECIPIENT";
	public static final String METADATA_EMAIL_SENDER = "METADATA_EMAIL_SENDER";
	public static final String METADATA_EMAIL_SUBJECT = "METADATA_EMAIL_SUBJECT";
	public static final String METADATA_SMTP_SERVER = "METADATA_SMTP_SERVER";
	public static final String CONSUMER_PORTAL_SUPPORT_EMAIL = "EIT_ConsumerPortals_SupportTeam@bcbst.com";
	public static final String META_FILE_GEN_FAILED = "Metadata file creation Failed";
	public static final String SMTP_SERVER = "mail.bcbst.com";
	public static final String METADATA_BODY_MESSAGE = "METADATA_BODY_MESSAGE";
	public static final String ENABLE_TRIGGERING_EMAIL = "ENABLE_TRIGGERING_EMAIL";
	
	private Logger logger = LogManager.getLogger(PDFGenerator.class);
	
	
	/**
	 * @objective This method will invoke methods for generating files for ondemand and transform process.
	 * @param benefitChangeInfoObj
	 * @param applicationId
	 */
	public void submitRequestForPDFGeneration(BenefitChangeInfo benefitChangeInfoObj, String applicationId){
		PDFGenerator pdfGenerator = null;
		String destinationFile =null;
		String wasPDFOnDemandLoc = ApplicationSettings.getSettingValue(WASPDF_ON_DEMAND_LOC).trim();
		String onDemandFileNamePrefix = ApplicationSettings.getSettingValue(ON_DEMAND_FILE_NAME_PREFIX).trim();
		
		String ondemandFileName = wasPDFOnDemandLoc +  onDemandFileNamePrefix + applicationId+ "-"+ benefitChangeInfoObj.getSubscriberId() + ARD_OUT;
		String indexFilePath = wasPDFOnDemandLoc + onDemandFileNamePrefix+ applicationId+ "-"+ benefitChangeInfoObj.getSubscriberId() + ".ARD.IND";
		String odEmptyFilePath = wasPDFOnDemandLoc + onDemandFileNamePrefix+ applicationId+ "-"+ benefitChangeInfoObj.getSubscriberId() + ".ARD";
		try{
			pdfGenerator = new PDFGenerator();
			logger.debug("BenefitChangeServiceImpl: submitRequestForPDFGeneration(): action: {}", StringEscapeUtils.escapeJava(benefitChangeInfoObj.getAction()));
			if (benefitChangeInfoObj.getAction()!= null && benefitChangeInfoObj.getAction().trim().equalsIgnoreCase("Submit")){
				// PDF generation
				// decide which template to use based upon number of dependents and addresses.
				// If there are multiple addresses or more than 3 dependents then use IHBC-Merge.pdf which has actual pdf and overflow page.
				// or else use IHBC-Original.pdf. 
				String formType = ApplicationSettings.getSettingValue("IHBCForm").trim(); // default
				boolean overflowneeded = isOverFlowPageNeeded(benefitChangeInfoObj);
				if (overflowneeded){
					formType = ApplicationSettings.getSettingValue("IHBCMergeForm").trim();
				}
				logger.debug("BenefitChangeServiceImpl: submitRequestForPDFGeneration(): formType: {}", StringEscapeUtils.escapeJava(formType));
				// Generate PDF for transform	params(template, newPdfForTransform, data, ondemandpdfloc, overflowneeded)
				destinationFile = ApplicationSettings.getSettingValue(WASPDF_TRANSFORM_LOC).trim()+ BNFTCHGFORM+ applicationId+ "-"+ benefitChangeInfoObj.getSubscriberId() + ".pdf";
				pdfGenerator.generateIHBCPdf(
						ApplicationSettings.getSettingValue(WASPDF_TEMPLATE_LOC).trim()+formType,
							
							destinationFile,
									benefitChangeInfoObj,
											overflowneeded);

				// create file with .OUT extn for On-Demand. PDF file is the source for creating OUT file.
				pdfGenerator.createOUTFileForOnDemand(
						destinationFile,
						ondemandFileName);
			
				// create index files for On-Demand
				String groupfilename = ApplicationSettings.getSettingValue(ON_DEMAND_LOC).trim()+ onDemandFileNamePrefix+ applicationId+ "-"+ benefitChangeInfoObj.getSubscriberId() + ARD_OUT;
				pdfGenerator.createIndexFiles(benefitChangeInfoObj, 
									indexFilePath,
									groupfilename,
											odEmptyFilePath, 
											applicationId);
				Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
				File file = new  File(destinationFile);
				PDFFileValidator.isValidFile(file);
				if(file.exists()){
					logger.debug("{} destinationFile FILE EXISTS", StringEscapeUtils.escapeJava(file.toPath().toString()));
					Files.setPosixFilePermissions(file.toPath(), permissions);
				}else {
					logger.error("Cannot set permissions for file {}", StringEscapeUtils.escapeJava(file.toPath().toString()));
				}
				file = new  File(ondemandFileName);
				PDFFileValidator.isValidFile(file);
				if(file.exists()){
					logger.debug("{} ondemandFileName FILE EXISTS", StringEscapeUtils.escapeJava(file.toPath().toString()));
					Files.setPosixFilePermissions(file.toPath(), permissions);
				}else {
					logger.error("Cannot set permissions for file {}", StringEscapeUtils.escapeJava(file.toPath().toString()));
				}
				
				file = new  File(indexFilePath);
				PDFFileValidator.isValidFile(file);
				if(file.exists()){
					logger.debug("{} indexFilePath FILE EXISTS", StringEscapeUtils.escapeJava(file.toPath().toString()));
					Files.setPosixFilePermissions(file.toPath(), permissions);
				}else {
					logger.error("Cannot set permissions for file {}", StringEscapeUtils.escapeJava(file.toPath().toString()));
				}
				file = new  File(groupfilename);
//				PDFFileValidator.isValidFile(file);
				if(file.exists()){
					logger.debug("{} groupfilename FILE EXISTS", StringEscapeUtils.escapeJava(file.toPath().toString()));
					Files.setPosixFilePermissions(file.toPath(), permissions);
				}else {
					logger.error("Cannot set permissions for file {}", StringEscapeUtils.escapeJava(file.toPath().toString()));
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			String escapedAppId = StringEscapeUtils.escapeJava(applicationId);
				try{
					logger.error("PDFGenerator: Deleting files which were written for ondemand and transform process: applicationId: [{}]", escapedAppId);
					// Delete files and throw the exception back.
					// delete transform pdf
					pdfGenerator.deleteFile(destinationFile);
					
					// delete ondemand files
					// delete out file
					pdfGenerator.deleteFile(ondemandFileName);
					
					// delete index file
					pdfGenerator.deleteFile(indexFilePath);
					
					// delete empty file
					pdfGenerator.deleteFile(odEmptyFilePath);
				}catch (BenefitChangeException e1){
					logger.error("PDFGenerator: Deleting files encountered issue for: applicationId: [{}]. It may require manual deletion of files from WAS for this applicationId.", escapedAppId);
					logger.error(e1.getMessage(), e1);
					throw e1;
				}
				catch (Exception e1){
					logger.error("PDFGenerator: Deleting files encountered issue for: applicationId: [{}]. It may require manual deletion of files from WAS for this applicationId.", escapedAppId);
					logger.error(e1.getMessage(), e1);
					throw new BenefitChangeException(e1.getMessage());
				}
			
			throw new BenefitChangeException(e.getMessage());
		}
	
	}
	
	
	/**
	 * @objective This method will return type of form to be used based upon addresses count and dependents count.
	 * 				If dependents count > 3 or address count > 1 then merge form is used which includes overflow page.
	 * @param benefitChangeInfo
	 * @return
	 */
	private boolean isOverFlowPageNeeded(BenefitChangeInfo benefitChangeInfo){
		logger.debug("BenefitChangeServiceImpl: isOverFlowPageNeeded() invoked changes");
		boolean overflowneeded = false;
		boolean additionaladdress = false;
		boolean additionalOrTermDependents = false;
		boolean additionalcanceldates = false;
		
		try{
			List<Address> addressList = benefitChangeInfo.getAddresses();
			List<Dependent> dependentsList = benefitChangeInfo.getDependents();
			
			if (addressList != null && addressList.size() >=2){
				additionaladdress = true;
			}
			
			if (!additionaladdress){
				additionalOrTermDependents = isAdditionalOrTermDependent(dependentsList);
				// see if there is dental or vision cancellation's.
				if (benefitChangeInfo.getSubscriberDentalCancelDate() != null || benefitChangeInfo.getSubscriberVisionCancelDate() != null || benefitChangeInfo.getSubscriberPolicyCancelDate()!=null){
					additionalcanceldates = true;
				}
			}
					
			overflowneeded = additionaladdress || additionalOrTermDependents || additionalcanceldates;
					
		}catch(Exception e){
			logger.error(e.getMessage(), e); 
			throw new BenefitChangeException(e.getMessage());
		}
		logger.debug("BenefitChangeServiceImpl: isOverFlowPageNeeded() ended: {}", overflowneeded);
		return overflowneeded;
	}

	private boolean isAdditionalOrTermDependent(List<Dependent> dependentsList) {
		boolean	additionaldependents = false;
		boolean terminateDependents = false;
		int counter = 0;
		int spouseCount=0;
		
		if (dependentsList != null && !dependentsList.isEmpty()){
			for (Dependent dependent: dependentsList){// separate dependents based upon type and count.
				if ("P".equalsIgnoreCase(dependent.getDependentRelationShipTypeInd())){
					spouseCount++;
					logger.debug("BenefitChangeServiceImpl  : spouseCount:{}", spouseCount);
				}
				else {
					counter ++;
					logger.debug("BenefitChangeServiceImpl  : Counter:{}", counter);
				}
				if (counter >3 || spouseCount>1){
					additionaldependents = true;
					break;
				}		
				logger.debug("BenefitChangeServiceImpl -> dependent.getDependentChangeTypeInd(): {}", StringEscapeUtils.escapeJava(dependent.getDependentChangeTypeInd().trim()));
				terminateDependents = isTerminateDependents(dependent);
			}
		}
		
		return additionaldependents || terminateDependents;
	}

	private boolean isTerminateDependents(Dependent dependent) {
		boolean terminateDependents = false;
		
		if(dependent.getDependentChangeTypeInd().trim().equalsIgnoreCase("D")) {
			terminateDependents = true;
		}
		return terminateDependents;
	}
	
	
	/**
	 * @objective This method generates PDF document for change form data.
	 * @param sourcefile
	 * @param destinationfile
	 * @param benefitChangeInfo
	 * @author Vijay
	 */
	private void generateIHBCPdf(String sourcefile, String destinationfile,
			BenefitChangeInfo benefitChangeInfo, boolean overflowPageNeeded) {
			
		logger.debug("Begining: PDFGenerator generateIHBCPdf..");
		PDDocument document = null;
		try(InputStream is = new FileInputStream(sourcefile);BufferedInputStream bis = new BufferedInputStream(is);) {
				if (benefitChangeInfo != null){
					SimpleDateFormat dateFormatFull = new SimpleDateFormat("MMddyyyy");
					SimpleDateFormat dateFormatDDMM = new SimpleDateFormat("MMdd");
					SimpleDateFormat dateFormatYY = new SimpleDateFormat("yy");
					String subscriberId = StringEscapeUtils.escapeJava(benefitChangeInfo.getSubscriberId());
					logger.debug("PDFGenerator generateIHBCPdf..SubscriberId: {}", subscriberId);

					document = PDDocument.load(bis);
					PDDocumentCatalog  dc = document.getDocumentCatalog();

					PDAcroForm form = dc.getAcroForm();
					setValueToField(form, "Identification_Number", subscriberId);
					
					cancelMedical(benefitChangeInfo, dateFormatDDMM, dateFormatYY, form);

					setValueToField(form, "Subscriber_Cancel_Reason", benefitChangeInfo.getSubscriberMedicalCancelReason());
					setValueToField(form, "Email_Address", benefitChangeInfo.getEmailAddress());
					setValueToField(form, "Subscriber_First_Name", benefitChangeInfo.getSubscriberFirstName());
					setValueToField(form, "New_Subscriber_First_Name", benefitChangeInfo.getUpdatedSubscriberFirstName());
					setValueToField(form, "Subscriber_Last_Name", benefitChangeInfo.getSubscriberLastName());
								
					setValueToFieldIfEquals(form, "CB_SB_Change_My_Address", benefitChangeInfo.getChangeAddressind(), "Y", YES);
					
					setValueToField(form, "Reason_for_Name_Change_S", benefitChangeInfo.getSubscriberReasonForNameChange());
					
					setValueToFieldIfEquals(form, "CB_SBSB_Keep_DentalVision", benefitChangeInfo.getCancelDentalVisionInd(), "Y", YES);
					
					setValueToField(form, "Subscriber_MI_Name", benefitChangeInfo.getSubscriberMiddleName());

					setValueToFieldIfEquals(form, "CB_SBSB_Keep_DentalVision", benefitChangeInfo.getTermLifeCovInd(), "Y", YES);
					
					setValueToField(form, "Group_Number", benefitChangeInfo.getGroupId());
					setValueToField(form, "New_Subscriber_MI_Name", benefitChangeInfo.getUpdatedSubscriberMiddleName());
				
					//i48355v
					addSignature(benefitChangeInfo, form);
					
					form.getField("Relationship").setValue("Self");  // Always self
					
					setValueToField(form, DAYTIME_PHONE, benefitChangeInfo.getSubscriberDaytimePhone());
					
					addSubscriberDOB(benefitChangeInfo, dateFormatFull, form);
					
					setValueToField(form, "New_Subscriber_Last_Name", benefitChangeInfo.getUpdatedSubscriberLastName());
					
					setValueToFieldIfEquals(form, "SBSB_CB_Medicare_Eligibility", benefitChangeInfo.getTermSubscriberNewIdRsnCd(), "M", YES);
					setValueToFieldIfEquals(form, "SBSB_CB_Death", benefitChangeInfo.getTermSubscriberNewIdRsnCd(), "D", YES);
					setValueToFieldIfEquals(form, "SBSB_CB_EnrolledInGroupCoverage", benefitChangeInfo.getTermSubscriberNewIdRsnCd(), "E", YES);
					setValueToFieldIfEquals(form, "SBSB_CB_Other", benefitChangeInfo.getTermSubscriberNewIdRsnCd(), "O", YES);

					setValueToField(form, "Subscriber_Term_New_ID_For_Dep_Reason", benefitChangeInfo.getTermSubscriberNewIdRsnDesc());				
					setValueToField(form, "SBSB_BEN_PLAN_CODE", benefitChangeInfo.getBenifitPlan());				
					setValueToField(form, "SBSB_SUBSC_NETWORK", benefitChangeInfo.getBenefitNetwork());				
					
					
					setValueToFieldsIfEquals(form, new String[] {SBSB_CB_VIS_EXAM, SBSB_CB_ADD_DEL_ANC_PROD}, benefitChangeInfo.getSubscriberVisionExamOnlyInd(), "Y", YES);				
					setValueToFieldsIfEquals(form, new String[] {SBSB_CB_VIS_EXAM_MATERIALS, SBSB_CB_ADD_DEL_ANC_PROD}, benefitChangeInfo.getSubscriberVisExamMatrlsInd(), "Y", YES);
					
					logger.debug("VIS CHG IND: {} VIS DEL IND: {}", StringEscapeUtils.escapeJava(benefitChangeInfo.getSubscriberVisionChangeInd()), StringEscapeUtils.escapeJava(benefitChangeInfo.getVisionDelInd()));

					setValueToFielIfAllPropsErualToValue(form, new String[] {SBSB_CB_VISION, SBSB_CB_ADD_DEL_ANC_PROD}, new String[] {benefitChangeInfo.getSubscriberVisionChangeInd(), benefitChangeInfo.getVisionDelInd()}, "Y", YES);					
					
					// getSubscriberVisionChangeInd -> for adding vision
					setValueToFieldsIfEquals(form, new String[] {SBSB_CB_VIS_ADD, SBSB_CB_ADD_DEL_ANC_PROD}, benefitChangeInfo.getSubscriberVisionChangeInd(), "Y", YES);					
					setValueToFieldsIfEquals(form, new String[] {SBSB_CB_VIS_DEL, SBSB_CB_ADD_DEL_ANC_PROD}, benefitChangeInfo.getVisionDelInd(), "Y", YES);
					
					setValueToFielIfAllPropsErualToValue(form, new String[] {SBSB_CB_DEN, SBSB_CB_ADD_DEL_ANC_PROD}, new String[] {benefitChangeInfo.getDentalAddInd(), benefitChangeInfo.getDentalRemInd()}, "Y", YES);					
					
					setValueToFieldsIfEquals(form, new String[] {SBSB_CB_DEN_ADD, SBSB_CB_ADD_DEL_ANC_PROD}, benefitChangeInfo.getDentalAddInd(), "Y", YES);				
					setValueToFieldsIfEquals(form, new String[] {SBSB_CB_DEN_DEL, SBSB_CB_ADD_DEL_ANC_PROD}, benefitChangeInfo.getDentalRemInd(), "Y", YES);
					
					setValueToFielIfAllPropsNotNull(form, SBSB_CB_ADD_DEL_ANC_PROD, new Object[] {benefitChangeInfo.getSubscriberDentalCancelDate(), benefitChangeInfo.getSubscriberVisionCancelDate()}, YES);
					
					setValueToFieldIfEquals(form, SBSB_CB_CHANGE_TOBACCO, benefitChangeInfo.getTobaccoUsageInd(), "Y", YES);

					setValueToFieldIfEquals(form, SBSB_TOB_CB_SPOUSE_ADD, benefitChangeInfo.getTobaccoUsageSpouseInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_TOB_CB_SPOUSE_REMOVE, benefitChangeInfo.getTobaccoUsageSpouseInd(), "N", YES);
					
					setValueToFieldIfEquals(form, SBSB_TOB_CB_PRIM_APPL_ADD, benefitChangeInfo.getTobaccoUsageSubscriberInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_TOB_CB_PRIM_APPL_REMOVE, benefitChangeInfo.getTobaccoUsageSubscriberInd(), "N", YES);
										
					
					logger.debug("Medical Planname: {}", StringEscapeUtils.escapeJava(benefitChangeInfo.getMedicalPlanName()));
					if (benefitChangeInfo.getChangeBenInd() != null && benefitChangeInfo.getChangeBenInd().trim().equalsIgnoreCase("Y") && benefitChangeInfo.getMedicalPlanName()!=null){
						form.getField(SBSB_CB_BEN_OPTION).setValue(YES);
					}
					
					setValueToFieldIfEquals(form, SBSB_CB_TERM_SUBSC, benefitChangeInfo.getTermPolicyInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_CHANGE_NAME, benefitChangeInfo.getChangeNameInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_TERMLIFE, benefitChangeInfo.getTermLifeCovInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_ADDDEL, benefitChangeInfo.getAddRemDepInd(), "Y", YES);
														
					// Begin Fields for Phase -2
					addBenefitChangeEventDate(benefitChangeInfo, dateFormatDDMM, dateFormatYY, form);
					
					setValueToFieldIfEquals(form, SBSB_CB_LOSS_OF_MIN, benefitChangeInfo.getRsnLossOfMnHlthInd(), "Y", YES);				
					setValueToFieldIfEquals(form, SBSB_CB_OPEN_ENROLL, benefitChangeInfo.getRsnOpenEnrollInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_PERMANENT_MOVE, benefitChangeInfo.getRsnPermanentMoveInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_BIRTH_ADOP_FOSTER, benefitChangeInfo.getRsnBrthAdpStrCrInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_GAIN_DEP, benefitChangeInfo.getRsnGainDepInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_LOSS_OF_DEP, benefitChangeInfo.getRsnLossOfDepInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_MARRIAGE, benefitChangeInfo.getRsnMrgInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_GAIN_ACCESS_TO_HRA, benefitChangeInfo.getRsnAccessToICHRAInd(), "Y", YES);
					
					setValueToFieldIfEquals(form, SBSB_CB_FOLLOW_EVENT_DATE, benefitChangeInfo.getFirstMonthEffDateInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_EVENT_DATE, benefitChangeInfo.getEventDateInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_STAND_EFF, benefitChangeInfo.getStdEffGuidelinesInd(), "Y", YES);
					setValueToFieldIfEquals(form, SBSB_CB_FOLLOW_RECEIPT, benefitChangeInfo.getFirstDayMthFollowingSubmInd(), "Y", YES);
					
						
					// End Fields for Phase -2
					List<Address> addressList = benefitChangeInfo.getAddresses();
					List<Dependent> dependentsList = benefitChangeInfo.getDependents();
					List<Address> overFlowAddressList = null;
					
					// Fill PDF with address : R -> M -> B (as per priority)
					overFlowAddressList = fillPdfWithAddresses(form, addressList);
					
					Dependent spouse = null;
					List<Dependent> first3dependents = new ArrayList<>();
					List<Dependent> additionalDependents = new ArrayList<>();
					
					spouse = getSpouseAndFillDependents(dependentsList, first3dependents, additionalDependents);
					
					fillPdfWithSpouseInfo(dateFormatFull, form, spouse);
					
					fillPdfWithFirst3Dependents(dateFormatFull, form, first3dependents);					

					fillPdfWithTermDependents(form, dependentsList);
					
					// populate overflow page
					if (overflowPageNeeded){
						populateOverflowPage(benefitChangeInfo, overFlowAddressList, additionalDependents, form);
					}					
					
					logger.debug("generateIHBCPdf ended for SubscriberId: "+ subscriberId);

				}else{
					logger.error("PDFGenerator: benefitChangeInfo is empty");
				}
			}catch (BenefitChangeException e) {
					logger.error(e.getMessage(), e);
					throw e;
			}catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new BenefitChangeException(e.getMessage());
			} finally {
				try {
					if (document != null){
						document.save(destinationfile);
						document.close();
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}	
			

	}


	private Dependent getSpouseAndFillDependents(List<Dependent> dependentsList, List<Dependent> first3dependents, List<Dependent> additionalDependents) {
		int counter = 0;
		int spouseCounter=0;
		Dependent spouse = null;
		
		if (dependentsList != null && !dependentsList.isEmpty()){
			for (Dependent dependent: dependentsList){// separate dependents based upon type and count.
				logger.debug("PDFGenerator: dependents has data : Dependent RelationShipTypeInd: {}", StringEscapeUtils.escapeJava(dependent.getDependentRelationShipTypeInd()));
				//add first 3 dependents except spouse
				if (counter < 3) {
					if (("P").equalsIgnoreCase(dependent.getDependentRelationShipTypeInd())){
						if(spouseCounter<1){
							spouse = dependent;
						}
						else{
							additionalDependents.add(dependent);
						}
						spouseCounter++;
					}else{
						first3dependents.add(dependent);
						counter ++;
					}
				} else {
					additionalDependents.add(dependent);
					counter ++;
				}
					
			}
		}
		return spouse;
	}


	private void fillPdfWithFirst3Dependents(SimpleDateFormat dateFormatFull, PDAcroForm form,
			List<Dependent> first3dependents) throws IOException {
		int i = 1;
		if (first3dependents != null && !first3dependents.isEmpty()){
			logger.debug("PDFGenerator: first3dependents has data.");
			for(Dependent dependent: first3dependents){
				// dependent
					logger.debug("PDFGenerator: first3dependents index: " + i);
					setValueToFieldIfEquals(form, DEP+i+"_CB_ADOPTED", dependent.getDependentRelationShipTypeInd(), "A", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_NATURALCHILD", dependent.getDependentRelationShipTypeInd(), "N", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_OTHER", dependent.getDependentRelationShipTypeInd(), "O", YES);						
					setValueToField(form, DEPENDENT_PREF+i+"_Other_Specify", dependent.getDependentRelationShipDesc());				
					
					setValueToField(form, DEPENDENT_PREF+i+"_SSNTIN", dependent.getDependentSSN());				
					addDependentDOB(dateFormatFull, form, i, dependent);
					
					setValueToField(form, DEPENDENT_PREF+i+"_First_Name", dependent.getDependentFirstName());				
					setValueToField(form, DEPENDENT_PREF+i+"_Last_Name", dependent.getDependentLastName());				
					setValueToField(form, DEPENDENT_PREF+i+"_Middle_Name", dependent.getDependentMiddleName());				
					
					setValueToFieldIfEquals(form, DEP+i+"_CB_MALE", dependent.getDependentGender(), "M", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_FEMALE", dependent.getDependentGender(), "F", YES);						
					
					setValueToFieldIfEquals(form, DEP+i+"_CB_TOBACCO_YES", dependent.getDependentTobaccoUsageInd(), "Y", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_TOBACCO_NO", dependent.getDependentTobaccoUsageInd(), "N", YES);						
					
					setValueToFieldIfEquals(form, DEP+i+"_CB_MEDICARE_YES", dependent.getDependentMedicareInd(), "Y", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_MEDICARE_NO", dependent.getDependentMedicareInd(), "N", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_ADD", dependent.getDependentChangeTypeInd(), "A", YES);						
					setValueToFieldIfEquals(form, DEP+i+"_CB_DELETE", dependent.getDependentChangeTypeInd(), "D", YES);						

					i++;
				}		
			
		}
	}


	private void fillPdfWithTermDependents(PDAcroForm form, List<Dependent> dependentsList) throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		int remDepIdx = 1;
		
		if (dependentsList != null && !dependentsList.isEmpty()){
			for (int idx=0;idx<dependentsList.size();idx++){
				Dependent dependent = dependentsList.get(idx);		
				if(dependent.getDependentChangeTypeInd().trim().equalsIgnoreCase("D")) {							
					setValueToField(form, TERM_DEP+remDepIdx+FIRST_NAME_SUFF, dependent.getDependentFirstName());				
					setValueToField(form, TERM_DEP+remDepIdx+LAST_NAME_SUFF, dependent.getDependentLastName());				
					setValueToField(form, TERM_DEP+remDepIdx+MI_NAME_SUFF, dependent.getDependentMiddleName());				
					if (dependent.getDependentDateOfBirth() != null){
						form.getField(TERM_DEP+remDepIdx+"_DOB").setValue(dateFormat.format(dependent.getDependentDateOfBirth()));
					}							
					if (dependent.getDependentTermDate() != null){
						form.getField(TERM_DEP+remDepIdx+"_TERM_DATE").setValue(dateFormat.format(dependent.getDependentTermDate()));
					}
					setValueToField(form, TERM_DEP+remDepIdx+"_TERM_REASON", dependent.getDependentTermReason());				
					
					remDepIdx++;
				}
			}
		}
	}


	private void fillPdfWithSpouseInfo(SimpleDateFormat dateFormatFull, PDAcroForm form, Dependent spouse)
			throws IOException {
		if (spouse != null){
			logger.debug("PDFGenerator: spouse has data.");
			// populate spouse details.
			addSpouseDOB(dateFormatFull, form, spouse);
				
			setValueToField(form, "Spouse_SSNTIN", spouse.getDependentSSN());				
			setValueToField(form, "Spouse_First_Name", spouse.getDependentFirstName());				
			setValueToField(form, "Spouse_Last_Name", spouse.getDependentLastName());																		
			setValueToField(form, "Spouse_Middle_Name", spouse.getDependentMiddleName());				
				
			setValueToFieldIfEquals(form, "SPOUSE_CB_MALE_YES", spouse.getDependentGender(), "M", YES);
			setValueToFieldIfEquals(form, "SPOUSE_CB_FEMALE_YES", spouse.getDependentGender(), "F", YES);
				
			setValueToFieldIfEquals(form, "SPOUSE_CB_TOBACCO_YES", spouse.getDependentTobaccoUsageInd(), "Y", YES);
			setValueToFieldIfEquals(form, "SPOUSE_CB_TOBACCO_NO", spouse.getDependentTobaccoUsageInd(), "N", YES);
				
			setValueToFieldIfEquals(form, "SPOUSE_CB_MEDICARE_YES", spouse.getDependentMedicareInd(), "Y", YES);
			setValueToFieldIfEquals(form, "SPOUSE_CB_MEDICARE_NO", spouse.getDependentMedicareInd(), "N", YES);

			setValueToFieldIfEquals(form, "SPOUSE_CB_ADD", spouse.getDependentChangeTypeInd(), "A", YES);
			setValueToFieldIfEquals(form, "SPOUSE_CB_DELETE", spouse.getDependentChangeTypeInd(), "D", YES);						
		}
	}


	private List<Address> fillPdfWithAddresses(PDAcroForm form, List<Address> addressList) {
		boolean residenceAddressAdded = false;
		boolean mailingAddressAdded = false; 
		boolean billingAddressAdded = false;
		
		List<Address> overflowAddressList = new ArrayList<>();

		if (addressList != null && !addressList.isEmpty()){
			logger.debug("PDFGenerator: addressList has data.");
			// main pdf needs to have all the indicators.
			populatePDFWithAddressIndicator(addressList, form);
			residenceAddressAdded = populatePDFWithAddress(addressList, form, "R");
			logger.debug("residenceAddressAdded["+residenceAddressAdded+"]");
			if (!residenceAddressAdded){
				mailingAddressAdded = populatePDFWithAddress(addressList, form, "M");
				logger.debug("mailingAddressAdded["+mailingAddressAdded+"]");
			}
			if (!residenceAddressAdded && !mailingAddressAdded){
				billingAddressAdded = populatePDFWithAddress(addressList, form, "B");
				logger.debug("billingAddressAdded["+billingAddressAdded+"]");
			}
			
			if (addressList.size() > 1){// additional address should go on overflow page
				overflowAddressList = fillOverflowPdfWithAddresses(addressList, mailingAddressAdded, billingAddressAdded);			
			}
		}

		return overflowAddressList;
	}


	private List<Address> fillOverflowPdfWithAddresses(List<Address> addressList, boolean mailingAddressAdded,
			boolean billingAddressAdded) {
			List<Address> overFlowAddressList = new ArrayList<>();
			for(Address address: addressList){
				if(!address.getSubscriberAddressInd().equalsIgnoreCase("R")){
					if (!mailingAddressAdded && address.getSubscriberAddressInd().equalsIgnoreCase("M")){
						// add mailing as it was not added to original form
						overFlowAddressList.add(address);
					}
					 if (!billingAddressAdded  && address.getSubscriberAddressInd().equalsIgnoreCase("B")){
						// add billing as it was not added to original form
						overFlowAddressList.add(address);
					}
					
				}
			}

			return overFlowAddressList;
	}


	private void addSignature(BenefitChangeInfo benefitChangeInfo, PDAcroForm form) throws IOException {
		SimpleDateFormat dateFormatYY = new SimpleDateFormat("yy");
		SimpleDateFormat dateFormatDDMM = new SimpleDateFormat("MMdd");
		Date currentDate = Calendar.getInstance().getTime();

		if(benefitChangeInfo.getUserType()==null || benefitChangeInfo.getUserType().equalsIgnoreCase("M")) {
			String fullName = "\"Electronic Signature\"" +" "+ benefitChangeInfo.getSubscriberFirstName() + " " + benefitChangeInfo.getSubscriberLastName();
			form.getField(SIGNATURE_X).setValue(fullName);
		}else if(benefitChangeInfo.getUserType().equalsIgnoreCase("I")) {
			String fullName = "Telephonic" +" - "+ benefitChangeInfo.getSubscriberFirstName() + " " + benefitChangeInfo.getSubscriberLastName();
			form.getField(SIGNATURE_X).setValue(fullName);

			String agentSignature = benefitChangeInfo.getUserId();
			form.getField("Agents Signature X").setValue(agentSignature);
		}else if(benefitChangeInfo.getUserType().equalsIgnoreCase("B")) {
			logger.info("User type is 'Broker'");
			
			String fullName = benefitChangeInfo.getSubscriberFirstName() + " " + benefitChangeInfo.getSubscriberLastName();
			form.getField(SIGNATURE_X).setValue(fullName);

			String agentSignature = benefitChangeInfo.getRepresentativeName();
			form.getField("Agents Signature X").setValue(agentSignature);

			String agentID = benefitChangeInfo.getRepId();
			form.getField("Date mmddyyyy").setValue(agentID);

			form.getField("Agent_Signature_DDMM").setValue(dateFormatDDMM.format(currentDate));
			form.getField("Agent_Signature_YY").setValue(dateFormatYY.format(currentDate));

			String agentEmail = benefitChangeInfo.getRepresentativeEmail();
			form.getField("Agents ID").setValue(agentEmail);
			
			logger.info("User's name is : {}", StringEscapeUtils.escapeJava(benefitChangeInfo.getRepresentativeName()));

			String agentName = benefitChangeInfo.getRepresentativeName();
			form.getField("Agents_Name").setValue(agentName);
		}
		
		try{
			form.getField("Signature_DDMM").setValue(dateFormatDDMM.format(currentDate));
			form.getField("Signature_YY").setValue(dateFormatYY.format(currentDate));
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}


	private void setValueToFieldIfEquals(PDAcroForm form, String fieldName, String value, String eqValue, String setValue) throws IOException {
		if (value != null && value.trim().equalsIgnoreCase(eqValue)){
			form.getField(fieldName).setValue(setValue);
		}
		
	}

	private void setValueToFieldsIfEquals(PDAcroForm form, String[] fieldsName, String value, String eqValue, String setValue) throws IOException {
		if (value != null && value.trim().equalsIgnoreCase(eqValue)){
			for(String fieldName : fieldsName) {
				form.getField(fieldName).setValue(setValue);
			}
		}
		
	}
	
	private void setValueToFielIfAllPropsErualToValue(PDAcroForm form, String[] fieldsName, String[] beanProperties, String eqValue, String setValue) throws IOException {
		boolean allPropsNotNullAndEqToValue = true;

		for(String str : beanProperties) {
			if(str==null || !str.trim().equals(eqValue)) {
				allPropsNotNullAndEqToValue = false;
				
				break;
			}
		}		
		
		if(allPropsNotNullAndEqToValue) {
			for(String fieldName : fieldsName) {
				form.getField(fieldName).setValue(setValue);
			}
		}
	}
	

	private void addDependentDOB(SimpleDateFormat dateFormatFull, PDAcroForm form, int i, Dependent dependent) {
		try{
			if (dependent.getDependentDateOfBirth() != null){
				form.getField(DEPENDENT_PREF+i+"_DOB").setValue(dateFormatFull.format(dependent.getDependentDateOfBirth()));
			}
		}catch(Exception e){
			logger.error(e);
		}
	}

	private void addSpouseDOB(SimpleDateFormat dateFormatFull, PDAcroForm form, Dependent spouse) {
		try{
			if (spouse.getDependentDateOfBirth() != null){
				form.getField("Spouse_DOB").setValue(dateFormatFull.format(spouse.getDependentDateOfBirth()));
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}


	private void addBenefitChangeEventDate(BenefitChangeInfo benefitChangeInfo, SimpleDateFormat dateFormatDDMM,
			SimpleDateFormat dateFormatYY, PDAcroForm form) {
		if (benefitChangeInfo.getBenefitChangeEventDate() != null){
			try{
				form.getField("Subsc_Event_Date_mmdd").setValue(dateFormatDDMM.format(benefitChangeInfo.getBenefitChangeEventDate()));
				form.getField("Subsc_Event_Date_yy").setValue(dateFormatYY.format(benefitChangeInfo.getBenefitChangeEventDate()));
				
			}catch(Exception e){
				logger.error("",e);
			}
		}
	}


	private void addSubscriberDOB(BenefitChangeInfo benefitChangeInfo, SimpleDateFormat dateFormatFull,
			PDAcroForm form) {
		if (benefitChangeInfo.getSubscriberDateOfBirth() != null){
			try{
				form.getField(SUBSCRIBER_DATEOF_BIRTH).setValue(dateFormatFull.format(benefitChangeInfo.getSubscriberDateOfBirth()));
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
	}

	private void cancelMedical(BenefitChangeInfo benefitChangeInfo, SimpleDateFormat dateFormatDDMM,
			SimpleDateFormat dateFormatYY, PDAcroForm form) {
		if (benefitChangeInfo.getSubscriberMedicalCancelDate() != null){
			try{
				form.getField("CB_SB_Cancel_Date").setValue(YES);
				form.getField("Subscriber_Cancel_Policy_Date").setValue(dateFormatDDMM.format(benefitChangeInfo.getSubscriberMedicalCancelDate()));
				form.getField("Subscriber_Cancel_Policy_YY").setValue(dateFormatYY.format(benefitChangeInfo.getSubscriberMedicalCancelDate()));
				
			}catch(Exception e){
				logger.error("", e);
			}
		}
	}
	
	/**
	 * This method will populate overflow page with additional address or dependents
	 * @param addressList
	 * @param dependentsList
	 * @param form
	 */
	private void populateOverflowPage(BenefitChangeInfo benefitChangeInfo, List<Address> addressList, List<Dependent> dependentsList, PDAcroForm form){
		logger.debug("populateOverflowPage() invoked for :{}", StringEscapeUtils.escapeJava(benefitChangeInfo.getSubscriberId()));
		try{
			setValueToField(form, "SBSB_ID_O", benefitChangeInfo.getSubscriberId());
			setValueToField(form, "SBSB_FIRST_NAME_O", benefitChangeInfo.getSubscriberFirstName());
			setValueToField(form, "SBSB_LAST_NAME_O", benefitChangeInfo.getSubscriberLastName());
			setValueToField(form, "SBSB_MIDDLE_NAME_O", benefitChangeInfo.getSubscriberMiddleName());
				
			if (addressList != null && !addressList.isEmpty()) {
				int i = 1;
				for (Address address: addressList){
					setValueToField(form, "STREET_"+i, address.getStreetName());
					setValueToField(form, "CITY_"+i, address.getCityName());
					setValueToField(form, "STATE_"+i, address.getStatename());
					setValueToField(form, "ZIP_"+i, address.getZipCode());

					setValueToFieldIfEquals(form, "TYPE_"+i, address.getSubscriberAddressInd(), "M", "Mailing");
					setValueToFieldIfEquals(form, "TYPE_"+i, address.getSubscriberAddressInd(), "B", "Billing");

					i++;
			
				}
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			// dependents.
			int i = 4;
			if (dependentsList != null && !dependentsList.isEmpty()){
				for(Dependent dependent: dependentsList){
					// dependent
						if (dependent.getDependentRelationShipTypeInd() != null){
							setValueToFieldIfEquals(form, DEP+i+REL_SUFF, dependent.getDependentRelationShipTypeInd(), "A", "Adopted");
							setValueToFieldIfEquals(form, DEP+i+REL_SUFF, dependent.getDependentRelationShipTypeInd(), "N", "Natural Child");
							setValueToFieldIfEquals(form, DEP+i+REL_SUFF, dependent.getDependentRelationShipTypeInd(), "O", dependent.getDependentRelationShipDesc());
							setValueToFieldIfEquals(form, DEP+i+REL_SUFF, dependent.getDependentRelationShipTypeInd(), "P", "Spouse");
						}
					    
						setValueToField(form, DEP+i+"_SSN", dependent.getDependentSSN());

						if (dependent.getDependentDateOfBirth() != null){
							form.getField(DEP+i+"_DOB").setValue(dateFormat.format(dependent.getDependentDateOfBirth()));
						}
						
						setValueToField(form, DEP+i+FIRST_NAME_SUFF, dependent.getDependentFirstName());
						setValueToField(form, DEP+i+LAST_NAME_SUFF, dependent.getDependentLastName());
						setValueToField(form, DEP+i+MI_NAME_SUFF, dependent.getDependentMiddleName());

						setValueToFieldIfEquals(form, DEP+i+GENDER_SUFF, dependent.getDependentGender(), "M", "Male");
						setValueToFieldIfEquals(form, DEP+i+GENDER_SUFF, dependent.getDependentGender(), "F", "Female");

						setValueToFieldIfEquals(form, DEP+i+TOBACCO_SUFF, dependent.getDependentTobaccoUsageInd(), "Y", YES);
						setValueToFieldIfEquals(form, DEP+i+TOBACCO_SUFF, dependent.getDependentTobaccoUsageInd(), "N", NO);
							
						setValueToFieldIfEquals(form, DEP+i+MEDICARE_SUFF, dependent.getDependentMedicareInd(), "Y", YES);
						setValueToFieldIfEquals(form, DEP+i+MEDICARE_SUFF, dependent.getDependentMedicareInd(), "N", NO);
					
						i++;
					}
					
				
			}
			
			// populate cancellation dates/reason
			
			populateCancellation(benefitChangeInfo, form, dateFormat);			
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		}
		logger.debug("populateOverflowPage() ended for :{}", StringEscapeUtils.escapeJava(benefitChangeInfo.getSubscriberId()));
	}


	private void populateCancellation(BenefitChangeInfo benefitChangeInfo, PDAcroForm form, SimpleDateFormat dateFormat)
			throws IOException {
		int count=0;
		if (benefitChangeInfo.getSubscriberDentalCancelDate() != null){
			count++;
			addSubscriberDentalCancelDate(benefitChangeInfo, form, dateFormat, count);
			
			setValueToField(form, REASON+count+CANCEL_SUFF, benefitChangeInfo.getSubscriberDentalCancelReason());				
			setValueToFielIfAllPropsNotNull(form, POLICY+count+TYPE_SUFF, new Object[] {benefitChangeInfo.getSubscriberDentalCancelReason()}, DENTAL);
		}
		
		if (benefitChangeInfo.getSubscriberVisionCancelDate() != null){
			count++;
			addSubscriberVisionCancelDate(benefitChangeInfo, form, dateFormat, count);
			
			setValueToField(form, REASON+count+CANCEL_SUFF, benefitChangeInfo.getSubscriberVisionCancelReason());								
			setValueToFielIfAllPropsNotNull(form, POLICY+count+TYPE_SUFF, new Object[] {benefitChangeInfo.getSubscriberVisionCancelReason()}, VISION);
		}
		
		if (benefitChangeInfo.getSubscriberPolicyCancelDate() != null){
			count++;
			addSubscriberPolicyCancelDate(benefitChangeInfo, form, dateFormat, count);			
		}
	}


	private void setValueToFielIfAllPropsNotNull(PDAcroForm form, String fieldName, Object[] beanProperties, String value) throws IOException {
		boolean allPropsNotNull = true;

		for(Object obj : beanProperties) {
			if(obj==null) {
				allPropsNotNull = false;
				
				break;
			}
		}		
		
		if(allPropsNotNull) {
			form.getField(fieldName).setValue(value);
		}
	}


	private void setValueToField(PDAcroForm form, String fieldName, String beanProperty) throws IOException {
		if (beanProperty != null){ 
			form.getField(fieldName).setValue(beanProperty);
		}
	}


	private void addSubscriberDentalCancelDate(BenefitChangeInfo benefitChangeInfo, PDAcroForm form,
			SimpleDateFormat dateFormat, int count) {
		try{
			form.getField(POLICY+count+CANCEL_DATE_SUFF).setValue(dateFormat.format(benefitChangeInfo.getSubscriberDentalCancelDate()));
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}


	private void addSubscriberVisionCancelDate(BenefitChangeInfo benefitChangeInfo, PDAcroForm form,
			SimpleDateFormat dateFormat, int count) {
		try{
			form.getField(POLICY+count+CANCEL_DATE_SUFF).setValue(dateFormat.format(benefitChangeInfo.getSubscriberVisionCancelDate()));
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}


	private void addSubscriberPolicyCancelDate(BenefitChangeInfo benefitChangeInfo, PDAcroForm form,
			SimpleDateFormat dateFormat, int count) throws IOException {
		try{
			form.getField(POLICY+count+CANCEL_DATE_SUFF).setValue(dateFormat.format(benefitChangeInfo.getSubscriberPolicyCancelDate()));
			if (benefitChangeInfo.getTermSubscriberNewIdRsnCd() != null){
				if (benefitChangeInfo.getTermSubscriberNewIdRsnCd().trim().equalsIgnoreCase("M")){
					form.getField(REASON+count+CANCEL_SUFF).setValue("Medicare Eligibility");						
				}
				else if (benefitChangeInfo.getTermSubscriberNewIdRsnCd().trim().equalsIgnoreCase("D")){
					form.getField(REASON+count+CANCEL_SUFF).setValue("Death");
				}
				else if (benefitChangeInfo.getTermSubscriberNewIdRsnCd().trim().equalsIgnoreCase("E")){
					form.getField(REASON+count+CANCEL_SUFF).setValue("Enrolled in Group coverage");
				}
				else if (benefitChangeInfo.getTermSubscriberNewIdRsnCd().trim().equalsIgnoreCase("O")){
					if (benefitChangeInfo.getTermSubscriberNewIdRsnDesc() != null){
						form.getField(REASON+count+CANCEL_SUFF).setValue(benefitChangeInfo.getTermSubscriberNewIdRsnDesc().trim());
						
					}else{
						form.getField(REASON+count+CANCEL_SUFF).setValue("Other");
					}
					
				}
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
					
		form.getField(POLICY+count+TYPE_SUFF).setValue("Term Subscriber");
	}
	
	
	/**
	 * @objective This method will populate pdf with address.
	 * @param addressList
	 * @param form
	 * @param addressType
	 * @return
	 * @author Vijay
	 */
	private boolean populatePDFWithAddress(List<Address> addressList, PDAcroForm form, String addressType){
		boolean addressFound= false;
		try {
			if(addressList != null){
				for (Address address: addressList){
					if (address.getSubscriberAddressInd()!=null && address.getSubscriberAddressInd().trim().equalsIgnoreCase(addressType)){
						populateAddressToForm(form, address);
						addressFound = true;
						break;
					}
			}
		}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return addressFound;
	}


	private void populateAddressToForm(PDAcroForm form, Address address) throws IOException {
		if (address.getStreetName()!=null){
			form.getField("Street").setValue(address.getStreetName());
		}
		if (address.getCityName() != null){
			form.getField("City").setValue(address.getCityName());
		}
		if (address.getCountyName() != null){
			form.getField("County").setValue(address.getCountyName());
		}
		if (address.getStatename() != null){
			form.getField("State").setValue(address.getStatename());
		}
		if (address.getZipCode() != null){
			form.getField("Zip").setValue(address.getZipCode());
		}
	}
	
	/**
	 * @objective This method will populate all the address indicators in pdf.
	 * @param addressList
	 * @param form
	 * @author Vijay
	 */
	private void populatePDFWithAddressIndicator(List<Address> addressList, PDAcroForm form){
		
		try {
			for (Address address: addressList){
				if (address.getSubscriberAddressInd()!=null){
					if (address.getSubscriberAddressInd().trim().equalsIgnoreCase("R")){
						form.getField("SBSB_CB_RESIDENCE").setValue(YES);
					}
					if (address.getSubscriberAddressInd().trim().equalsIgnoreCase("M")){
						form.getField("SBSB_CB_MAILING").setValue(YES);
					}
					if (address.getSubscriberAddressInd().trim().equalsIgnoreCase("B")){
						form.getField("SBSB_CB_BILLING").setValue(YES);
					}					
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}		
	}
	
	/**
	 * @objective This method will copy pdf file to file with extension which ondemand needs it.
	 * @param transformFileName
	 * @param ondemandFileName
	 * @author Vijay
	 */
	private void createOUTFileForOnDemand(String transformFileName, String ondemandFileName){
		File transformfile = null;
		File odfile = null;
		try {
			transformfile = new File(transformFileName);
			odfile = new File(ondemandFileName);
			
			PDFFileValidator.isValidFile(transformfile, ApplicationSettings.getSettingValue(WASPDF_TRANSFORM_LOC), BNFTCHGFORM, "pdf");
			PDFFileValidator.isValidFile(odfile, ApplicationSettings.getSettingValue(WASPDF_ON_DEMAND_LOC), ApplicationSettings.getSettingValue(ON_DEMAND_FILE_NAME_PREFIX).trim(), "OUT");
			
			logger.debug("Copying transform file to ondemand file");
			logger.debug("Transformed file name: {}", StringEscapeUtils.escapeJava(transformFileName));
			logger.debug("Ondemand file name: {}", StringEscapeUtils.escapeJava(ondemandFileName));

			FileUtils.copyFile(transformfile, odfile);

			logger.debug("Transformed file size: " + FileUtils.sizeOf(transformfile));
			logger.debug("Ondemand file size: " + FileUtils.sizeOf(odfile));
			
			logger.debug("File copied.");
			logger.debug("createIndexFile() filepath: " + new File(ondemandFileName).length());		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		}
	}
	
	/**
	 * @objective This method will write contents to index file.
	 * @param benefitChangeInfo
	 * @param filepath
	 * @param groupfilename
	 * @param emptyFilePath
	 * @param applicationId
	 * @author Vijay
	 */
	private void createIndexFiles(BenefitChangeInfo benefitChangeInfo, String filepath, String groupfilename, String emptyFilePath, String applicationId){

		logger.error("createIndexFile() begin: applicationId: {}", StringEscapeUtils.escapeJava(applicationId));
		logger.error("createIndexFile() filepath: {}", StringEscapeUtils.escapeJava(filepath));
		logger.error("createIndexFile() groupfilename: {}", StringEscapeUtils.escapeJava(groupfilename));
		logger.error("createIndexFile() emptyFilePath: {}", StringEscapeUtils.escapeJava(emptyFilePath));

		boolean flagToSendMail = false;
		String currentdate = null;
		File file = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			currentdate = dateFormat.format(Calendar.getInstance().getTime());
			file = new File(filepath);
			flagToSendMail = !PDFFileValidator.isValidFile(file);
		}
		catch(Exception e) {
			logger.error("{} SubscriberId: {}", e.getMessage(), StringEscapeUtils.escapeJava(benefitChangeInfo.getSubscriberId()), e);
			flagToSendMail = true;
		}
		
		try (Writer fileWriter = new FileWriter(file); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			StringBuilder emailBody = new StringBuilder();
			prepareMetaData(benefitChangeInfo, filepath, groupfilename, emptyFilePath, applicationId, currentdate, emailBody);
            bufferedWriter.write(emailBody.toString());
			createEmptyFile(emptyFilePath); // for creating empty file
		} catch (Exception e) {
			flagToSendMail = true;
        	logger.error("{} SubscriberId: {}", e.getMessage(), StringEscapeUtils.escapeJava(benefitChangeInfo.getSubscriberId()), e);
        } finally {
			logger.debug("createIndexFile() filepath: " + new File(filepath).length());
			logger.debug("createIndexFile() groupfilename: " + new File(groupfilename).length());
			logger.debug("createIndexFile() emptyFilePath: " + new File(emptyFilePath).length());
        }
		
		String enableTriggeringEmail =  ApplicationSettings.getSettingValue(ENABLE_TRIGGERING_EMAIL);
		enableTriggeringEmail = (enableTriggeringEmail == null) ? "0" : enableTriggeringEmail;
		logger.debug("flagToSendMail : " + flagToSendMail + "  enableTriggeringEmail : " + enableTriggeringEmail);
		if (flagToSendMail || "1".equals(enableTriggeringEmail)) {
			StringBuilder emailBody = new StringBuilder();
			prepareEmailHeader(emailBody);
			prepareMetaData(benefitChangeInfo, filepath, groupfilename, emptyFilePath, applicationId, currentdate, emailBody);
			prepareEmailFooter(emailBody);
			sendEmailWithMetaData(emailBody.toString());
		}
		
		logger.debug("PDFGenerator : createIndexFile() ended");		
	    
	}
	
	/**
	 * @objective This method is for creating empty file as part of on-demand index files.
	 * @param filepath
	 * @author Vijay
	 * @throws IOException 
	 */
	private void createEmptyFile(String filepath) throws IOException, BenefitChangeException{
		logger.debug("createEmptyFile invoked: {}", StringEscapeUtils.escapeJava(filepath));
		boolean emptyfilecreated = false;
		try
		{
			File file = null;
			file = new File(filepath);
			PDFFileValidator.isValidFile(file);
			emptyfilecreated = file.createNewFile();
			logger.debug("createEmptyFile: empty file created: "+ emptyfilecreated);
		}
		catch(InvalidFileException e)
		{
			logger.debug("createEmptyFile: empty file created: "+ emptyfilecreated);
			throw new BenefitChangeException(e.getMessage());
		}
	}
	
	/**
	 * @objective This method deletes file
	 * @param filename
	 * @return
	 */
	public boolean deleteFile(String filename){
		boolean status = false;
		filename = StringEscapeUtils.escapeJava(filename);
		try {
			logger.error("PDFGenerator: deleteFile invoked for file: [{}]", filename);
			File f = new File(filename);
			PDFFileValidator.isValidFile(f);
			status = Files.deleteIfExists(f.toPath());
			logger.error("PDFGenerator: deleted file: [{}]", filename);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		}
		return status;
	}
	
	private StringBuilder prepareMetaData(BenefitChangeInfo benefitChangeInfo, String filepath, String groupfilename,
			String emptyFilePath, String applicationId, String currentdate, StringBuilder emailBody) {

		String lineSeperator = System.getProperty("line.separator");
		emailBody.append("CODEPAGE:819").append(lineSeperator)
					.append("GROUP_FIELD_NAME:APP_ID").append(lineSeperator)
					.append(GROUP_FIELD_VALUE).append(applicationId).append(lineSeperator)
					.append("GROUP_FIELD_NAME:SBSB_ID").append(lineSeperator)
					.append(GROUP_FIELD_VALUE).append(benefitChangeInfo.getSubscriberId()).append(lineSeperator)
					.append("GROUP_FIELD_NAME:SUBMITTED_DATE").append(lineSeperator)
					.append(GROUP_FIELD_VALUE).append(currentdate).append(lineSeperator)
					.append("GROUP_OFFSET:0").append(lineSeperator)
					.append("GROUP_LENGTH:0").append(lineSeperator)
					.append("GROUP_FILENAME:" + groupfilename);
		return emailBody;
	}
	
	private StringBuilder prepareEmailHeader(StringBuilder emailBody) {
		String lineSeperator = System.getProperty("line.separator");
		String bodyMessage = ApplicationSettings.getSettingValue(METADATA_BODY_MESSAGE);
		bodyMessage = (bodyMessage == null)
				? "IHBC OnDemand load process failed for application submission. Please manually load files to OnDemand."
				: bodyMessage;
		emailBody.append(lineSeperator).append(lineSeperator).append(bodyMessage).append(lineSeperator)
				.append(lineSeperator);
		return emailBody;
	}	
	
	private StringBuilder prepareEmailFooter(StringBuilder emailBody) {
		String lineSeperator = System.getProperty("line.separator");
		String emailFromAddress = ApplicationSettings.getSettingValue(METADATA_EMAIL_SENDER);
		emailFromAddress = (emailFromAddress == null) ? CONSUMER_PORTAL_SUPPORT_EMAIL : emailFromAddress;
		emailBody.append(lineSeperator).append(lineSeperator).append("Thanks").append(lineSeperator).append(emailFromAddress).append(lineSeperator);
		return emailBody;
	}

	private void sendEmailWithMetaData(String metaData) {

		String emailToAddress = ApplicationSettings.getSettingValue(METADATA_EMAIL_RECIPIENT);
		emailToAddress = (emailToAddress == null) ? CONSUMER_PORTAL_SUPPORT_EMAIL : emailToAddress;
		String[] emailToAddressArray = emailToAddress.split(";");
		
		String emailFromAddress = ApplicationSettings.getSettingValue(METADATA_EMAIL_SENDER);
		emailFromAddress = (emailFromAddress == null) ? CONSUMER_PORTAL_SUPPORT_EMAIL : emailFromAddress;

		String emailSubject = ApplicationSettings.getSettingValue(METADATA_EMAIL_SUBJECT);
		emailSubject = (emailSubject == null) ? META_FILE_GEN_FAILED : emailSubject;

		String smtpServer = ApplicationSettings.getSettingValue(METADATA_SMTP_SERVER);
		smtpServer = (smtpServer == null) ? SMTP_SERVER : smtpServer;

		try {
			InternetAddress[] internetAddress = new InternetAddress[emailToAddressArray.length];
			for (int index = 0 ; index < emailToAddressArray.length;  index ++) {
				internetAddress[index] = new InternetAddress(emailToAddressArray[index]);
			}
			
			Properties props = System.getProperties();
			String ipAddress = InetAddress.getByName(smtpServer).getHostAddress();
			props.put("mail.smtp.host", ipAddress);
			Session session = Session.getDefaultInstance(props, null);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailFromAddress));
			message.setRecipients(Message.RecipientType.TO, internetAddress);
			message.setSubject(emailSubject);
			message.setContent(metaData, "text/plain");
			Transport.send(message);
		} catch (Exception exception) {
			logger.error("Exception : " + exception.getCause() + " :: " + exception.getMessage());
			throw new BenefitChangeException(exception.getMessage());
		}
	}
}
