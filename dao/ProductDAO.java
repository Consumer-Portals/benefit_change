package com.bcbst.benefitchange.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bcbst.applogger.core.ApplicationSettings;
import com.bcbst.benefitchange.core.BenefitChangeContext;
import com.bcbst.benefitchange.dto.EnrollmentDate;
import com.bcbst.benefitchange.dto.PlanSearchMember;
import com.bcbst.benefitchange.dto.Product;
import com.bcbst.benefitchange.util.BenefitChangeException;
import com.bcbst.benefitchange.util.FieldsValidator;
import com.bcbst.benefitchange.util.MetalLevel;

/**
 * @objective This class has DAO methods for getting counties/zipcodes/plan details required for Benefit Change Form.
 * @author Vijay Narsingoju
 *
 */
@Named
public class ProductDAO {
	@Inject
	private BenefitChangeContext benefitChangeContext;
	
	private static final String SBC_PLAN_CODE = "SBC_PLAN_CODE";
	private static final String CSPI_ID = "CSPI_ID";
	private static final String SBC_PLAN_ID = "SBC_PLAN_ID";
	private static final String TERM_DATE = "TERM_DATE";
	private static final String EFF_DATE = "EFF_DATE";
	private static final String GRGR_ID = "GRGR_ID";
	private Logger logger = LogManager.getLogger(ProductDAO.class);
	private static final String PLAN_NAME_PREFIX = "BlueCross ";
	 
