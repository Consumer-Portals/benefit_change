package com.bcbst.benefitchange.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.bcbst.benefitchange.core.BenefitChangeContext;
import com.bcbst.benefitchange.dto.Address;
import com.bcbst.benefitchange.dto.Application;
import com.bcbst.benefitchange.dto.BenefitChangeInfo;
import com.bcbst.benefitchange.dto.Dependent;
import com.bcbst.benefitchange.dto.PlanSearchMember;
import com.bcbst.benefitchange.pdf.PDFGenerator;
import com.bcbst.benefitchange.util.BenefitChangeException;
import com.bcbst.benefitchange.util.FieldsValidator;


/**
 * @objective This class has DAO methods for save/update/retrieval of Benefit Change Form.
 * @author Vijay Narsingoju 11/3/2015
 * 
 */
@Named
public class BenefitChangeDAO {
	@Inject
	private BenefitChangeContext benefitChangeContext;

	private static final String DATASOURCE_ISSUE = "Datasource issue";
	private static final String INVALID_INPUT = "Invalid input";
	private static final String FRST_DAY_OF_MNTH_FLW_SUBM_IND = "FRST_DAY_OF_MNTH_FLW_SUBM_IND";
	private static final String R_GAIN_ICHRA_QSEHRA_IND = "R_GAIN_ICHRA_QSEHRA_IND";
	private static final String REPRESENTATIVE_NAME = "REPRESENTATIVE_NAME";
	private static final String USER_TYPE = "USER_TYPE";
	private static final String REP_ID = "REP_ID";
	private static final String VIS_SBC_ENG_LOC = "VIS_SBC_ENG_LOC";
	private static final String DEN_SBC_ENG_LOC = "DEN_SBC_ENG_LOC";
	private static final String MED_SBC_ENG_LOC = "MED_SBC_ENG_LOC";
	private static final String SBSB_VIS_CANCEL_RSN = "SBSB_VIS_CANCEL_RSN";
	private static final String SBSB_DEN_CANCEL_RSN = "SBSB_DEN_CANCEL_RSN";
	private static final String SBSB_MED_CANCEL_RSN = "SBSB_MED_CANCEL_RSN";
	private static final String SBSB_VIS_CANCEL_DATE = "SBSB_VIS_CANCEL_DATE";
	private static final String SBSB_DEN_CANCEL_DATE = "SBSB_DEN_CANCEL_DATE";
	private static final String SBSB_MED_CANCEL_DATE = "SBSB_MED_CANCEL_DATE";
	private static final String MED_PLAN_NAME = "MED_PLAN_NAME";
	private static final String VIS_REM_IND = "VIS_REM_IND";
	private static final String CHG_ADDR_IND = "CHG_ADDR_IND";
	private static final String CHG_POLICY_IND = "CHG_POLICY_IND";
	private static final String VIS_PLAN_NAME = "VIS_PLAN_NAME";
	private static final String DEN_PLAN_NAME = "DEN_PLAN_NAME";
	private static final String VIS_RATE_PER_MONTH = "VIS_RATE_PER_MONTH";
	private static final String DEN_RATE_PER_MONTH = "DEN_RATE_PER_MONTH";
	private static final String MED_RATE_PER_MONTH = "MED_RATE_PER_MONTH";
	private static final String APP_STATUS_CD = "APP_STATUS_CD";
	private static final String ADD_REM_DEP_IND = "ADD_REM_DEP_IND";
	private static final String TERM_POLICY_IND = "TERM_POLICY_IND";
	private static final String SBSB_DAYTIME_PHONE = "SBSB_DAYTIME_PHONE";
	private static final String CHG_EMAIL_ADDR_IND = "CHG_EMAIL_ADDR_IND";
	private static final String CHG_NAME_IND = "CHG_NAME_IND";
	private static final String CHG_PINFO_IND = "CHG_PINFO_IND";
	private static final String EVENT_DT_IND = "EVENT_DT_IND";
	private static final String FIRST_MNTH_EFFDT_IND = "FIRST_MNTH_EFFDT_IND";
	private static final String STD_EFF_GUIDELINES_IND = "STD_EFF_GUIDELINES_IND";
	private static final String R_GAIN_DEP_IND = "R_GAIN_DEP_IND";
	private static final String R_REDOFHRS_IND = "R_REDOFHRS_IND";
	private static final String R_LOSS_OF_MINHLT_IND = "R_LOSS_OF_MINHLT_IND";
	private static final String R_LOSS_OF_DEP_IND = "R_LOSS_OF_DEP_IND";
	private static final String R_MRG_IND = "R_MRG_IND";
	private static final String R_NONCALYRPOLEXP_IND = "R_NONCALYRPOLEXP_IND";
	private static final String R_PERMANENTMOVE_IND = "R_PERMANENTMOVE_IND";
	private static final String R_BTHADPFSTRCR_IND = "R_BTHADPFSTRCR_IND";
	private static final String R_OPEN_ENROLL_IND = "R_OPEN_ENROLL_IND";
	private static final String BEN_CHG_EVENT_DATE = "BEN_CHG_EVENT_DATE";
	private static final String BEN_NETWORK = "BEN_NETWORK";
	private static final String BEN_PLAN = "BEN_PLAN";
	private static final String CHG_BEN_IND = "CHG_BEN_IND";
	private static final String DEN_REM_IND = "DEN_REM_IND";
	private static final String DEN_ADD_IND = "DEN_ADD_IND";
	private static final String DEN_IND = "DEN_IND";
	private static final String SBSB_VIS_EXAM_MATRLS_IND = "SBSB_VIS_EXAM_MATRLS_IND";
	private static final String SBSB_VISION_EXAM_ONLY_IND = "SBSB_VISION_EXAM_ONLY_IND";
	private static final String SBSB_VISION_CHG_IND = "SBSB_VISION_CHG_IND";
	private static final String SBSB_VISION_IND = "SBSB_VISION_IND";
	private static final String ADD_DEL_ANC_PRD_IND = "ADD_DEL_ANC_PRD_IND";
	private static final String TERM_LIFE_COV_IND = "TERM_LIFE_COV_IND";
	private static final String SBSB_ADD_DEL_SEC_IND = "SBSB_ADD_DEL_SEC_IND";
	private static final String TERM_SUB_NEWID_RSN_DESC = "TERM_SUB_NEWID_RSN_DESC";
	private static final String TERM_SUB_NEWID_RSN_CD = "TERM_SUB_NEWID_RSN_CD";
	private static final String TERM_SBSB_IND = "TERM_SBSB_IND";
	private static final String TOB_USG_SPOUSE_IND = "TOB_USG_SPOUSE_IND";
	private static final String TOB_USG_SBSB_IND = "TOB_USG_SBSB_IND";
	private static final String TOB_USG_CHG = "TOB_USG_CHG";
	private static final String CANCEL_DEN_VIS_IND = "CANCEL_DEN_VIS_IND";
	private static final String SBSB_POLICY_CANCEL_RSN = "SBSB_POLICY_CANCEL_RSN";
	private static final String SBSB_POLICY_CANCEL_DATE = "SBSB_POLICY_CANCEL_DATE";
	private static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
	private static final String CHG_PHONE_IND = "CHG_PHONE_IND";
	private static final String SBSB_RSN_FOR_NAME_CHG = "SBSB_RSN_FOR_NAME_CHG";
	private static final String UPDTD_SBSB_MIDDLE_NAME = "UPDTD_SBSB_MIDDLE_NAME";
	private static final String UPDTD_SBSB_FIRST_NAME = "UPDTD_SBSB_FIRST_NAME";
	private static final String UPDTD_SBSB_LAST_NAME = "UPDTD_SBSB_LAST_NAME";
	private static final String SBSB_DATE_OF_BIRTH = "SBSB_DATE_OF_BIRTH";
	private static final String SBSB_MIDDLE_NAME = "SBSB_MIDDLE_NAME";
	private static final String SBSB_FIRST_NAME = "SBSB_FIRST_NAME";
	private static final String SBSB_LAST_NAME = "SBSB_LAST_NAME";
	private static final String GRGR_ID = "GRGR_ID";
	private static final String COUNTY_NAME = "COUNTY_NAME";
	private static final String ZIP_CODE = "ZIP_CODE";
	private static final String STATE_NAME = "STATE_NAME";
	private static final String CITY_NAME = "CITY_NAME";
	private static final String STREET_NAME = "STREET_NAME";
	private static final String SBSB_ADDR_IND = "SBSB_ADDR_IND";
	private static final String APP_SUBMITTED_DATE = "APP_SUBMITTED_DATE";
	private static final String DEP_TERM_REASON = "DEP_TERM_REASON";
	private static final String DEP_TERM_DATE = "DEP_TERM_DATE";
	private static final String DEP_MEDICARE_ELIG_IND = "DEP_MEDICARE_ELIG_IND";
	private static final String DEP_CHANGE_TYPE_IND = "DEP_CHANGE_TYPE_IND";
	private static final String DEP_RELATIONSHIP_DESC = "DEP_RELATIONSHIP_DESC";
	private static final String DEP_RELATIONSHIP_TYPE_IND = "DEP_RELATIONSHIP_TYPE_IND";
	private static final String DEP_TOBACCO_USAGE_IND = "DEP_TOBACCO_USAGE_IND";
	private static final String DEP_SSN = "DEP_SSN";
	private static final String DEP_DATE_OF_BIRTH = "DEP_DATE_OF_BIRTH";
	private static final String DEP_GENDER = "DEP_GENDER";
	private static final String DEP_MI_NAME = "DEP_MI_NAME";
	private static final String DEP_FIRST_NAME = "DEP_FIRST_NAME";
	private static final String DEP_LAST_NAME = "DEP_LAST_NAME";
	private static final String DEP_SEQ_NO = "DEP_SEQ_NO";
	private static final String APPLICATION_ID = "Application_ID";
	private static final String WEB_USER_ID = "WebUserID";
	private static final String SBSB_ID = "SBSB_ID";
	private static final String INVALID_APPLICATION_ID = "Invalid applicationId";
	private Logger logger = LogManager.getLogger(BenefitChangeDAO.class);

	/**
	 * @objective This method retrieves the benefit change details from master table INDIV_ENRL_CHG_DETAILS.
	 * @param applicationId
	 * @return
	 */
	public BenefitChangeInfo getBenefitChangeFormDetails(String applicationId){
		logger.debug("BenefitChangeDAO: getBenefitChangeFormDetails() executed.");
		BenefitChangeInfo benefitChangeInfo = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// validate input
		if (FieldsValidator.isEmpty(applicationId)) {
			throw new BenefitChangeException(INVALID_APPLICATION_ID);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("select top 1 * from INDIV_ENRL_CHG_DETAILS where Application_ID = ?");
			ps.setString(1, applicationId);
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			if (rs.next()) {
				benefitChangeInfo = populateChangeForm(rs);				
			}
			rs.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}
		logger.debug("BenefitChangeDAO: getBenefitChangeFormDetails() ended.");
		return benefitChangeInfo;
	}
	
