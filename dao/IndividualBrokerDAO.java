package com.bcbst.benefitchange.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.Logger;

import com.bcbst.benefitchange.core.BenefitChangeContext;

import org.apache.logging.log4j.LogManager;

@RequestScoped
public class IndividualBrokerDAO {
	@Inject
	private BenefitChangeContext benefitChangeContext;
	
	public static final Logger logger = LogManager.getLogger(IndividualBrokerDAO.class.getName());
	
	public List<String> getCommissionIdList(String userid, List<String> groupFilter) {	
		List<String> result = new ArrayList<>();
		
		for(String group : groupFilter) {
			logger.debug("groups: {}", StringEscapeUtils.escapeJava(group));
		}

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		try {
			conn = benefitChangeContext.getFacetsDBDS().getConnection();
			
			String query = "SELECT distinct CPS_AGN_NO  " +
					"FROM CPS_AGN_ARNG_REF ref " +
					"join ER_TB_SYST_EXPM_XPERMISSIONS xperm on xperm.EXPM_DATA = ref.CPS_COMM_ENT_ID " +
					"where xperm.EXTU_ID = ? AND xperm.EXPM_DATA_ID = 'COCE_ID' " +
					"AND " + createInClause("ref.CPS_GRP_ID", groupFilter, true) +
					"AND ref.COAG_EFF_DT < getDate()  " +
					"AND (ref.COAG_TERM_DT > getDate() OR ref.COAG_TERM_DT IS NULL) ";
			
			logger.debug("getCommissionIdList Query -> " + query);

			ps = conn.prepareStatement(query);
			
			ps.setString(1, userid);

			int paramIdx = 1;
			
			for(String group : groupFilter) {
				ps.setString(++paramIdx, group);
			}
			
			rs = ps.executeQuery();
			
    		while(rs.next()) {
    			result.add(rs.getString(1));
    		}
		
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}

		return result;
	}

	public boolean isIndividualBroker(String userid) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		try {
			conn = benefitChangeContext.getFacetsDBDS().getConnection();
			
			String query = "SELECT count (*)  " +
					"FROM CPS_AGN_ARNG_REF ref " +
					"join ER_TB_SYST_EXPM_XPERMISSIONS xperm " +
					"on xperm.EXPM_DATA = ref.CPS_COMM_ENT_ID " +
					"where xperm.EXTU_ID = ? " + 
					"AND SUBSTRING(ref.CPS_COMM_ENT_ID,10,1)='D' " +
					"AND xperm.EXPM_DATA_ID = 'COCE_ID' " +
					"AND ref.COAG_EFF_DT < getDate() " +
					"AND (ref.COAG_TERM_DT > getDate() OR ref.COAG_TERM_DT IS NULL)";
			
			logger.debug("isIndividualBroker Query -> {}", query);
			
			ps = conn.prepareStatement(query);
			
			ps.setString(1, userid);
					
			rs = ps.executeQuery();
			
    		rs.next();
    		
    		if(rs.getInt(1)>0) {
    			logger.debug("Count -> {}", rs.getInt(1));
    			
    			return true;
    		}
		
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}