	/**
	 * @objective This method will return all the counties for a given zipcode.
	 * @param applicationId
	 * @return
	 */
	public List<String> getCountyList(String zipcode) {
		logger.debug("ProductDAO: getCountyList() executed.");
		List<String> countyList = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// validate input
		if (FieldsValidator.isEmpty(zipcode)) {
			throw new BenefitChangeException("Invalid zipcode");
		}
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("select COUNTY_NAME from ZIP_COUNTY_MAPPING where ZIP_CODE = ?");
			ps.setString(1, zipcode);
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			String countyName = null;
			countyList = new ArrayList<>();
			while (rs.next()) {
				countyName = rs.getString("COUNTY_NAME");
				if (countyName != null){
				countyList.add(countyName.trim());
			}	
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
		logger.debug("ProductDAO: getCountyList() ended.");
		return countyList;
	}
	
	/**
	 * @objective This method will get all the zipcodes within TN
	 * @return
	 */
	public List<String> getZipCodes(){
		logger.debug("ProductDAO: getZipCodes() executed.");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> zipCodesList = null;
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("select distinct ZIP_CODE from ZIP_COUNTY_MAPPING");
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			String zipcode = null;
			zipCodesList = new ArrayList<>();
			while (rs.next()) {
				zipcode = rs.getString("ZIP_CODE");
				if (zipcode != null){
					zipCodesList.add(zipcode.trim());
			}	
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
		logger.debug("ProductDAO: getZipCodes() ended.");
		return zipCodesList;
	}
	
	/**
	 * @objective This method will get the Region code for a given county.
	 * @param applicationId
	 * @return
	 */
	public String getRegionCode(String county) {
		logger.debug("ProductDAO: getRegionCodes() executed. {}", StringEscapeUtils.escapeJava(county));
		String regionCode = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null; 
		// validate input
		if (FieldsValidator.isEmpty(county)) {
			throw new BenefitChangeException("Invalid county");			
		}

		try {
			String schemaName = ApplicationSettings.getSettingValue("productSchema");
			if(schemaName==null || schemaName.trim().length()==0){
				schemaName = "wfas1";
			}
			logger.debug("productSchema: "+ schemaName);
			
			StringBuilder query = new StringBuilder("select ASDB_MKTPLC_RGN_CD from "+ schemaName +".CPS_ASDB_MKTPLC_RGN_REF where UPPER(TRIM(ASDB_MKTPLC_RGN_CNTY_NME)) = ?");
			logger.debug("ProductDAO: getRegionCodes(): query: "+ query.toString());
			conn = benefitChangeContext.getBnfChgASDBDS().getConnection();
			ps = conn.prepareStatement(query.toString());
			String countyName = county.trim().toUpperCase();
			ps.setString(1, countyName);	
			ps.setQueryTimeout(10);
			
			rs = ps.executeQuery();
			
			if (rs.next() && rs.getString("ASDB_MKTPLC_RGN_CD") != null) {
				regionCode = rs.getString("ASDB_MKTPLC_RGN_CD").trim();
				logger.debug("ProductDAO: getRegionCode() resultset has data..");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closeResultSet(rs);
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("ProductDAO: getRegionCode() ended with regionCode: {}", StringEscapeUtils.escapeJava(regionCode));
		return regionCode;
	}
	
	/**
	 * @objective This method will get all the plans by region codes and effective/term dates from mapping table .
	 * @param applicationId
	 * @return
	 */
	public List<Product> getPlansByRegionCodes(String regionCode, java.util.Date effectiveDate, java.util.Date termDate) {
		logger.debug("ProductDAO: getPlansByRegionCodes() executed.{}", StringEscapeUtils.escapeJava(regionCode));
		List<Product> productList = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// validate input
		if (FieldsValidator.isEmpty(regionCode) || effectiveDate == null || termDate == null) {
			throw new BenefitChangeException("Invalid input");
		}

		try {
			
			StringBuilder query = new StringBuilder("SELECT CSPI_ID, REGION_CD ,GRGR_ID ,EFF_DATE ,TERM_DATE FROM REGION_PLAN_MAPPING where LTRIM(RTRIM(REGION_CD)) = ? and EFF_DATE = ? and TERM_DATE = ?");
			logger.debug("ProductDAO: getPlansByRegionCodes(): query: "+ query.toString());
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, regionCode.trim());	
			ps.setDate(2, new java.sql.Date(effectiveDate.getTime()));	
			ps.setDate(3, new java.sql.Date(termDate.getTime()));	
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			Product product = null;
			productList = new ArrayList<>();
			while (rs.next()) {
				product = new Product();
				product.setPlanId(rs.getString(CSPI_ID));
				product.setRegion(rs.getString("REGION_CD"));
				product.setGroupId(rs.getString(GRGR_ID));
				
				if (rs.getDate(EFF_DATE) != null){
					product.setEffectiveDate(new java.util.Date(rs.getDate(EFF_DATE).getTime()));
				}
				
				if (rs.getDate(TERM_DATE) != null){
					product.setTermDate(new java.util.Date(rs.getDate(TERM_DATE).getTime()));
				}
				logger.debug("PLANS Available::{}", StringEscapeUtils.escapeJava(product.getPlanId()));
				productList.add(product);				
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
		logger.debug("ProductDAO: getPlansByRegionCodes() ended.");
		return productList;
	}
	
	/**
	 * @objective This method will get all the plan details by planid's from SBC table using effective/term dates 
	 * @param applicationId
	 * @return
	 */
	public List<Product> getPlanDetailsByPlanIDs(List<String> planList, Date effectiveDate, Date termDate) {
		logger.debug("ProductDAO: getPlanDetailsByPlanIDs() executed. Effective date: " + effectiveDate + " Term date" + termDate);
		List<Product> productList = null;
				
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// validate input
		if (planList == null || planList.isEmpty() || effectiveDate == null || termDate == null) {
			throw new BenefitChangeException("Invalid input");
		}

		try {			
			StringBuilder query = new StringBuilder("SELECT CSPI_ID, SBC_PLAN_CODE, SBC_PLAN_ID, NETWORK, DED_IN_DESC, OV_COPAY, OOP_MAX, RX_COV, GRGR_ID, SPECIALIST_COPAY, SBC_ENG_LOC, EFF_DATE, TERM_DATE FROM SBC_PLAN_REF where EFF_DATE = ? and TERM_DATE = ? and CSPI_ID IN(");
			
			addQueryParametersPlanDetails(planList, query);
			
			query.append(") ORDER BY CSPI_ID");
			logger.debug("ProductDAO: getPlanDetailsByPlanIDs(): query: "+ query.toString());
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setDate(1, effectiveDate);	
			ps.setDate(2, termDate);	
			int paramIndex = 3;
			StringBuffer sb = new StringBuffer();
			for(String planid: planList){
				sb.append(planid + ",");
				ps.setString(paramIndex++, planid);	
			}
			logger.debug("PlanDetails {}", StringEscapeUtils.escapeJava(sb.toString()));
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			productList = new ArrayList<>();

			getPlanDetailsFromResultSet(productList, rs);
			rs.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		} finally {
			ResourceTool.closeResultSet(rs);
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("ProductDAO: getPlanDetailsByPlanIDs() ended.");
		return productList;
	}

	private void getPlanDetailsFromResultSet(List<Product> productList, ResultSet rs) throws SQLException {
		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setPlanId(rs.getString(CSPI_ID));
			product.setPlanName(PLAN_NAME_PREFIX + rs.getString(SBC_PLAN_CODE));
			
			if (rs.getString(SBC_PLAN_ID) != null){
				for (MetalLevel cName : MetalLevel.values()) {
					if (cName.name().equalsIgnoreCase(""+rs.getString(SBC_PLAN_ID).trim().charAt(0))){
						product.setMetalLevel(cName.getType());
						break;
					}
				}
			}else{
				product.setMetalLevel("");
			}
			
			product.setSbcPlanId(rs.getString(SBC_PLAN_ID));
			product.setNetwork(rs.getString("NETWORK"));
			product.setDeductible(rs.getString("DED_IN_DESC"));
			product.setOfficevisitcopay(rs.getString("OV_COPAY"));
			product.setOopmax(rs.getString("OOP_MAX"));
			product.setPrescriptioncoverage(rs.getString("RX_COV"));
			product.setGroupId(rs.getString(GRGR_ID));
			product.setSpecialistcopay(rs.getString("SPECIALIST_COPAY"));
			product.setSbcEnglishLoc(rs.getString("SBC_ENG_LOC"));
			
			if (rs.getDate(EFF_DATE) != null){
				product.setEffectiveDate(new java.util.Date(rs.getDate(EFF_DATE).getTime()));
			}
			
			if (rs.getDate(TERM_DATE) != null){
				product.setTermDate(new java.util.Date(rs.getDate(TERM_DATE).getTime()));
			}
			productList.add(product);				
		}
	}

	private void addQueryParametersPlanDetails(List<String> planList, StringBuilder query) {
		for(int i =0; i< planList.size(); i++){
			if (i != (planList.size() - 1)) {
				query.append("?, ");
			} else {
				query.append("?");
			}
		}
	}
	
	/**
	 * @objective This method is for getting effective and term dates for searching plans in SBC table.
	 * @return
	 */
	public List<EnrollmentDate> getEnrollmentDates() {
		logger.debug("ProductDAO: getEnrollmentDates() executed.");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<EnrollmentDate> enrollmentDates = null;

		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("select START_DATE, END_DATE, PLAN_EFF_DATE, PLAN_TERM_DATE from OPEN_ENRLL_DATES");
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			enrollmentDates = new ArrayList<>();
			EnrollmentDate enrollmentDate = null;
			while (rs.next()) {
				enrollmentDate = new EnrollmentDate();
				if (rs.getDate("START_DATE") != null){
					enrollmentDate.setStartDate(new java.util.Date(rs.getDate("START_DATE").getTime()));
				}
				if (rs.getDate("END_DATE") != null){
					enrollmentDate.setEndDate(new java.util.Date(rs.getDate("END_DATE").getTime()));
				}
				if (rs.getDate("PLAN_EFF_DATE") != null){
					enrollmentDate.setPlanEffectiveDate(new java.util.Date(rs.getDate("PLAN_EFF_DATE").getTime()));
				}
				if (rs.getDate("PLAN_TERM_DATE") != null){
					enrollmentDate.setPlanTermDate(new java.util.Date(rs.getDate("PLAN_TERM_DATE").getTime()));
				}
				enrollmentDates.add(enrollmentDate);
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
		logger.debug("ProductDAO: getEnrollmentDates() ended.");
		return enrollmentDates;
	}
	
	
	
	/**
	 * @objective This method will return all the members for a given application Id.
	 * @return
	 */
	public List<PlanSearchMember> getPlanSearchMembers(String applicationId) {
		logger.debug("ProductDAO: getPlanSearchMembers() executed.");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PlanSearchMember> planSearchMembers = null;
		// validate input
		if (FieldsValidator.isEmpty(applicationId)) {
			throw new BenefitChangeException("Invalid applicationId");
		}
		StringBuilder buffer = new StringBuilder("SELECT WebUserID ,Application_ID ,SEQ_NO ,LAST_NAME,FIRST_NAME,MI_NAME,GENDER,DATE_OF_BIRTH,TOBACCO_USAGE_IND,");
		buffer.append(" RELATIONSHIP_TYPE_IND, RELATIONSHIP_DESC,SBSB_ID ,APP_SUBMITTED_DATE,ZIP_CODE,COUNTY_NAME from INDIV_ENRL_CHG_PLAN_MEMBERS ");
		buffer.append(" WHERE Application_ID = ?");
		
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement(buffer.toString());
			ps.setQueryTimeout(10);
			ps.setString(1, applicationId);
			rs = ps.executeQuery();
			planSearchMembers = new ArrayList<>();
			PlanSearchMember planSearchMember = null;
			while (rs.next()) {
				planSearchMember = new PlanSearchMember();
				planSearchMember.setApplicationId(rs.getString("Application_ID"));
				planSearchMember.setDependentSeqNum(rs.getString("SEQ_NO"));
				planSearchMember.setLastName(rs.getString("LAST_NAME"));
				planSearchMember.setFirstName(rs.getString("FIRST_NAME"));
				planSearchMember.setMiddleName(rs.getString("MI_NAME"));
				planSearchMember.setGender(rs.getString("GENDER"));
				if (rs.getDate("DATE_OF_BIRTH") != null){
					planSearchMember.setDateofBirth(new java.util.Date(rs.getDate("DATE_OF_BIRTH").getTime()));
				}
				planSearchMember.setTobaccoUsageInd(rs.getString("TOBACCO_USAGE_IND"));
				planSearchMember.setRelationShipTypeInd(rs.getString("RELATIONSHIP_TYPE_IND"));
				planSearchMember.setRelationShipTypeDesc(rs.getString("RELATIONSHIP_DESC"));
				planSearchMember.setSubscriberId(rs.getString("SBSB_ID"));
				if (rs.getDate("APP_SUBMITTED_DATE") != null){
					planSearchMember.setAppSubmittedDate(new java.util.Date(rs.getDate("APP_SUBMITTED_DATE").getTime()));
				}
				
				planSearchMember.setZipcode(rs.getString("ZIP_CODE"));
				planSearchMember.setCountyName(rs.getString("COUNTY_NAME"));
				planSearchMembers.add(planSearchMember);
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
		logger.debug("ProductDAO: getPlanSearchMembers() ended..");
		return planSearchMembers;
	}
	
	
	
	
	
	/**
	 * @objective This method will get all the dental and vision plan details
	 * @param applicationId
	 * @return
	 */
	public List<Product> getDentalAndVisionPlans(String groupId, Date date) {
		logger.debug("ProductDAO: getDentalAndVisionPlans() executed.");
		List<Product> productList = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// validate input
		if (FieldsValidator.isEmpty(groupId)) {
			throw new BenefitChangeException("Invalid groupId");
		}

		try {
			StringBuilder query = new StringBuilder("SELECT PLAN_ID, PLAN_NAME, PRODUCT_TYPE, RATING_AREA,GRGR_ID,SBC_LOC FROM INDIV_ENRL_CHG_DEN_VIS_PLAN where GRGR_ID = ?");
			
			if(date!=null) {
				query.append(" AND EFF_DATE <= ? AND TERM_DATE >= ?");
			}
			
			logger.debug("ProductDAO: getDentalAndVisionPlans(): query: "+ query.toString());
			logger.debug("ProductDAO: getDentalAndVisionPlans(): date: "+ date);
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, groupId.trim());

			if(date!=null) {
				ps.setDate(2, date);
				ps.setDate(3, date);
			}
			
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			Product product = null;
			productList = new ArrayList<>();
			while (rs.next()) {
				product = new Product();
				product.setPlanId(rs.getString("PLAN_ID"));
				product.setPlanName(rs.getString("PLAN_NAME"));
				product.setProductType(rs.getString("PRODUCT_TYPE"));
				product.setRatingArea(rs.getInt("RATING_AREA"));
				product.setGroupId(rs.getString(GRGR_ID));
				product.setSbcEnglishLoc(rs.getString("SBC_LOC"));
				productList.add(product);				
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
		
		logger.debug("ProductDAO: productList.size() " + productList.size());
		
		logger.debug("ProductDAO: getDentalAndVisionPlans() ended. ");
		return productList;
	}

	// Additional DAO methods which are not being used.
	
	/**
	 *@objective This method will insert multiple members  
	 * @return
	 */
	public boolean savePlanSearchMembers(List<PlanSearchMember> planMembers, String applicationId, String subscriberId) {
		logger.debug("ProductDAO: savePlanSearchMembers() executed for: " + applicationId);
		boolean updated =false;
		Connection conn = null;
		PreparedStatement ps = null;
		StringBuilder buffer = new StringBuilder("INSERT INTO INDIV_ENRL_CHG_PLAN_MEMBERS(WebUserID ,Application_ID ,SEQ_NO ,FIRST_NAME,GENDER,DATE_OF_BIRTH,TOBACCO_USAGE_IND,");
		buffer.append(" RELATIONSHIP_TYPE_IND, SBSB_ID ,APP_SUBMITTED_DATE,ZIP_CODE,COUNTY_NAME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ");
		if(planMembers != null && !planMembers.isEmpty()){
			try {
				logger.debug("ProductDAO: savePlanSearchMembers() query : " + buffer.toString());
				conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(buffer.toString());
				ps.setQueryTimeout(15);
				for (PlanSearchMember member: planMembers){
					logger.debug("ProductDAO: savePlanSearchMembers() member dob: " + member.getDateofBirth());
					addPlanSearchMemberToSaveBatch(applicationId, subscriberId, ps, member);
					
				}
				
				ps.executeBatch();
				
				conn.commit();
				updated = true; // setting it to true, assuming there are no errors.
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				try {
					if (conn != null){
						conn.rollback();
					}
				} catch (SQLException e1) {
					logger.error(e.getMessage(), e);
				}				
			} finally {
				ResourceTool.closePreparedStatement(ps);
				ResourceTool.close(conn);
			}
		}
		logger.debug("ProductDAO: savePlanSearchMembers() ended..");
		return updated;
	}

	private void addPlanSearchMemberToSaveBatch(String applicationId, String subscriberId, PreparedStatement ps,
			PlanSearchMember member) throws SQLException {
		ps.setString(1, member.getUserId());
		ps.setString(2, applicationId);
		ps.setString(3, member.getDependentSeqNum());
		ps.setString(4, member.getFirstName());
		ps.setString(5, member.getGender());
		if (member.getDateofBirth() != null){
			ps.setDate(6, new java.sql.Date(member.getDateofBirth().getTime()));
		}
		ps.setString(7, member.getTobaccoUsageInd());
		ps.setString(8, member.getRelationShipTypeInd());
		ps.setString(9, subscriberId);
		if (member.getAppSubmittedDate() != null){
			ps.setDate(10, new java.sql.Date(member.getAppSubmittedDate().getTime()));
		}
		ps.setString(11, member.getZipcode());
		ps.setString(12, member.getCountyName());
		ps.addBatch();
	}
	
	/**
	 * @objective This method will delete plan search members for a given application id and subscriber id. It will consider seq numbers if provided.
	 * @return
	 */
	public boolean deletePlanSearchMembers(String applicationId, String subsciberId) {
		logger.debug("ProductDAO: deletePlanSearchMembers() executed.");
		Connection conn = null;
		PreparedStatement ps = null;
		int rowsUpdated = 0;
		boolean updated = false;
		StringBuilder buffer = new StringBuilder("Delete from INDIV_ENRL_CHG_PLAN_MEMBERS ");
		buffer.append(" WHERE Application_ID = ? and SBSB_ID = ? ");
		
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(buffer.toString());
			ps.setQueryTimeout(10);
			ps.setString(1, applicationId);
			ps.setString(2, subsciberId);
			rowsUpdated = ps.executeUpdate();
			conn.commit();
			if (rowsUpdated >0){
				updated = true;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e1) {
				logger.error(e.getMessage(), e);
			}
		} finally {
			ResourceTool.closePreparedStatement(ps);
			ResourceTool.close(conn);
		}
		logger.debug("ProductDAO: deletePlanSearchMembers() ended.."+ rowsUpdated);
		return updated;
	}

}