	/**
	 * @objective This method will retrieve all the applications for the given subscriber id.
	 * @param subscriberId
	 * @return
	 */
	public List<Application> getBenefitChangeApplicationsList(String subscriberId){
		return getBenefitChangeApplicationsList(subscriberId, null);
	}
	

	public List<Application> getBenefitChangeApplicationsListForBroker(String subscriberId, List<String> repIds){
		return getBenefitChangeApplicationsList(subscriberId, repIds);
	}
	/**
	 * @objective This method will retrieve all the applications for the given subscriber id.
	 * @param subscriberId
	 * @return
	 */
	public List<Application> getBenefitChangeApplicationsList(String subscriberId, List<String> repIds){
		logger.debug("BenefitChangeDAO: getBenefitChangeApplicationsListForBroker() executed: "+ StringEscapeUtils.escapeJava(subscriberId));
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Application application = null;
		List<Application> applicationList = null;
		
		// validate input
		if (FieldsValidator.isEmpty(subscriberId)) {
			throw new BenefitChangeException("Invalid subscriberId");
		}
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			
			if(repIds==null) {
				ps = conn.prepareStatement("select Application_ID, APP_SUBMITTED_DATE, APP_STATUS_CD, SBSB_DATE_OF_BIRTH, TOB_USG_SBSB_IND from INDIV_ENRL_CHG_DETAILS  where SBSB_ID = ?  ORDER BY APP_STATUS_CD ASC, Application_ID DESC");			

				ps.setString(1, subscriberId);
			} else {
				String params = getRepIdInClause(repIds);

				String queryString = "select Application_ID, APP_SUBMITTED_DATE, APP_STATUS_CD, SBSB_DATE_OF_BIRTH, TOB_USG_SBSB_IND from INDIV_ENRL_CHG_DETAILS  where SBSB_ID = ? AND (USER_TYPE is NULL OR USER_TYPE = 'M' OR ((USER_TYPE = 'B' OR USER_TYPE = 'I') AND (REP_ID IS NULL OR REP_ID IN (" + params +"))))  ORDER BY APP_STATUS_CD ASC, Application_ID DESC";
				
				logger.debug(queryString);
				
				ps = conn.prepareStatement(queryString);
				
				ps.setString(1, subscriberId);

				for(int i=0;i<repIds.size();i++) {
					logger.debug("RepId #{}='{}'", i, StringEscapeUtils.escapeJava(repIds.get(i)));
					ps.setString(2+i, repIds.get(i));
				}
			}
			
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			applicationList = new ArrayList<>();
			while (rs.next()) {
				application = new Application();
				application.setApplicationId(rs.getString( APPLICATION_ID));
				application.setApplStatusCode(rs.getString( APP_STATUS_CD));
				if (rs.getDate(APP_SUBMITTED_DATE)!=null){
					application.setApplSubmittedDate(
							new java.util.Date(rs.getDate(APP_SUBMITTED_DATE).getTime()));
				}
				if (rs.getDate(SBSB_DATE_OF_BIRTH)!=null){
					application.setSubscriberDateOfBirth(
							new java.util.Date(rs.getDate(SBSB_DATE_OF_BIRTH).getTime()));
				}
				application.setTobaccoUsageSubscriberInd(
						rs.getString(TOB_USG_SBSB_IND));
				applicationList.add( application);
			}
			
			rs.close();

		}catch (BenefitChangeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closeResultSet(rs);
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: getBenefitChangeApplicationsList() ended: "+ StringEscapeUtils.escapeJava(subscriberId));
		return applicationList;
	}

	private String getRepIdInClause(List<String> repIds) {
		StringBuilder params = new StringBuilder("");
		
		for(int i=0;i<repIds.size();i++) {
			params.append("?");			
			
			if(i+1<repIds.size()) {
				params.append(",");
			}
			
		}
		return params.toString();
	}
	