		return false;
	}

	public boolean isSubscriberRelatedToBroker(String userid, String subscriberid, List<String> groupFilter) {
		for(String group : groupFilter) {
			logger.debug("groups: {}", StringEscapeUtils.escapeJava(group));
		}

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		try {
			conn = benefitChangeContext.getFacetsDBDS().getConnection();
			
			String query = "SELECT COUNT(*) " +
				       "FROM (CMC_COCE_COMM_ENTY COCE INNER JOIN (CMC_COAG_AGREEMENT COAG INNER JOIN CMC_BLCO_COMM_ITEM BLCO " +
				       "ON COAG.COAR_ID = BLCO.COAR_ID) " +
				       "ON COCE.COCE_ID = COAG.COCE_ID) " +
				       "INNER JOIN (CDS_BEIN_BL_INDIC  BEIN " +
				       "INNER JOIN (CMC_MEME_MEMBER    MEME " +
				       "INNER JOIN  CMC_MEPE_PRCS_ELIG MEPE " +
				       "ON MEME.MEME_CK = MEPE.MEME_CK) " +
				       "ON BEIN.SBSB_CK = MEME.SBSB_CK) " +
				       "ON (BLCO.CSPI_ID = MEPE.CSPI_ID) AND (BLCO.BLEI_CK = BEIN.BLEI_CK) WHERE BEIN.SBSB_ID=? AND COAG.COCE_ID IN (" + 
				       "SELECT DISTINCT ref.CPS_COMM_ENT_ID FROM CPS_AGN_ARNG_REF ref join ER_TB_SYST_EXPM_XPERMISSIONS xperm on xperm.EXPM_DATA = ref.CPS_COMM_ENT_ID where xperm.EXTU_ID = ? AND SUBSTRING(ref.CPS_COMM_ENT_ID,10,1)='D' " +
				       "AND xperm.EXPM_DATA_ID = 'COCE_ID' AND ref.COAG_EFF_DT < getDate() AND (ref.COAG_TERM_DT > getDate() OR ref.COAG_TERM_DT IS NULL)) " +
				       "AND ((" + createInClause("BEIN.GRGR_ID", groupFilter, true) + ") AND ((MEME.MEME_REL)='M') AND ((MEPE.MEPE_EFF_DT)<getDate()) AND ((MEPE.MEPE_TERM_DT)>getDate()) AND ((MEPE.MEPE_ELIG_IND)='Y'))";
			
			logger.debug("isSubscriberRelatedToBroker Query -> {}", query);

			ps = conn.prepareStatement(query);
			
			ps.setString(1, subscriberid);
			ps.setString(2, userid);
					
			int paramIdx = 2;
			
			for(String group : groupFilter) {
				ps.setString(++paramIdx, group);
			}

			rs = ps.executeQuery();
			
    		rs.next();
    		
    		if(rs.getInt(1)>0) {
    			return true;
    		}
		
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}

		return false;
	}
	
	public String createInClause(String field, List<String> values, boolean noConditionIfEmpty) {
		if(noConditionIfEmpty&&(values==null||values.isEmpty())) {
			return "1=1 ";
		} else {
			StringBuilder clauseSB = new StringBuilder();
			
			for(String value : values) {
				if(clauseSB.length()!=0){
					clauseSB.append(", ");
				}
				
				clauseSB.append("?");
			}
			
					
		    return field + " IN (" + clauseSB.toString() + ") ";
		}
	}

	public String getBrokerName(String userid, String repid) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		String brokerName = "";
		
		try {
			conn = benefitChangeContext.getFacetsDBDS().getConnection();
			
			String query = "SELECT DISTINCT TOP 1 COCE_NAME FROM CMC_COCE_COMM_ENTY ent " +
						   "JOIN ER_TB_SYST_EXPM_XPERMISSIONS xpm ON ent.COCE_ID = xpm.EXPM_DATA " +
						   "JOIN CPS_AGN_ARNG_REF ref ON ref.CPS_COMM_ENT_ID = xpm.EXPM_DATA " +
						   "WHERE xpm.EXTU_ID = ? AND " +
						   "ref.CPS_AGN_NO = ? AND " +
						   "xpm.EXPM_DATA_ID = 'COCE_ID' AND ent.COCE_TERM_DT > getDate()";
			
			logger.debug("getBrokerName Query -> {}", query);
			
			ps = conn.prepareStatement(query);
			
			ps.setString(1, userid);
			ps.setString(2, repid);
					
			rs = ps.executeQuery();			
    		    		
    		if(rs.next()) {
    			brokerName = rs.getString(1);
    			logger.debug("Broker name -> {}", StringEscapeUtils.escapeJava(brokerName));
    		}
		
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}

		return brokerName;
	}

	public String getBrokerEmail(String userid) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		String brokerEmail = "";
		
		try {
			conn = benefitChangeContext.getFacetsDBDS().getConnection();
			String query = "SELECT TOP 1 CPS_AGN_COAD_EMAIL " +
					"FROM CPS_AGN_ARNG_REF ref  " +
					"join ER_TB_SYST_EXPM_XPERMISSIONS xperm " +
					"on xperm.EXPM_DATA = ref.CPS_COMM_ENT_ID " +
					"where xperm.EXTU_ID = ? " + 
					"AND SUBSTRING(ref.CPS_COMM_ENT_ID,10,1)='D' " +
					"AND xperm.EXPM_DATA_ID = 'COCE_ID' " +
					"AND ref.COAG_EFF_DT < getDate() " +
					"AND (ref.COAG_TERM_DT > getDate() OR ref.COAG_TERM_DT IS NULL)";
			
			logger.debug("getBrokerEmail Query -> {}", query);
			
			ps = conn.prepareStatement(query);
			
			ps.setString(1, userid);
					
			rs = ps.executeQuery();			
    		    		
    		if(rs.next()) {
    			brokerEmail = rs.getString(1);
    			logger.debug("Broker email -> {}", StringEscapeUtils.escapeJava(brokerEmail));
    		}
		
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}

		return brokerEmail;
	}

	public List<String> getSubscriberPlans(String subId, String productType, String eligDate) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		List<String> plans = new ArrayList<>();
		
		try {
			conn = benefitChangeContext.getFacetsDBDS().getConnection();

			String query = "select distinct c.PDPD_ID "
					.concat("from CMC_MEME_MEMBER b, CMC_MEPE_PRCS_ELIG c, CMC_CSPI_CS_PLAN d, CMC_SBSB_SUBSC e ")
					.concat("where e.SBSB_ID = ? ")
					.concat("and e.SBSB_CK = b.SBSB_CK ")
					.concat("and b.MEME_CK = c.MEME_CK ")
					.concat("and c.CSPD_CAT = d.CSPD_CAT ")
					.concat("and c.PDPD_ID = d.PDPD_ID ")
					.concat("and c.CSCS_ID = d.CSCS_ID ")					
					.concat("and c.MEPE_ELIG_IND = 'Y' ");
					
			if(eligDate==null || eligDate.isEmpty()) {
				query = query.concat("and d.CSPI_EFF_DT <= getdate() ")
				.concat("and d.CSPI_TERM_DT >= getdate() ")
				.concat("and c.MEPE_EFF_DT <= getdate() ")
				.concat("and c.MEPE_TERM_DT >= getdate() ");
			} else {
				query = query.concat("and d.CSPI_EFF_DT <= ? ")
				.concat("and d.CSPI_TERM_DT >= ? ")
				.concat("and c.MEPE_EFF_DT <= ? ")
				.concat("and c.MEPE_TERM_DT >= ? ");
			}

			if(productType!=null && !productType.isEmpty()) {
				query = query.concat("and c.CSPD_CAT = ? ");
			}
			
			logger.debug("getSubscriberPlans Query -> " + query);
			
			ps = conn.prepareStatement(query);
			
			logger.debug("Query params: subId-> " + StringEscapeUtils.escapeJava(subId));
			logger.debug("Query params: prodType-> " + StringEscapeUtils.escapeJava(productType));
			ps.setString(1, subId);
			
			int prodTypeIdx = 2;
			
			if(eligDate!=null && !eligDate.isEmpty()) {
				ps.setString(2, eligDate);
				ps.setString(3, eligDate);
				ps.setString(4, eligDate);
				ps.setString(5, eligDate);
				
				prodTypeIdx = 6;
			}
			
			if(productType!=null && !productType.isEmpty()) {
				ps.setString(prodTypeIdx, productType);
			}
				
			rs = ps.executeQuery();			
    		    		
    		while(rs.next()) {
    			String plan = rs.getString(1); 
    			if(plan!=null && !plan.isEmpty()) {
        			plans.add(plan);
    			}
    			logger.debug("Subscriber plan -> {}", StringEscapeUtils.escapeJava(plan));
    		}
		
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			ResourceTool.cleanup(rs, ps, conn);
		}

		return plans;
	}

}