	/**
	 * @objective This method will retrieve dependent details from  INDIV_ENRL_CHG_DEPENDENT table.
	 * @param applicationId
	 * @return
	 */
	public List<Dependent> getDependentDetails(String applicationId) {
		logger.debug("BenefitChangeDAO: getDependentChangeFormDetails() executed: {}", StringEscapeUtils.escapeJava(applicationId));
		List<Dependent> listofdependentDetails = new ArrayList<>();
		Dependent dependentDetails = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// validate input
		if (FieldsValidator.isEmpty(applicationId)) {
			throw new BenefitChangeException(INVALID_APPLICATION_ID);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("select * from INDIV_ENRL_CHG_DEPENDENT where Application_ID = ?");
			ps.setString(1, applicationId);
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			while (rs.next()) {
				dependentDetails = populateDependentDetails(rs);
				listofdependentDetails.add(dependentDetails);				
			}
			rs.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closeResultSet(rs);
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: getDependentDetails() ended.");
		return listofdependentDetails;
	}

	

	
	/**
	 * @objective This method will return the addresses for a given applicationId.
	 * @param applicationId
	 * @return
	 */
	public List<Address> getAddressList(String applicationId) {
		logger.debug("BenefitChangeDAO: getDependentAddress() executed.");
		List<Address> listofdependentAddress = new ArrayList<>();
		Address dependentDetails = new Address();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// validate input
		if (FieldsValidator.isEmpty(applicationId)) {
			throw new BenefitChangeException(INVALID_APPLICATION_ID);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("select * from INDIV_ENRL_MBR_CHG_ADDR where Application_ID = ?");
			ps.setString(1, applicationId);
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();

			while (rs.next()) {
				dependentDetails = populateDependentAddress(rs);
				listofdependentAddress.add(dependentDetails);				
			}
			rs.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closeResultSet(rs);
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: getDependentAddress() ended.");
		return listofdependentAddress;
	}
	
	/**
	 * @objective This method will save benefitChangeInfo information in to multiple tables.
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean saveBenefitChangeFormDetails(BenefitChangeInfo benefitChangeInfo, String applicationId) {	
		logger.debug("BenefitChangeDAO: saveBenefitChangeFormDetails() executed: applicationId: " + applicationId);
		boolean success = false;
		long starttime = System.currentTimeMillis();
		logger.debug("BenefitChangeDAO: saveBenefitChangeFormDetails() : applicationId: "+ applicationId);
		if (benefitChangeInfo == null || applicationId == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement chgdetailsPS = null;
		
		StringBuilder chgdetailssql = new StringBuilder();
		chgdetailssql.append("INSERT INTO INDIV_ENRL_CHG_DETAILS");
		chgdetailssql.append("(WebUserID, Application_ID, GRGR_ID, SBSB_ID, SBSB_LAST_NAME, SBSB_FIRST_NAME, SBSB_MIDDLE_NAME, SBSB_DATE_OF_BIRTH, UPDTD_SBSB_LAST_NAME, UPDTD_SBSB_FIRST_NAME, ");
		chgdetailssql.append("UPDTD_SBSB_MIDDLE_NAME, SBSB_RSN_FOR_NAME_CHG, CHG_PHONE_IND, EMAIL_ADDRESS, SBSB_POLICY_CANCEL_DATE, SBSB_POLICY_CANCEL_RSN, CANCEL_DEN_VIS_IND, TOB_USG_CHG, ");
		chgdetailssql.append("TOB_USG_SBSB_IND, TOB_USG_SPOUSE_IND, TERM_SBSB_IND, TERM_SUB_NEWID_RSN_CD,TERM_SUB_NEWID_RSN_DESC ");
		chgdetailssql.append(",SBSB_ADD_DEL_SEC_IND,TERM_LIFE_COV_IND,ADD_DEL_ANC_PRD_IND,SBSB_VISION_IND,SBSB_VISION_CHG_IND,SBSB_VISION_EXAM_ONLY_IND,SBSB_VIS_EXAM_MATRLS_IND,DEN_IND,DEN_ADD_IND ");
		chgdetailssql.append(",DEN_REM_IND,CHG_BEN_IND,BEN_PLAN,BEN_NETWORK,BEN_CHG_EVENT_DATE,R_OPEN_ENROLL_IND,R_BTHADPFSTRCR_IND,R_PERMANENTMOVE_IND,R_NONCALYRPOLEXP_IND ");
		chgdetailssql.append(",R_MRG_IND,R_LOSS_OF_DEP_IND,R_LOSS_OF_MINHLT_IND,R_REDOFHRS_IND,R_GAIN_DEP_IND,STD_EFF_GUIDELINES_IND,FIRST_MNTH_EFFDT_IND,EVENT_DT_IND ");
		chgdetailssql.append(",CHG_PINFO_IND,CHG_NAME_IND,CHG_EMAIL_ADDR_IND,SBSB_DAYTIME_PHONE,TERM_POLICY_IND,ADD_REM_DEP_IND,APP_SUBMITTED_DATE,APP_STATUS_CD ");
		chgdetailssql.append(", MED_RATE_PER_MONTH,DEN_RATE_PER_MONTH,VIS_RATE_PER_MONTH, DEN_PLAN_NAME, VIS_PLAN_NAME, CHG_POLICY_IND, CHG_ADDR_IND, VIS_REM_IND, MED_PLAN_NAME ");
		chgdetailssql.append(", SBSB_MED_CANCEL_DATE,SBSB_MED_CANCEL_RSN,SBSB_DEN_CANCEL_DATE,SBSB_DEN_CANCEL_RSN,SBSB_VIS_CANCEL_DATE,SBSB_VIS_CANCEL_RSN, MED_SBC_ENG_LOC, DEN_SBC_ENG_LOC, VIS_SBC_ENG_LOC, REP_ID, USER_TYPE ");
		chgdetailssql.append(", REPRESENTATIVE_NAME, REPRESENTATIVE_EMAIL, R_GAIN_ICHRA_QSEHRA_IND, FRST_DAY_OF_MNTH_FLW_SUBM_IND)");
		chgdetailssql.append("  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext
					.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false); // begin transaction
			
			logger.debug("BenefitChangeDAO: saveBenefitChangeFormDetails() : chgdetailssql: "+ chgdetailssql.toString());
			chgdetailsPS = conn.prepareStatement(chgdetailssql.toString());
			populateBenefitChangeInfoPreparedStatement(chgdetailsPS, benefitChangeInfo, applicationId);
			chgdetailsPS.setQueryTimeout(10);
			int rowsUpdCount = 0;
			rowsUpdCount = chgdetailsPS.executeUpdate();
			logger.debug("BenefitChangeDAO: saveBenefitChangeFormDetails() : chgdetailsPS: finished");
			
			// save address details if it exist
			saveAddressDetailsIfExist(benefitChangeInfo, applicationId, conn, rowsUpdCount);
			
			// save dependent details if it exist
			saveDependentDetailsIfExist(benefitChangeInfo, applicationId, conn, rowsUpdCount);
			// save plan search members
			
			savePlanSearchMembersIfExist(benefitChangeInfo, applicationId, conn, rowsUpdCount);
			
			// If action is submit then generate pdf.
			if (benefitChangeInfo.getAction()!= null && benefitChangeInfo.getAction().trim().equalsIgnoreCase("Submit")){
				PDFGenerator pdfGenerator = new PDFGenerator();
				pdfGenerator.submitRequestForPDFGeneration(benefitChangeInfo, applicationId);
			}
			
			conn.commit(); // commit transaction
			
			if (rowsUpdCount > 0){
				success = true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback(); // rollback transaction
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closePreparedStatement(chgdetailsPS);
			ResourceTool.close(conn);
		}
		long timetaken = System.currentTimeMillis() - starttime;
		logger.debug("BenefitChangeDAO: saveBenefitChangeFormDetails() ended in " + timetaken + " msecs.");
		logger.debug("BenefitChangeDAO: saveBenefitChangeFormDetails() ended." + success);
		return success;
	}

	private void savePlanSearchMembersIfExist(BenefitChangeInfo benefitChangeInfo, String applicationId,
			Connection conn, int rowsUpdCount) throws SQLException {
		
		if (benefitChangeInfo.getPlanSearchMembers() != null &&  !benefitChangeInfo.getPlanSearchMembers().isEmpty() && rowsUpdCount > 0){
			StringBuilder planmemberssql = new StringBuilder();
			planmemberssql.append("INSERT INTO INDIV_ENRL_CHG_PLAN_MEMBERS(WebUserID ,Application_ID ,SEQ_NO ,FIRST_NAME,GENDER,DATE_OF_BIRTH,TOBACCO_USAGE_IND,");
			planmemberssql.append(" RELATIONSHIP_TYPE_IND, SBSB_ID ,APP_SUBMITTED_DATE,ZIP_CODE,COUNTY_NAME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ");
			
			logger.debug("BenefitChangeDAO: savePlanSearchMembersIfExist(): applicationId: [{}] planmemberssql: {}",StringEscapeUtils.escapeJava(applicationId), planmemberssql.toString());
			
			try(PreparedStatement planmembersPS = conn.prepareStatement(planmemberssql.toString());) {
				planmembersPS.setQueryTimeout(10);
				for (PlanSearchMember member: benefitChangeInfo.getPlanSearchMembers()){
					logger.debug("ProductDAO: savePlanSearchMembers() member dob: " + member.getDateofBirth());
					planmembersPS.setString(1, member.getUserId());
					planmembersPS.setString(2, applicationId);
					planmembersPS.setString(3, member.getDependentSeqNum());
					planmembersPS.setString(4, member.getFirstName());
					planmembersPS.setString(5, member.getGender());
					if (member.getDateofBirth() != null){
						planmembersPS.setDate(6, new java.sql.Date(member.getDateofBirth().getTime()));
					}
					planmembersPS.setString(7, member.getTobaccoUsageInd());
					planmembersPS.setString(8, member.getRelationShipTypeInd());
					planmembersPS.setString(9, benefitChangeInfo.getSubscriberId());
					if (member.getAppSubmittedDate() != null){
						planmembersPS.setDate(10, new java.sql.Date(member.getAppSubmittedDate().getTime()));
					}
					planmembersPS.setString(11, member.getZipcode());
					planmembersPS.setString(12, member.getCountyName());
					planmembersPS.addBatch();
					
				}
				planmembersPS.executeBatch();
			}		
		}
	}

	private void saveDependentDetailsIfExist(BenefitChangeInfo benefitChangeInfo, String applicationId,
			Connection conn, int rowsUpdCount) throws SQLException {
		
		if (benefitChangeInfo.getDependents() != null && !benefitChangeInfo.getDependents().isEmpty() && rowsUpdCount > 0){
			StringBuilder dependentssql = new StringBuilder();
			dependentssql.append("INSERT INTO INDIV_ENRL_CHG_DEPENDENT ");
			dependentssql.append("(WebUserID,Application_ID, DEP_SEQ_NO,DEP_LAST_NAME,DEP_FIRST_NAME,DEP_MI_NAME");
			dependentssql.append(", DEP_GENDER, DEP_DATE_OF_BIRTH,DEP_SSN,DEP_TOBACCO_USAGE_IND");
			dependentssql.append(", DEP_RELATIONSHIP_TYPE_IND, DEP_RELATIONSHIP_DESC,DEP_CHANGE_TYPE_IND");
			dependentssql.append(", DEP_MEDICARE_ELIG_IND, DEP_TERM_DATE,DEP_TERM_REASON");
			dependentssql.append(", SBSB_ID, APP_SUBMITTED_DATE) ");
			dependentssql.append("  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			logger.debug("BenefitChangeDAO: saveDependentDetailsIfExist(): applicationId: [{}] dependentssql: {}",StringEscapeUtils.escapeJava(applicationId), dependentssql.toString());
			
			try(PreparedStatement dependentPS = conn.prepareStatement(dependentssql.toString());) {
				dependentPS.setQueryTimeout(10);
				for(Dependent dependent: benefitChangeInfo.getDependents()){
					populateDependentPreparedStatement(dependentPS, dependent, applicationId);
				}
				
				dependentPS.executeBatch();
			}			
		}
	}

	private void saveAddressDetailsIfExist(BenefitChangeInfo benefitChangeInfo, String applicationId,
			Connection conn, int rowsUpdCount) throws SQLException {
		
		
		if (benefitChangeInfo.getAddresses() != null && !benefitChangeInfo.getAddresses().isEmpty() && rowsUpdCount > 0){
			StringBuilder addresssql = new StringBuilder();
			addresssql.append("INSERT INTO INDIV_ENRL_MBR_CHG_ADDR  ");
			addresssql.append("(WebUserID,Application_ID,SBSB_ID,SBSB_ADDR_IND,STREET_NAME,CITY_NAME,STATE_NAME ");
			addresssql.append(",ZIP_CODE ,COUNTY_NAME ,APP_SUBMITTED_DATE) ");
			addresssql.append(" VALUES (?,?,?,?,?,?,?,?,?,?)");
			
			logger.debug("BenefitChangeDAO: saveAddressDetailsIfExist(): applicationId: [{}] address sql: {}", StringEscapeUtils.escapeJava(applicationId), addresssql.toString());

			try(PreparedStatement addressPS = conn.prepareStatement(addresssql.toString());) {
				for(Address address: benefitChangeInfo.getAddresses()){
					populateAddressPreparedStatement(addressPS, address, applicationId);
				}
				addressPS.setQueryTimeout(10);
				addressPS.executeBatch();				
			}
		}
	}

	/**
	 * @objective This method will update benefitChangeInfo data in multiple tables. It will update existing record in INDIV_ENRL_CHG_DETAILS and will delete 
	 * 			  all the records for child tables and will insert new data in child tables for the same applicationId and subscriberId. it runs in single transaction.
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean updateBenefitChangeFormDetails(BenefitChangeInfo benefitChangeInfo) {
		logger.debug("BenefitChangeDAO: updateBenefitChangeFormDetails() executed.");
		long starttime = System.currentTimeMillis();
		boolean success = false;
		if (benefitChangeInfo == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement chgdetailsPS = null;
		PreparedStatement delAddressPS = null;
		PreparedStatement delPlanmembersPS = null;
		PreparedStatement delDependentPS = null;
		
		StringBuilder chgdetailssql = new StringBuilder();
		chgdetailssql.append("UPDATE INDIV_ENRL_CHG_DETAILS");
		chgdetailssql.append("   SET WebUserID = ? ,Application_ID = ? ,GRGR_ID = ?      ,SBSB_ID = ?  ,SBSB_LAST_NAME = ?  ,SBSB_FIRST_NAME = ?      ,SBSB_MIDDLE_NAME = ?      ");
		chgdetailssql.append(", SBSB_DATE_OF_BIRTH = ? ,UPDTD_SBSB_LAST_NAME = ?      ,UPDTD_SBSB_FIRST_NAME = ?      ,UPDTD_SBSB_MIDDLE_NAME = ?      ,SBSB_RSN_FOR_NAME_CHG = ?      ");
		chgdetailssql.append(", CHG_PHONE_IND = ? ,EMAIL_ADDRESS = ?      ,SBSB_POLICY_CANCEL_DATE = ?      ,SBSB_POLICY_CANCEL_RSN = ?      ,CANCEL_DEN_VIS_IND = ?      ,TOB_USG_CHG = ");
		chgdetailssql.append("? ,TOB_USG_SBSB_IND = ? ,TOB_USG_SPOUSE_IND = ?      ,TERM_SBSB_IND = ?      ,TERM_SUB_NEWID_RSN_CD = ?      ,TERM_SUB_NEWID_RSN_DESC = ?      ");
		chgdetailssql.append(", SBSB_ADD_DEL_SEC_IND = ? ,TERM_LIFE_COV_IND = ?      ,ADD_DEL_ANC_PRD_IND = ?      ,SBSB_VISION_IND = ?      ,SBSB_VISION_CHG_IND = ?      ");
		chgdetailssql.append(", SBSB_VISION_EXAM_ONLY_IND = ? ,SBSB_VIS_EXAM_MATRLS_IND = ?      ,DEN_IND = ?      ,DEN_ADD_IND = ?      ,DEN_REM_IND = ?      ,CHG_BEN_IND = ? ");
		chgdetailssql.append(", BEN_PLAN = ?,BEN_NETWORK = ?      ,BEN_CHG_EVENT_DATE = ?      ,R_OPEN_ENROLL_IND = ?      ,R_BTHADPFSTRCR_IND = ?      ,R_PERMANENTMOVE_IND = ?  ");
		chgdetailssql.append(", R_NONCALYRPOLEXP_IND = ? ,R_MRG_IND = ?      ,R_LOSS_OF_DEP_IND = ?      ,R_LOSS_OF_MINHLT_IND = ?      ,R_REDOFHRS_IND = ?      ,R_GAIN_DEP_IND = ?");
		chgdetailssql.append(", STD_EFF_GUIDELINES_IND = ? ,FIRST_MNTH_EFFDT_IND = ?      ,EVENT_DT_IND = ?      ,CHG_PINFO_IND = ?      ,CHG_NAME_IND = ?      ");
		chgdetailssql.append(", CHG_EMAIL_ADDR_IND = ? ,SBSB_DAYTIME_PHONE = ?      ,TERM_POLICY_IND = ?      ,ADD_REM_DEP_IND = ?      ,APP_SUBMITTED_DATE = ?      ,APP_STATUS_CD = ?  ");
		chgdetailssql.append(", MED_RATE_PER_MONTH = ? ,DEN_RATE_PER_MONTH = ? ,VIS_RATE_PER_MONTH = ?, DEN_PLAN_NAME=?, VIS_PLAN_NAME=?, CHG_POLICY_IND=?, CHG_ADDR_IND=?, VIS_REM_IND=?, MED_PLAN_NAME=? ");
		chgdetailssql.append(", SBSB_MED_CANCEL_DATE =? ,SBSB_MED_CANCEL_RSN = ?,SBSB_DEN_CANCEL_DATE = ?,SBSB_DEN_CANCEL_RSN =?,SBSB_VIS_CANCEL_DATE =?,SBSB_VIS_CANCEL_RSN = ?, MED_SBC_ENG_LOC=?, DEN_SBC_ENG_LOC=?, VIS_SBC_ENG_LOC=? ");
		chgdetailssql.append(", REP_ID=?, USER_TYPE=?, REPRESENTATIVE_NAME=?, REPRESENTATIVE_EMAIL=?, R_GAIN_ICHRA_QSEHRA_IND=?, FRST_DAY_OF_MNTH_FLW_SUBM_IND=? WHERE Application_ID = ?");
		
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			
			conn.setAutoCommit(false); // begin transaction
			
			chgdetailsPS = conn.prepareStatement(chgdetailssql.toString());
			populateUpdatedBenefitChangeInfoPS(chgdetailsPS, benefitChangeInfo);
			chgdetailsPS.setString(82, benefitChangeInfo.getApplicationId());
			chgdetailsPS.setQueryTimeout(10);
			int rowsUpdCount = 0;
			rowsUpdCount = chgdetailsPS.executeUpdate();
			
			
			// run delete queries
			String deleteAddrSQL = "DELETE FROM INDIV_ENRL_MBR_CHG_ADDR WHERE Application_ID = ? and SBSB_ID = ?";
			String deletePlanMembersSQL = "DELETE from INDIV_ENRL_CHG_PLAN_MEMBERS WHERE Application_ID = ? and SBSB_ID = ?";
			String deleteDependentsSQL = "DELETE FROM INDIV_ENRL_CHG_DEPENDENT WHERE Application_ID = ? and SBSB_ID = ?";
			
			delAddressPS = conn.prepareStatement(deleteAddrSQL);			
			delAddressPS.setString(1,benefitChangeInfo.getApplicationId().trim());
			delAddressPS.setString(2,benefitChangeInfo.getSubscriberId().trim());
			delAddressPS.setQueryTimeout(10);
			delAddressPS.executeUpdate();
			logger.debug("BenefitChangeDAO: updateBenefitChangeFormDetails() deleteAddrSQL executed: "+ deleteAddrSQL);
			
			delPlanmembersPS = conn.prepareStatement(deletePlanMembersSQL);			
			delPlanmembersPS.setString(1,benefitChangeInfo.getApplicationId().trim());
			delPlanmembersPS.setString(2,benefitChangeInfo.getSubscriberId().trim());
			delPlanmembersPS.setQueryTimeout(10);
			delPlanmembersPS.executeUpdate();
			logger.debug("BenefitChangeDAO: updateBenefitChangeFormDetails() deletePlanMembersSQL executed: "+ deletePlanMembersSQL);
			
			delDependentPS = conn.prepareStatement(deleteDependentsSQL);			
			delDependentPS.setString(1,benefitChangeInfo.getApplicationId().trim());
			delDependentPS.setString(2,benefitChangeInfo.getSubscriberId().trim());
			delDependentPS.setQueryTimeout(10);
			delDependentPS.executeUpdate();
			logger.debug("BenefitChangeDAO: updateBenefitChangeFormDetails() deleteDependentsSQL executed: "+ deleteDependentsSQL);
						
			// run insert queries
			
			// save address details if it exist
				saveAddressDetailsIfExist(benefitChangeInfo, benefitChangeInfo.getApplicationId(), conn, rowsUpdCount);			
				// save dependent details if it exist
				saveDependentDetailsIfExist(benefitChangeInfo, benefitChangeInfo.getApplicationId(), conn, rowsUpdCount);
				// save plan search members
				
				savePlanSearchMembersIfExist(benefitChangeInfo, benefitChangeInfo.getApplicationId(), conn, rowsUpdCount);
				
				// If action is submit then generate pdf.
				if (benefitChangeInfo.getAction()!= null && benefitChangeInfo.getAction().trim().equalsIgnoreCase("Submit")){
					PDFGenerator pdfGenerator = new PDFGenerator();
					pdfGenerator.submitRequestForPDFGeneration(benefitChangeInfo, benefitChangeInfo.getApplicationId());
				}
			
			conn.commit(); // commit transaction
			
			if (rowsUpdCount > 0){
				success = true;
			}

		} catch (Exception e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool
				.closePreparedStatement(chgdetailsPS);
			ResourceTool
				.closePreparedStatement(delAddressPS);
			ResourceTool
				.closePreparedStatement(delDependentPS);
			ResourceTool
				.closePreparedStatement(delPlanmembersPS);
			ResourceTool.close(conn);
		}
		long timetaken = System.currentTimeMillis() - starttime;
		logger.debug("BenefitChangeDAO: updateBenefitChangeFormDetails() ended in " + timetaken + " msecs.");
		logger.debug("BenefitChangeDAO: updateBenefitChangeFormDetails() ended." + success);
		return success;
	}
	
	
	
	/**
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean updateDependentDetails(Dependent dependent) {
		logger.debug("BenefitChangeDAO: updateDependentDetails() executed.");
		boolean success = false;
		if (dependent == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE INDIV_ENRL_CHG_DEPENDENT ");
		sql.append("SET WebUserID = ? ,Application_ID = ? ,DEP_SEQ_NO = ?, DEP_LAST_NAME = ?");
		sql.append(" ,DEP_FIRST_NAME = ?, DEP_MI_NAME = ?, DEP_GENDER = ?, DEP_DATE_OF_BIRTH = ? ");
		sql.append(" ,DEP_SSN = ?, DEP_TOBACCO_USAGE_IND = ?, DEP_RELATIONSHIP_TYPE_IND = ?  ");
		sql.append(" ,DEP_RELATIONSHIP_DESC = ?, DEP_CHANGE_TYPE_IND = ? ");
		sql.append(" ,DEP_MEDICARE_ELIG_IND = ?, DEP_TERM_DATE = ?, DEP_TERM_REASON = ? ");
		sql.append(" ,SBSB_ID = ?, APP_SUBMITTED_DATE = ? ");
		sql.append(" WHERE Application_ID = ? and DEP_SEQ_NO = ?");

		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			populateUpdatedDependentPS(ps, dependent);
			ps.setString(16,dependent.getApplicationId());
			ps.setString(17,dependent.getDependentSeqNum());
			ps.setQueryTimeout(10);
			int rowsUpdCount = 0;
			rowsUpdCount = ps.executeUpdate();
			conn.commit();
			if (rowsUpdCount > 0){
				success = true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: updateDependentDetails() ended." + success);
		return success;
	}
	
	
	
	/**
	 * @objective This method will update INDIV_ENRL_CHG_DETAILS with APP_STATUS_CD = 'D' for a given applicationId so that it won't be retrieved again. 
	 * This is delete functionality of an application.
	 * @param applicationId
	 * @return
	 */
	public boolean deleteBenefitChangeFormDetails(String applicationId){
		logger.debug("BenefitChangeDAO: deleteBenefitChangeFormDetails() executed.");
		Connection conn = null;
		PreparedStatement ps = null;
		boolean updated = false;
		
		// validate input
		if (FieldsValidator.isEmpty(applicationId)) {
			throw new BenefitChangeException(INVALID_APPLICATION_ID);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("UPDATE INDIV_ENRL_CHG_DETAILS set APP_STATUS_CD = 'D' where Application_ID = ?");
			ps.setString(1, applicationId);
			ps.setQueryTimeout(10);
			int deletedCount = ps.executeUpdate();
			conn.commit();
			if (deletedCount > 0) {
				logger.debug("BenefitChangeDAO: deleteBenefitChangeFormDetails has deleted..{}", StringEscapeUtils.escapeJava(applicationId));
				updated = true;
			}
			

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: deleteBenefitChangeFormDetails() ended:{}", StringEscapeUtils.escapeJava(applicationId));
		return updated;
	}


	
	// taken from eNroll. This method will generate unique AppId for Individual change form.
	public String generateAppId() {
		logger.debug("BenefitChangeDAO: generateAppId() begin.");
		int seqNo = 0;
		String lastDate = null;
		String date = null;
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet resultSet = null;
		String updateSQL = " update SLF_ENRL_DCNGEN set LastSeqNo = LastSeqNo + 1 WHERE Purpose = 'BNFCHGAPPID'";
		String selectSQL = " select LastSeqNo, LastJulianDate, GETDATE () as NowDate from SLF_ENRL_DCNGEN WHERE Purpose = 'BNFCHGAPPID'";
		SimpleDateFormat format = new SimpleDateFormat("yyDDD");

		try {
			connection = benefitChangeContext.getEnrlbftchgDS().getConnection();
			connection.setAutoCommit(false);
			ps1 = connection.prepareStatement(updateSQL);
			ps1.executeUpdate();
			ps2 = connection.prepareStatement(selectSQL);
			resultSet = ps2.executeQuery();

			if (resultSet.next()) {
				seqNo = resultSet.getInt("LastSeqNo");
				lastDate = resultSet.getString("LastJulianDate");
				date = format.format(resultSet.getDate("NowDate"));
			} else {
				throw new SQLException("No data return");
			}

			if (seqNo > 999999) {
				throw new SQLException("Sequence over flow");
			}

			if (!date.equals(lastDate)) {
				seqNo = 1;
				StringBuilder buffer = new StringBuilder()
						.append(" update SLF_ENRL_DCNGEN set LastSeqNo = 1, LastJulianDate = '")
						.append(date).append("' WHERE Purpose = 'BNFCHGAPPID'");
				ps3 = connection.prepareStatement(buffer.toString());
				ps3.executeUpdate();
			}
			resultSet.close();
			connection.commit();
			logger.debug("BenefitChangeDAO: generateAppId() updated.");
		} catch (SQLException e) {
			try {
				if(connection!=null) {
					connection.rollback();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closeResultSet(resultSet);
			ResourceTool.closePreparedStatement(ps1);
			ResourceTool.closePreparedStatement(ps2);
			ResourceTool.closePreparedStatement(ps3);
			ResourceTool.close(connection);
		}

		if (seqNo < 100000) {
			String tmp = ("000000" + seqNo);
			tmp = tmp.substring(tmp.length() - 6);

			return date + tmp;
		} else {
			return date + seqNo;
		}
	}	
	
	private void populateAddressPreparedStatement(PreparedStatement ps, Address address, String applicationId) throws SQLException {
		logger.debug("BenefitChangeDAO: populateAddressPreparedStatement() begin.");
		ps.setString(1, address.getUserId());
		ps.setString(2,applicationId); // generated
		ps.setString(3,address.getSubscriberId());
		ps.setString(4,address.getSubscriberAddressInd());
		ps.setString(5,address.getStreetName());
		ps.setString(6,address.getCityName());
		ps.setString(7,address.getStatename());
		ps.setString(8,address.getZipCode());
		ps.setString(9,address.getCountyName());
		if (address.getAppSubmittedDate() != null){
			ps.setDate(10, new java.sql.Date(address.getAppSubmittedDate().getTime()));
		}else{
			ps.setDate(10, null);
		}
		ps.addBatch();		
	}
	
	private void populateUpdatedAddressPS(PreparedStatement ps, Address address) throws SQLException {
		logger.debug("BenefitChangeDAO: populateUpdatedAddressPS() begin.");
		ps.setString(1, address.getUserId());
		ps.setString(2,address.getApplicationId()); 
		ps.setString(3,address.getSubscriberId());
		ps.setString(4,address.getSubscriberAddressInd());
		ps.setString(5,address.getStreetName());
		ps.setString(6,address.getCityName());
		ps.setString(7,address.getStatename());
		ps.setString(8,address.getZipCode());
		ps.setString(9,address.getCountyName());
		if (address.getAppSubmittedDate() != null){
			ps.setDate(10, new java.sql.Date(address.getAppSubmittedDate().getTime()));
		}else{
			ps.setDate(10, null);
		}
		
		logger.debug("BenefitChangeDAO: populateUpdatedAddressPS() end.");
	}
	
	
	private void populateDependentPreparedStatement(PreparedStatement ps, Dependent dependent, String applicationId) throws SQLException {
		logger.debug("BenefitChangeDAO: populateDependentPreparedStatement() begin.");
		ps.setString(1, dependent.getUserId());
		ps.setString(2, applicationId); // generated
		ps.setString(3,dependent.getDependentSeqNum());
		ps.setString(4,dependent.getDependentLastName());
		ps.setString(5,dependent.getDependentFirstName());
		ps.setString(6,dependent.getDependentMiddleName());
		ps.setString(7,dependent.getDependentGender());
		if (dependent.getDependentDateOfBirth() != null){
			ps.setDate(8, new java.sql.Date(dependent.getDependentDateOfBirth().getTime()));
		}else{
			ps.setDate(8, null);
		}
		ps.setString(9,dependent.getDependentSSN());
		ps.setString(10,dependent.getDependentTobaccoUsageInd());
		ps.setString(11,dependent.getDependentRelationShipTypeInd());
		ps.setString(12,dependent.getDependentRelationShipDesc());
		ps.setString(13,dependent.getDependentChangeTypeInd());
		ps.setString(14,dependent.getDependentMedicareInd());
		if (dependent.getDependentTermDate() != null){
			ps.setDate(15, new java.sql.Date(dependent.getDependentTermDate().getTime()));
		}else{
			ps.setDate(15, null);
		}
		ps.setString(16,dependent.getDependentTermReason());
		ps.setString(17,dependent.getSubscriberId());
		if (dependent.getAppSubmittedDate() != null){
			ps.setDate(18, new java.sql.Date(dependent.getAppSubmittedDate().getTime()));
		}else{
			ps.setDate(18, null);
		}
		ps.addBatch();		
	}
	
	private void populateUpdatedDependentPS(PreparedStatement ps, Dependent dependent) throws SQLException {
		logger.debug("BenefitChangeDAO: populateUpdatedDependentPS() begin.");
		ps.setString(1, dependent.getUserId());
		ps.setString(2, dependent.getApplicationId()); 
		ps.setString(3,dependent.getDependentSeqNum());
		ps.setString(4,dependent.getDependentLastName());
		ps.setString(5,dependent.getDependentFirstName());
		ps.setString(6,dependent.getDependentMiddleName());
		ps.setString(7,dependent.getDependentGender());
		if (dependent.getDependentDateOfBirth() != null){
			ps.setDate(8, new java.sql.Date(dependent.getDependentDateOfBirth().getTime()));
		}else{
			ps.setDate(8, null);
		}
		ps.setString(9,dependent.getDependentSSN());
		ps.setString(10,dependent.getDependentTobaccoUsageInd());
		ps.setString(11,dependent.getDependentRelationShipTypeInd());
		ps.setString(12,dependent.getDependentRelationShipDesc());
		ps.setString(13,dependent.getDependentChangeTypeInd());
		ps.setString(14,dependent.getDependentMedicareInd());
		if (dependent.getDependentTermDate() != null){
			ps.setDate(15, new java.sql.Date(dependent.getDependentTermDate().getTime()));
		}else{
			ps.setDate(15, null);
		}
		ps.setString(16,dependent.getDependentTermReason());
		ps.setString(17,dependent.getSubscriberId());
		if (dependent.getAppSubmittedDate() != null){
			ps.setDate(18, new java.sql.Date(dependent.getAppSubmittedDate().getTime()));
		}else{
			ps.setDate(18, null);
		}
		logger.debug("BenefitChangeDAO: populateDependentPreparedStatement() end.");
	}
	
	
	private Dependent populateDependentDetails(ResultSet rs) throws SQLException {
		logger.info("BenefitChangeDAO: populateDependentDetails() begin.");
		Dependent dependent = new Dependent();
		dependent.setUserId(rs.getString(WEB_USER_ID));
		dependent.setApplicationId(rs.getString(APPLICATION_ID));
		dependent.setDependentSeqNum(rs.getString(DEP_SEQ_NO));
		dependent.setDependentLastName(rs.getString(DEP_LAST_NAME));
		dependent.setDependentFirstName(rs.getString(DEP_FIRST_NAME));
		dependent.setDependentMiddleName(rs.getString(DEP_MI_NAME));
		dependent.setDependentGender(rs.getString(DEP_GENDER));
		if (rs.getDate(DEP_DATE_OF_BIRTH) != null){
			dependent.setDependentDateOfBirth(new java.util.Date(rs.getDate(DEP_DATE_OF_BIRTH).getTime()));
		}
		dependent.setDependentSSN(rs.getString(DEP_SSN));
		dependent.setDependentTobaccoUsageInd(rs.getString(DEP_TOBACCO_USAGE_IND));
		dependent.setDependentRelationShipTypeInd(rs.getString(DEP_RELATIONSHIP_TYPE_IND));
		dependent.setDependentRelationShipDesc(rs.getString(DEP_RELATIONSHIP_DESC));
		dependent.setDependentChangeTypeInd(rs.getString(DEP_CHANGE_TYPE_IND));
		dependent.setDependentMedicareInd(rs.getString(DEP_MEDICARE_ELIG_IND));
		if (rs.getDate(DEP_TERM_DATE) != null){
			dependent.setDependentTermDate(new java.util.Date(rs.getDate(DEP_TERM_DATE).getTime()));
		}
		dependent.setDependentTermReason(rs.getString(DEP_TERM_REASON));
		dependent.setSubscriberId(rs.getString(SBSB_ID));
		
		if (rs.getDate(APP_SUBMITTED_DATE) != null){
			dependent.setAppSubmittedDate(new java.util.Date(rs.getDate(APP_SUBMITTED_DATE).getTime()));
		}else{
			dependent.setAppSubmittedDate(null);
		}
		
		return dependent;

	}


	private Address populateDependentAddress(ResultSet rs) throws SQLException {
		logger.info("BenefitChangeDAO: populateDependentDetails() begin.");
		Address address = new Address();
		address.setUserId(rs.getString(WEB_USER_ID));
		address.setApplicationId(rs.getString(APPLICATION_ID));
		address.setSubscriberId(rs.getString(SBSB_ID));
		address.setSubscriberAddressInd(rs.getString(SBSB_ADDR_IND));
		address.setStreetName(rs.getString(STREET_NAME));
		address.setCityName(rs.getString(CITY_NAME));
		address.setStatename(rs.getString(STATE_NAME));
		address.setZipCode(rs.getString(ZIP_CODE));
		address.setCountyName(rs.getString(COUNTY_NAME));
		
		if (rs.getDate(APP_SUBMITTED_DATE) != null){
			address.setAppSubmittedDate(new java.util.Date(rs.getDate(APP_SUBMITTED_DATE).getTime()));
		}else{
			address.setAppSubmittedDate(null);
		}
		
		return address;

	}

	private BenefitChangeInfo populateChangeForm(ResultSet rs) throws SQLException {
		logger.debug("BenefitChangeDAO: populateChangeForm() begin.");
		BenefitChangeInfo benefitChangeInfo = new BenefitChangeInfo();
		benefitChangeInfo.setUserId(rs.getString(WEB_USER_ID));
		benefitChangeInfo.setApplicationId(rs.getString(APPLICATION_ID));
		benefitChangeInfo.setGroupId(rs.getString(GRGR_ID));
		benefitChangeInfo.setSubscriberId(rs.getString(SBSB_ID));
		benefitChangeInfo.setSubscriberLastName(rs.getString(SBSB_LAST_NAME));
		benefitChangeInfo.setSubscriberFirstName(rs
				.getString(SBSB_FIRST_NAME));
		benefitChangeInfo.setSubscriberMiddleName(rs
				.getString(SBSB_MIDDLE_NAME));
		
		if (rs.getDate(SBSB_DATE_OF_BIRTH) != null){
			benefitChangeInfo.setSubscriberDateOfBirth(new java.util.Date(rs.getDate(SBSB_DATE_OF_BIRTH).getTime()));
		}else{
			benefitChangeInfo.setSubscriberDateOfBirth(null);
		}
		
		benefitChangeInfo.setUpdatedSubscriberLastName(rs
				.getString(UPDTD_SBSB_LAST_NAME));
		benefitChangeInfo.setUpdatedSubscriberFirstName(rs
				.getString(UPDTD_SBSB_FIRST_NAME));
		benefitChangeInfo.setUpdatedSubscriberMiddleName(rs.getString(UPDTD_SBSB_MIDDLE_NAME));
		benefitChangeInfo.setSubscriberReasonForNameChange(rs
				.getString(SBSB_RSN_FOR_NAME_CHG));
		benefitChangeInfo.setChangePhoneInd(rs.getString(CHG_PHONE_IND));

		benefitChangeInfo.setEmailAddress(rs.getString(EMAIL_ADDRESS));
		if (rs.getDate(SBSB_POLICY_CANCEL_DATE) != null){
			benefitChangeInfo.setSubscriberPolicyCancelDate(new java.util.Date(rs.getDate(SBSB_POLICY_CANCEL_DATE).getTime()));
		}else{
			benefitChangeInfo.setSubscriberPolicyCancelDate(null);
		}
		benefitChangeInfo.setSubscriberPolicyCancelReason(rs.getString(SBSB_POLICY_CANCEL_RSN));
		benefitChangeInfo.setCancelDentalVisionInd(rs
				.getString(CANCEL_DEN_VIS_IND));

		benefitChangeInfo.setTobaccoUsageInd(rs.getString(TOB_USG_CHG));
		benefitChangeInfo.setTobaccoUsageSubscriberInd(rs
				.getString(TOB_USG_SBSB_IND));
		benefitChangeInfo.setTobaccoUsageSpouseInd(rs
				.getString(TOB_USG_SPOUSE_IND));
		benefitChangeInfo.setTermSubscriberInd(rs.getString(TERM_SBSB_IND));
		benefitChangeInfo.setTermSubscriberNewIdRsnCd(rs
				.getString(TERM_SUB_NEWID_RSN_CD));

		benefitChangeInfo.setTermSubscriberNewIdRsnDesc(rs
				.getString(TERM_SUB_NEWID_RSN_DESC));
		benefitChangeInfo.setSubscAddDelSecInd(rs
				.getString(SBSB_ADD_DEL_SEC_IND));
		benefitChangeInfo.setTermLifeCovInd(rs.getString(TERM_LIFE_COV_IND));
		benefitChangeInfo.setAddDelAncPrdInd(rs
				.getString(ADD_DEL_ANC_PRD_IND));

		benefitChangeInfo.setSubscriberVisionInd(rs
				.getString(SBSB_VISION_IND));
		benefitChangeInfo.setSubscriberVisionChangeInd(rs
				.getString(SBSB_VISION_CHG_IND));
		benefitChangeInfo.setSubscriberVisionExamOnlyInd(rs
				.getString(SBSB_VISION_EXAM_ONLY_IND));

		benefitChangeInfo.setSubscriberVisExamMatrlsInd(rs
				.getString(SBSB_VIS_EXAM_MATRLS_IND));
		benefitChangeInfo.setDentalInd(rs.getString(DEN_IND));
		benefitChangeInfo.setDentalAddInd(rs.getString(DEN_ADD_IND));
		benefitChangeInfo.setDentalRemInd(rs.getString(DEN_REM_IND));

		benefitChangeInfo.setChangeBenInd(rs.getString(CHG_BEN_IND));
		benefitChangeInfo.setBenifitPlan(rs.getString(BEN_PLAN));
		benefitChangeInfo.setBenefitNetwork(rs.getString(BEN_NETWORK));
		
		if (rs.getDate(BEN_CHG_EVENT_DATE) != null){
			benefitChangeInfo.setBenefitChangeEventDate(new java.util.Date(rs.getDate(BEN_CHG_EVENT_DATE).getTime()));
		}else{
			benefitChangeInfo.setBenefitChangeEventDate(null);
		}
		
		
		benefitChangeInfo
				.setRsnOpenEnrollInd(rs.getString(R_OPEN_ENROLL_IND));
		benefitChangeInfo.setRsnBrthAdpStrCrInd(rs
				.getString(R_BTHADPFSTRCR_IND));
		benefitChangeInfo.setRsnPermanentMoveInd(rs
				.getString(R_PERMANENTMOVE_IND));
		benefitChangeInfo.setRsnNonCalyrPolExpInd(rs
				.getString(R_NONCALYRPOLEXP_IND));
		benefitChangeInfo.setRsnMrgInd(rs.getString(R_MRG_IND));
		benefitChangeInfo.setRsnLossOfDepInd(rs.getString(R_LOSS_OF_DEP_IND));
		benefitChangeInfo.setRsnLossOfMnHlthInd(rs
				.getString(R_LOSS_OF_MINHLT_IND));
		benefitChangeInfo.setRsnRedOfHrsInd(rs.getString(R_REDOFHRS_IND));
		benefitChangeInfo.setRsnGainDepInd(rs.getString(R_GAIN_DEP_IND));

		benefitChangeInfo.setStdEffGuidelinesInd(rs
				.getString(STD_EFF_GUIDELINES_IND));
		benefitChangeInfo.setFirstMonthEffDateInd(rs
				.getString(FIRST_MNTH_EFFDT_IND));
		benefitChangeInfo.setEventDateInd(rs.getString(EVENT_DT_IND));
		benefitChangeInfo.setChangePInfoInd(rs.getString(CHG_PINFO_IND));
		benefitChangeInfo.setChangeNameInd(rs.getString(CHG_NAME_IND));
		benefitChangeInfo.setChangeEmailAddrInd(rs
				.getString(CHG_EMAIL_ADDR_IND));
		benefitChangeInfo.setSubscriberDaytimePhone(rs
				.getString(SBSB_DAYTIME_PHONE));

		benefitChangeInfo.setTermPolicyInd(rs.getString(TERM_POLICY_IND));
		benefitChangeInfo.setAddRemDepInd(rs.getString(ADD_REM_DEP_IND));
		
		
		if (rs.getDate(APP_SUBMITTED_DATE) != null){
			benefitChangeInfo.setApplSubmittedDate(new java.util.Date(rs.getDate(APP_SUBMITTED_DATE).getTime()));
		}else{
			benefitChangeInfo.setApplSubmittedDate(null);
		}		
		
		benefitChangeInfo.setApplStatusCode(rs.getString(APP_STATUS_CD));
		benefitChangeInfo.setMedicalRatePerMonth(rs.getFloat(MED_RATE_PER_MONTH));
		benefitChangeInfo.setDentalRatePerMonth(rs.getFloat(DEN_RATE_PER_MONTH));
		benefitChangeInfo.setVisionRatePerMonth(rs.getFloat(VIS_RATE_PER_MONTH));
		
		benefitChangeInfo.setDentalPlanName(rs.getString(DEN_PLAN_NAME));
		benefitChangeInfo.setVisionPlanName(rs.getString(VIS_PLAN_NAME));
		benefitChangeInfo.setChangePolicyind(rs.getString(CHG_POLICY_IND));
		benefitChangeInfo.setChangeAddressind(rs.getString(CHG_ADDR_IND));
		benefitChangeInfo.setVisionDelInd(rs.getString(VIS_REM_IND));
		benefitChangeInfo.setMedicalPlanName(rs.getString(MED_PLAN_NAME));
		
		if (rs.getDate(SBSB_MED_CANCEL_DATE) != null){
			benefitChangeInfo.setSubscriberMedicalCancelDate(new java.util.Date(rs.getDate(SBSB_MED_CANCEL_DATE).getTime()));
		}else{
			benefitChangeInfo.setSubscriberMedicalCancelDate(null);
		}
		
		if (rs.getDate(SBSB_DEN_CANCEL_DATE) != null){
			benefitChangeInfo.setSubscriberDentalCancelDate(new java.util.Date(rs.getDate(SBSB_DEN_CANCEL_DATE).getTime()));
		}else{
			benefitChangeInfo.setSubscriberDentalCancelDate(null);
		}
		
		if (rs.getDate(SBSB_VIS_CANCEL_DATE) != null){
			benefitChangeInfo.setSubscriberVisionCancelDate(new java.util.Date(rs.getDate(SBSB_VIS_CANCEL_DATE).getTime()));
		}else{
			benefitChangeInfo.setSubscriberVisionCancelDate(null);
		}
		
		benefitChangeInfo.setSubscriberMedicalCancelReason(rs.getString(SBSB_MED_CANCEL_RSN));
		benefitChangeInfo.setSubscriberDentalCancelReason(rs.getString(SBSB_DEN_CANCEL_RSN));
		benefitChangeInfo.setSubscriberVisionCancelReason(rs.getString(SBSB_VIS_CANCEL_RSN));
		
		benefitChangeInfo.setMedicalSBCLoc(rs.getString(MED_SBC_ENG_LOC));
		benefitChangeInfo.setDentalSBCLoc(rs.getString(DEN_SBC_ENG_LOC));
		benefitChangeInfo.setVisionSBCLoc(rs.getString(VIS_SBC_ENG_LOC));
		
		benefitChangeInfo.setRepId(rs.getString(REP_ID));
		benefitChangeInfo.setUserType(rs.getString(USER_TYPE));
		benefitChangeInfo.setRepresentativeName(rs.getString(REPRESENTATIVE_NAME));
		benefitChangeInfo.setRepresentativeEmail(rs.getString("REPRESENTATIVE_EMAIL"));

		benefitChangeInfo.setRsnAccessToICHRAInd(rs.getString(R_GAIN_ICHRA_QSEHRA_IND));
		benefitChangeInfo.setFirstDayMthFollowingSubmInd(rs.getString(FRST_DAY_OF_MNTH_FLW_SUBM_IND));
		
		return benefitChangeInfo;

	}
	
	private void populateBenefitChangeInfoPreparedStatement(PreparedStatement ps, BenefitChangeInfo benefitChangeInfo, String applicationId) throws SQLException {
		logger.debug("BenefitChangeDAO: populatePreparedStatement() begin.");
		ps.setString(1, trimString(benefitChangeInfo.getUserId()));
		ps.setString(2,trimString(applicationId));// generated from database.
		ps.setString(3,trimString(benefitChangeInfo.getGroupId()));
		ps.setString(4,trimString(benefitChangeInfo.getSubscriberId()));
		ps.setString(5,trimString(benefitChangeInfo.getSubscriberLastName()));
		ps.setString(6,trimString(benefitChangeInfo.getSubscriberFirstName()));
		ps.setString(7,trimString(benefitChangeInfo.getSubscriberMiddleName()));
		if (benefitChangeInfo.getSubscriberDateOfBirth() != null){
			ps.setDate(8, new java.sql.Date(benefitChangeInfo.getSubscriberDateOfBirth().getTime()));
		}else{
			ps.setDate(8, null);
		}
		
		ps.setString(9,trimString(benefitChangeInfo.getUpdatedSubscriberLastName()));
		ps.setString(10,trimString(benefitChangeInfo.getUpdatedSubscriberFirstName()));
		ps.setString(11,trimString(benefitChangeInfo.getUpdatedSubscriberMiddleName()));
		ps.setString(12,trimString(benefitChangeInfo.getSubscriberReasonForNameChange()));
		ps.setString(13,trimString(benefitChangeInfo.getChangePhoneInd()));
		ps.setString(14,trimString(benefitChangeInfo.getEmailAddress()));
		if (benefitChangeInfo.getSubscriberPolicyCancelDate() != null){
			ps.setDate(15, new java.sql.Date(benefitChangeInfo.getSubscriberPolicyCancelDate().getTime()));
		}else{
			ps.setDate(15, null);
		}
		ps.setString(16,trimString(benefitChangeInfo.getSubscriberPolicyCancelReason()));
		ps.setString(17,trimString(benefitChangeInfo.getCancelDentalVisionInd()));
		ps.setString(18,trimString(benefitChangeInfo.getTobaccoUsageInd()));
		ps.setString(19,trimString(benefitChangeInfo.getTobaccoUsageSubscriberInd()));
		ps.setString(20,trimString(benefitChangeInfo.getTobaccoUsageSpouseInd()));
		ps.setString(21,trimString(benefitChangeInfo.getTermSubscriberInd()));
		ps.setString(22,trimString(benefitChangeInfo.getTermSubscriberNewIdRsnCd()));

		ps.setString(23,trimString(benefitChangeInfo.getTermSubscriberNewIdRsnDesc()));
		ps.setString(24,trimString(benefitChangeInfo.getSubscAddDelSecInd()));
		ps.setString(25,trimString(benefitChangeInfo.getTermLifeCovInd()));
		ps.setString(26,trimString(benefitChangeInfo.getAddDelAncPrdInd()));

		ps.setString(27,trimString(benefitChangeInfo.getSubscriberVisionInd()));
		ps.setString(28,trimString(benefitChangeInfo.getSubscriberVisionChangeInd()));
		ps.setString(29,trimString(benefitChangeInfo.getSubscriberVisionExamOnlyInd()));

		ps.setString(30,trimString(benefitChangeInfo.getSubscriberVisExamMatrlsInd()));
		ps.setString(31,trimString(benefitChangeInfo.getDentalInd()));
		ps.setString(32,trimString(benefitChangeInfo.getDentalAddInd()));
		ps.setString(33,trimString(benefitChangeInfo.getDentalRemInd()));
		ps.setString(34,trimString(benefitChangeInfo.getChangeBenInd()));
		ps.setString(35,trimString(benefitChangeInfo.getBenifitPlan()));
		ps.setString(36,trimString(benefitChangeInfo.getBenefitNetwork()));
		if (benefitChangeInfo.getBenefitChangeEventDate() != null){
			ps.setDate(37, new java.sql.Date(benefitChangeInfo.getBenefitChangeEventDate().getTime()));
		}else{
			ps.setDate(37, null);
		}
		ps.setString(38,trimString(benefitChangeInfo.getRsnOpenEnrollInd()));
		ps.setString(39,trimString(benefitChangeInfo.getRsnBrthAdpStrCrInd()));
		ps.setString(40,trimString(benefitChangeInfo.getRsnPermanentMoveInd()));
		ps.setString(41,trimString(benefitChangeInfo.getRsnNonCalyrPolExpInd()));
		ps.setString(42,trimString(benefitChangeInfo.getRsnMrgInd()));
		ps.setString(43,trimString(benefitChangeInfo.getRsnLossOfDepInd()));
		ps.setString(44,trimString(benefitChangeInfo.getRsnLossOfMnHlthInd()));
		ps.setString(45,trimString(benefitChangeInfo.getRsnRedOfHrsInd()));
		ps.setString(46,trimString(benefitChangeInfo.getRsnGainDepInd()));

		ps.setString(47,trimString(benefitChangeInfo.getStdEffGuidelinesInd()));
		ps.setString(48,trimString(benefitChangeInfo.getFirstMonthEffDateInd()));
		ps.setString(49,trimString(benefitChangeInfo.getEventDateInd()));
		ps.setString(50,trimString(benefitChangeInfo.getChangePInfoInd()));
		ps.setString(51,trimString(benefitChangeInfo.getChangeNameInd()));
		ps.setString(52,trimString(benefitChangeInfo.getChangeEmailAddrInd()));
		ps.setString(53,trimString(benefitChangeInfo.getSubscriberDaytimePhone()));

		ps.setString(54,trimString(benefitChangeInfo.getTermPolicyInd()));
		ps.setString(55,trimString(benefitChangeInfo.getAddRemDepInd()));
		
		if(benefitChangeInfo.getApplSubmittedDate() != null){
		 ps.setDate(56,new java.sql.Date(benefitChangeInfo.getApplSubmittedDate().getTime()));
		}else{
			ps.setDate(56, null);
		}
		ps.setString(57,trimString(benefitChangeInfo.getApplStatusCode()));
		ps.setFloat(58,benefitChangeInfo.getMedicalRatePerMonth());
		ps.setFloat(59,benefitChangeInfo.getDentalRatePerMonth());
		ps.setFloat(60,benefitChangeInfo.getVisionRatePerMonth());
		ps.setString(61, trimString(benefitChangeInfo.getDentalPlanName()));
		ps.setString(62, trimString(benefitChangeInfo.getVisionPlanName()));
		ps.setString(63, trimString(benefitChangeInfo.getChangePolicyind()));
		ps.setString(64, trimString(benefitChangeInfo.getChangeAddressind()));
		ps.setString(65, trimString(benefitChangeInfo.getVisionDelInd()));
		ps.setString(66, trimString(benefitChangeInfo.getMedicalPlanName()));
		
		if(benefitChangeInfo.getSubscriberMedicalCancelDate() != null){
			 ps.setDate(67,new java.sql.Date(benefitChangeInfo.getSubscriberMedicalCancelDate().getTime()));
		}
		else{
			ps.setDate(67, null);
		}
		ps.setString(68, trimString(benefitChangeInfo.getSubscriberMedicalCancelReason()));
		
		if(benefitChangeInfo.getSubscriberDentalCancelDate() != null){
			 ps.setDate(69,new java.sql.Date(benefitChangeInfo.getSubscriberDentalCancelDate().getTime()));
		}
		else{
			ps.setDate(69, null);
		}
		ps.setString(70, trimString(benefitChangeInfo.getSubscriberDentalCancelReason()));
		
		if(benefitChangeInfo.getSubscriberVisionCancelDate() != null){
			 ps.setDate(71,new java.sql.Date(benefitChangeInfo.getSubscriberVisionCancelDate().getTime()));
		}
		else{
			ps.setDate(71, null);
		}
		ps.setString(72, trimString(benefitChangeInfo.getSubscriberVisionCancelReason()));
		
		ps.setString(73, trimString(benefitChangeInfo.getMedicalSBCLoc()));
		ps.setString(74, trimString(benefitChangeInfo.getDentalSBCLoc()));
		ps.setString(75, trimString(benefitChangeInfo.getVisionSBCLoc()));
		ps.setString(76, trimString(benefitChangeInfo.getRepId()));
		ps.setString(77, trimString(benefitChangeInfo.getUserType()));
		ps.setString(78, trimString(benefitChangeInfo.getRepresentativeName()));
		ps.setString(79, trimString(benefitChangeInfo.getRepresentativeEmail()));

		ps.setString(80, trimString(benefitChangeInfo.getRsnAccessToICHRAInd()));
		ps.setString(81, trimString(benefitChangeInfo.getFirstDayMthFollowingSubmInd()));
	}
	
	private void populateUpdatedBenefitChangeInfoPS(PreparedStatement ps, BenefitChangeInfo benefitChangeInfo) throws SQLException {
		logger.debug("BenefitChangeDAO: populateUpdatedBenefitChangeInfoPS() begin.");
		ps.setString(1, benefitChangeInfo.getUserId());
		ps.setString(2,benefitChangeInfo.getApplicationId());
		ps.setString(3,benefitChangeInfo.getGroupId());
		ps.setString(4,benefitChangeInfo.getSubscriberId());
		ps.setString(5,benefitChangeInfo.getSubscriberLastName());
		ps.setString(6,benefitChangeInfo.getSubscriberFirstName());
		ps.setString(7,benefitChangeInfo.getSubscriberMiddleName());
		if (benefitChangeInfo.getSubscriberDateOfBirth() != null){
			ps.setDate(8, new java.sql.Date(benefitChangeInfo.getSubscriberDateOfBirth().getTime()));
		}else{
			ps.setDate(8, null);
		}
		
		ps.setString(9,benefitChangeInfo.getUpdatedSubscriberLastName());
		ps.setString(10,benefitChangeInfo.getUpdatedSubscriberFirstName());
		ps.setString(11,benefitChangeInfo.getUpdatedSubscriberMiddleName());
		ps.setString(12,benefitChangeInfo.getSubscriberReasonForNameChange());
		ps.setString(13,benefitChangeInfo.getChangePhoneInd());
		ps.setString(14,benefitChangeInfo.getEmailAddress());
		if (benefitChangeInfo.getSubscriberPolicyCancelDate() != null){
			ps.setDate(15, new java.sql.Date(benefitChangeInfo.getSubscriberPolicyCancelDate().getTime()));
		}else{
			ps.setDate(15, null);
		}
		ps.setString(16,benefitChangeInfo.getSubscriberPolicyCancelReason());
		ps.setString(17,benefitChangeInfo.getCancelDentalVisionInd());
		ps.setString(18,benefitChangeInfo.getTobaccoUsageInd());
		ps.setString(19,benefitChangeInfo.getTobaccoUsageSubscriberInd());
		ps.setString(20,benefitChangeInfo.getTobaccoUsageSpouseInd());
		ps.setString(21,benefitChangeInfo.getTermSubscriberInd());
		ps.setString(22,benefitChangeInfo.getTermSubscriberNewIdRsnCd());

		ps.setString(23,benefitChangeInfo.getTermSubscriberNewIdRsnDesc());
		ps.setString(24,benefitChangeInfo.getSubscAddDelSecInd());
		ps.setString(25,benefitChangeInfo.getTermLifeCovInd());
		ps.setString(26,benefitChangeInfo.getAddDelAncPrdInd());

		ps.setString(27,benefitChangeInfo.getSubscriberVisionInd());
		ps.setString(28,benefitChangeInfo.getSubscriberVisionChangeInd());
		ps.setString(29,benefitChangeInfo.getSubscriberVisionExamOnlyInd());

		ps.setString(30,benefitChangeInfo.getSubscriberVisExamMatrlsInd());
		ps.setString(31,benefitChangeInfo.getDentalInd());
		ps.setString(32,benefitChangeInfo.getDentalAddInd());
		ps.setString(33,benefitChangeInfo.getDentalRemInd());
		ps.setString(34,benefitChangeInfo.getChangeBenInd());
		ps.setString(35,benefitChangeInfo.getBenifitPlan());
		ps.setString(36,benefitChangeInfo.getBenefitNetwork());
		if (benefitChangeInfo.getBenefitChangeEventDate() != null){
			ps.setDate(37, new java.sql.Date(benefitChangeInfo.getBenefitChangeEventDate().getTime()));
		}else{
			ps.setDate(37, null);
		}
		ps.setString(38,benefitChangeInfo.getRsnOpenEnrollInd());
		ps.setString(39,benefitChangeInfo.getRsnBrthAdpStrCrInd());
		ps.setString(40,benefitChangeInfo.getRsnPermanentMoveInd());
		ps.setString(41,benefitChangeInfo.getRsnNonCalyrPolExpInd());
		ps.setString(42,benefitChangeInfo.getRsnMrgInd());
		ps.setString(43,benefitChangeInfo.getRsnLossOfDepInd());
		ps.setString(44,benefitChangeInfo.getRsnLossOfMnHlthInd());
		ps.setString(45,benefitChangeInfo.getRsnRedOfHrsInd());
		ps.setString(46,benefitChangeInfo.getRsnGainDepInd());

		ps.setString(47,benefitChangeInfo.getStdEffGuidelinesInd());
		ps.setString(48,benefitChangeInfo.getFirstMonthEffDateInd());
		ps.setString(49,benefitChangeInfo.getEventDateInd());
		ps.setString(50,benefitChangeInfo.getChangePInfoInd());
		ps.setString(51,benefitChangeInfo.getChangeNameInd());
		ps.setString(52,benefitChangeInfo.getChangeEmailAddrInd());
		ps.setString(53,benefitChangeInfo.getSubscriberDaytimePhone());

		ps.setString(54,benefitChangeInfo.getTermPolicyInd());
		ps.setString(55,benefitChangeInfo.getAddRemDepInd());
		
		if(benefitChangeInfo.getApplSubmittedDate() != null){
		 ps.setDate(56,new java.sql.Date(benefitChangeInfo.getApplSubmittedDate().getTime()));
		}else{
			ps.setDate(56, null);
		}
		ps.setString(57,benefitChangeInfo.getApplStatusCode());
		ps.setFloat(58,benefitChangeInfo.getMedicalRatePerMonth());
		ps.setFloat(59,benefitChangeInfo.getDentalRatePerMonth());
		ps.setFloat(60,benefitChangeInfo.getVisionRatePerMonth());
		ps.setString(61, benefitChangeInfo.getDentalPlanName());
		ps.setString(62, benefitChangeInfo.getVisionPlanName());
		ps.setString(63, benefitChangeInfo.getChangePolicyind());
		ps.setString(64, benefitChangeInfo.getChangeAddressind());
		ps.setString(65, benefitChangeInfo.getVisionDelInd());
		ps.setString(66, trimString(benefitChangeInfo.getMedicalPlanName()));
		
		if(benefitChangeInfo.getSubscriberMedicalCancelDate() != null){
			 ps.setDate(67,new java.sql.Date(benefitChangeInfo.getSubscriberMedicalCancelDate().getTime()));
		}else{
			ps.setDate(67, null);
		}
		
		ps.setString(68, trimString(benefitChangeInfo.getSubscriberMedicalCancelReason()));
		
		if(benefitChangeInfo.getSubscriberDentalCancelDate() != null){
			 ps.setDate(69,new java.sql.Date(benefitChangeInfo.getSubscriberDentalCancelDate().getTime()));
		}else{
			ps.setDate(69, null);
		}
		ps.setString(70, trimString(benefitChangeInfo.getSubscriberDentalCancelReason()));
		
		if(benefitChangeInfo.getSubscriberVisionCancelDate() != null){
			 ps.setDate(71,new java.sql.Date(benefitChangeInfo.getSubscriberVisionCancelDate().getTime()));
		}else{
			ps.setDate(71, null);
		}
		ps.setString(72, trimString(benefitChangeInfo.getSubscriberVisionCancelReason()));

		ps.setString(73, trimString(benefitChangeInfo.getMedicalSBCLoc()));
		ps.setString(74, trimString(benefitChangeInfo.getDentalSBCLoc()));
		ps.setString(75, trimString(benefitChangeInfo.getVisionSBCLoc()));
		ps.setString(76, trimString(benefitChangeInfo.getRepId()));
		ps.setString(77, trimString(benefitChangeInfo.getUserType()));
		ps.setString(78, trimString(benefitChangeInfo.getRepresentativeName()));
		ps.setString(79, trimString(benefitChangeInfo.getRepresentativeEmail()));

		ps.setString(80, trimString(benefitChangeInfo.getRsnAccessToICHRAInd()));
		ps.setString(81, trimString(benefitChangeInfo.getFirstDayMthFollowingSubmInd()));
	}

	private String trimString(String string){
		if (string != null){
			return string.trim();
		}
		return string;
	}
	
	
	// Additional DAO methods which are not being used.
	
	/**
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean saveAddressDetails(List<Address> addressList, String applicationId) {
		logger.debug("BenefitChangeDAO: saveAddressDetails() executed.");
		boolean updated = false;
		if (addressList == null || addressList.isEmpty() || applicationId == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO INDIV_ENRL_MBR_CHG_ADDR ");
		sql.append("(WebUserID,Application_ID,SBSB_ID,SBSB_ADDR_IND,STREET_NAME,CITY_NAME,STATE_NAME ");
		sql.append(",ZIP_CODE ,COUNTY_NAME ,APP_SUBMITTED_DATE) ");
		sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?)");

		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			for(Address address: addressList){
				populateAddressPreparedStatement(ps, address, applicationId);
			}
			ps.setQueryTimeout(10);
			int[]  result = ps.executeBatch();
			conn.commit();
			if (result != null){
				for (int i=0; i< result.length; i++){
					logger.debug("ProductDAO: saveAddressDetails() completed for: " + applicationId + " result: "+ result[i]);
				}
			}				
			updated = true; // setting it to true, assuming there are no errors.

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: saveAddressDetails() ended." + updated);
		return updated;
	}
	
	/**
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean saveDependentDetails(List<Dependent> dependentsList, String applicationId) {
		logger.debug("BenefitChangeDAO: saveDependentDetails() executed.");
		boolean updated = false;
		if (dependentsList == null || dependentsList.isEmpty() || applicationId == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO INDIV_ENRL_CHG_DEPENDENT ");
		sql.append("(WebUserID,Application_ID,DEP_SEQ_NO,DEP_LAST_NAME,DEP_FIRST_NAME,DEP_MI_NAME ");
		sql.append(", DEP_GENDER,DEP_DATE_OF_BIRTH,DEP_SSN,DEP_TOBACCO_USAGE_IND ");
		sql.append(", DEP_RELATIONSHIP_TYPE_IND,DEP_RELATIONSHIP_DESC,DEP_CHANGE_TYPE_IND ");
		sql.append(", DEP_MEDICARE_ELIG_IND,DEP_TERM_DATE,DEP_TERM_REASON ");
		sql.append(", SBSB_ID,APP_SUBMITTED_DATE) ");
		sql.append("  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			ps.setQueryTimeout(15);
			for(Dependent dependent: dependentsList){
				populateDependentPreparedStatement(ps, dependent, applicationId);
			}
			
			int[] result = ps.executeBatch();
			conn.commit();
			if (result != null){
				for (int i=0; i< result.length; i++){
					logger.debug("ProductDAO:  saveDependentDetails() completed for: " + applicationId + " result: "+ result[i]);
				}
			}				
			updated = true; // setting it to true, assuming there are no errors.

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: saveDependentDetails() ended." + updated);
		return updated;
	}
	
	/**
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean deleteAddressDetails(String applicationId, String subscriberId) {
		logger.debug("BenefitChangeDAO: deleteAddressDetails() executed.");
		boolean success = false;
		if (applicationId == null || subscriberId == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM INDIV_ENRL_MBR_CHG_ADDR ");
		sql.append(" WHERE Application_ID = ? and SBSB_ID = ?");
		

		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());			
			ps.setString(1,applicationId.trim());
			ps.setString(2,subscriberId.trim());
			ps.setQueryTimeout(10);
			int rowsUpdCount = 0;
			rowsUpdCount = ps.executeUpdate();
			conn.commit();
			if (rowsUpdCount > 0){
				success = true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: deleteAddressDetails() ended." + success);
		return success;
	}

	/**
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean deleteDependentDetails(String applicationId, String subscriberId) {
		logger.debug("BenefitChangeDAO: deleteDependentDetails() executed.");
		boolean success = false;
		if (applicationId == null || subscriberId == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM INDIV_ENRL_CHG_DEPENDENT ");
		sql.append(" WHERE Application_ID = ? and SBSB_ID = ?");
		
		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());			
			ps.setString(1,applicationId.trim());
			ps.setString(2,subscriberId.trim());
			ps.setQueryTimeout(10);
			int rowsUpdCount = 0;
			rowsUpdCount = ps.executeUpdate();
			conn.commit();
			if (rowsUpdCount > 0){
				success = true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: deleteDependentDetails() ended." + success);
		return success;
	}
	
	/**
	 * @param benefitChangeInfo
	 * @return
	 */
	public boolean updateAddressDetails(Address address) {
		logger.debug("BenefitChangeDAO: updateAddressDetails() executed.");
		boolean success = false;
		if (address == null){
			throw new BenefitChangeException(INVALID_INPUT);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE INDIV_ENRL_MBR_CHG_ADDR ");
		sql.append("SET WebUserID = ?, Application_ID = ?, SBSB_ID = ?, SBSB_ADDR_IND = ?");
		sql.append(" ,STREET_NAME = ?, CITY_NAME = ?, STATE_NAME = ?, ZIP_CODE = ? ");
		sql.append(" ,COUNTY_NAME = ?, APP_SUBMITTED_DATE = ?   ");
		sql.append(" WHERE Application_ID = ? AND SBSB_ADDR_IND = ?");

		if (benefitChangeContext.getEnrlbftchgDS() == null) {
			throw new BenefitChangeException(DATASOURCE_ISSUE);
		}

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			populateUpdatedAddressPS(ps, address);
			ps.setString(11,address.getApplicationId());
			ps.setString(12,address.getSubscriberAddressInd());
			ps.setQueryTimeout(10);
			int rowsUpdCount = 0;
			rowsUpdCount = ps.executeUpdate();
			conn.commit();
			if (rowsUpdCount > 0){
				success = true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("BenefitChangeDAO: updateAddressDetails() ended." + success);
		return success;
	}

}
