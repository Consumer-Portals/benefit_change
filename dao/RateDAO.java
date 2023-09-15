package com.bcbst.benefitchange.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.AddressingFeature;

import org.apache.logging.log4j.Logger;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;

import com.bcbst.applogger.core.ApplicationSettings;
import com.bcbst.benefitchange.core.BenefitChangeContext;
import com.bcbst.benefitchange.dto.MemberInfo;
import com.bcbst.benefitchange.dto.PlanRateRequest;
import com.bcbst.benefitchange.dto.PlanRateRequestList;
import com.bcbst.benefitchange.dto.Product;
import com.bcbst.benefitchange.util.BenefitChangeException;
import com.bcbst.benefitchange.util.FieldsValidator;
import com.bcbst.rate.engine.client.ArrayOfMember;
import com.bcbst.rate.engine.client.ArrayOfRateRequest;
import com.bcbst.rate.engine.client.ArrayOfRateResponse;
import com.bcbst.rate.engine.client.Member;
import com.bcbst.rate.engine.client.RateEngine;
import com.bcbst.rate.engine.client.RateEngineSoap;
import com.bcbst.rate.engine.client.RateRequest;
import com.bcbst.rate.engine.client.RateResponse;
import com.google.gson.GsonBuilder;

/**
 * @objective This class has methods for getting Rates for a given plan/member details. It invokes CRE webservice.
 * @author Vijay Narsingoju
 *
 */
@Named
public class RateDAO {
	private Logger logger = LogManager.getLogger(RateDAO.class);
	@Inject
	private BenefitChangeContext benefitChangeContext;

	
	/**
	 * @Objective This method invokes CRE WebService to get the rates for the plans.
	 * @param planRateRequestList
	 * @return Products
	 * @author Vijay Narsingoju  
	 */
	public List<Product> getIndividualRates(PlanRateRequestList planRateRequestList) {
		long starttime = System.currentTimeMillis();
		logger.info("RateDAO: getIndividualRates() invoked." );
		List<Product> productList = null;		
		try {
			String rateEndpoint = null; 
			String  appSetting = ApplicationSettings.getSettingValue("CREServiceEndpoint");
			
			if (appSetting!=null){
				rateEndpoint = appSetting.trim();
				logger.debug("From SettingsManagement: "+rateEndpoint);
				
			}
			// validate endpoint
			if (FieldsValidator.isEmpty(rateEndpoint)){
				throw new BenefitChangeException("Invalid CRE endpoint");
			}
			ArrayOfRateRequest reqArray = new ArrayOfRateRequest();
			List<RateRequest> rateReqList = reqArray.getRateRequest();
			
			if (planRateRequestList!=null && planRateRequestList.getRateReqList() != null && !planRateRequestList.getRateReqList().isEmpty()){
				logger.debug("RateDAO: getIndividualRates() invoked: planRateRequestList has data." );
				
				// Get the list of members from the first object and populate memberList once and add the same list of members for each planraterequest as the members are going to be same.
				PlanRateRequest planRateRequest1stObj = planRateRequestList.getRateReqList().get(0);
				List<MemberInfo> memberInfoList = planRateRequest1stObj.getMemberInfoList();
				ArrayOfMember arrayOfMember = new ArrayOfMember();
				List<Member> memberList = arrayOfMember.getMember();
				for (MemberInfo memberInfo: memberInfoList){
					Member member = new Member();
					member.setCountyCode(memberInfo.getCountyCode());
					member.setClassPlan("NODATA");
					// // Calculate and set the ages as of benefit change enrollment effective date.								
					member.setAge(memberInfo.getAge());
					logger.debug("RateDAO: getIndividualRates(): Age asof benefit change effective date: ["
							+ memberInfo.getAge() + "]  DOB entered: " + memberInfo.getDob());	
					member.setMemberID(memberInfo.getMemberID());
					member.setGender(memberInfo.getGender());
					member.setDOB(memberInfo.getDob());
					member.setRelationship(memberInfo.getRelationship());
					member.setTobaccoUse(memberInfo.isTobaccoUse());
					member.setUnderwritingFactor(1);
					memberList.add(member);
				}
				
				
				// End
				 
				// For each planrequest
					for (PlanRateRequest planRateRequest:planRateRequestList.getRateReqList()){
						RateRequest rateRequest = new RateRequest();
						rateRequest.setPlanName(planRateRequest.getPlanName());
						rateRequest.setRateDate(planRateRequest.getRateDate());
						rateRequest.setRatingArea(planRateRequest.getRatingArea());
						rateRequest.setMemberList(arrayOfMember);
						rateReqList.add(rateRequest);
					}			
		
					String jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(reqArray);
					logger.debug("RateDAO Req:"+jsonRequest);
					logger.debug("RateDAO: getIndividualRates() : calling CRE service." );
					
					appSetting = ApplicationSettings.getSettingValue("rateServiceQName");
					final String qNameString;
					
					if (appSetting!=null){
						qNameString =  appSetting;
					} else 
						qNameString = "";
					logger.debug("Qname: "+ qNameString);
					//STGE //QName qName = new QName("https://ws.bcbst.com/salesmarketing/individual/rateengine/v1.0/rateengine.asmx", "RateService");
					//PROD //http://e-sales.bcbst.com/individual/rateengine/rateengine.asmx
					QName qName = new QName(qNameString, "RateService");
						
					Service service = Service.create(qName);
					service.setHandlerResolver(new HandlerResolver() {
						
						@Override
						public List<Handler> getHandlerChain(PortInfo arg0) {
							List<Handler> hchain = new ArrayList<Handler>();
					        hchain.add(new SOAPHandler<SOAPMessageContext>(){

								@Override
								public void close(MessageContext arg0) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public boolean handleFault(SOAPMessageContext arg0) {
									SOAPMessage message = arg0.getMessage();
							        try {
							        	logger.debug("RATEDAO Soap Fault");
							        	ByteArrayOutputStream bout = new ByteArrayOutputStream();   
							        	message.writeTo(bout);   
							        	String msg = bout.toString("UTF-8");
							        	logger.debug(msg);
							        } catch (SOAPException | IOException e) {
							        	logger.error("Fault Message in Rate : " + e.getCause());
							        	logger.error("Fault Message in Rate : " + e.getMessage());
							        } 
							        return true;
								}

								@Override
								public boolean handleMessage(SOAPMessageContext arg0) {
									SOAPMessage message = arg0.getMessage();
							        boolean isOutboundMessage = (Boolean) arg0.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
							        if (isOutboundMessage) {
							        	logger.debug("RateDAO OUTBOUND MESSAGE");
							 
							        } else {
							        	logger.debug("RateDAO INBOUND MESSAGE");
							        }
							        try {
							            
							            
							        	ByteArrayOutputStream bout = new ByteArrayOutputStream();   
							        	message.writeTo(bout);   
							        	String msg = bout.toString("UTF-8");
							        	logger.debug(msg);
							        } catch (SOAPException e) {
							        	logger.error(e);
							        } catch (Exception e) {
							        	logger.error(e);
							        }
							    	logger.debug("Exiting Chain\n");
							        return true;
								}

								@Override
								public Set<QName> getHeaders() {
									// TODO Auto-generated method stub
									return null;
								}
					        	
					        });
					        return hchain;
						}
					});
					RateEngineSoap delegate = service.getPort(RateEngineSoap.class, new AddressingFeature(true, false)); 
			        BindingProvider bp = (BindingProvider) delegate;
			        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, rateEndpoint);
			        
			        logger.debug("RateDAO: getIndividualRates() invoking :" + rateEndpoint);
					ArrayOfRateResponse  response = delegate.getIndividualRatesMulti(reqArray);
					List<RateResponse> rateResp = response.getRateResponse();
					logger.debug("RateDAO: getIndividualRates() Success" );
					String jsonResp = new GsonBuilder().create().toJson(reqArray);
					logger.debug("RateDAO Resp:"+jsonResp);
					Product product = null;
					productList = new ArrayList<>();
					for (RateResponse rateObj: rateResp){
						product = new Product();
						product.setPlanId(rateObj.getPlanName());
						product.setTotalrate(rateObj.getTotalRate());
						productList.add(product);
					}					
			}
			

		} catch (Exception e) {
			logger.error("Error calling rate engine service");
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		}
		long timetaken = System.currentTimeMillis() - starttime;
		logger.debug("RateDAO: getIndividualRates() ended in " + timetaken + " msecs.");
		return productList;
	}
	

	
	/**
	 * @objective This method is for getting effective date's for the benefit change from INDIV_ENRLL_CHG_EFF_DATE table.
	 * @return
	 */
	public Calendar getEffectiveDateByGroupID(Date currentDate, String groupId) {
		logger.debug("RateDAO: getEffectiveDateByGroupID() executed.");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Calendar asofDate = null;
		
		logger.debug("RateDAO: getEffectiveDateByGroupID() currentDate [{}]", currentDate);
		
		if (currentDate == null || groupId == null){
			logger.error("ProductDAO: getAsOfDateByCurrentDateForCRE() Invalid values for currentdate or groupId. GroupId: [{}] currentDate: [{}]", StringEscapeUtils.escapeJava(groupId), currentDate);
			throw new BenefitChangeException("Invalid input");
		}
		Date effectiveDate = null;
		try {
			conn = benefitChangeContext.getEnrlbftchgDS().getConnection();
			ps = conn.prepareStatement("SELECT EFFECTIVE_DATE FROM INDIV_ENRLL_CHG_EFF_DATE WHERE START_DATE <= ? and END_DATE >= ? AND GROUP_ID = ? ORDER BY EFFECTIVE_DATE ASC");
			ps.setDate(1, currentDate);
			ps.setDate(2, currentDate);
			ps.setString(3, groupId.trim());
			ps.setQueryTimeout(10);
			rs = ps.executeQuery();
			if (rs.next()) {
				effectiveDate = rs.getDate("EFFECTIVE_DATE");
				if (effectiveDate != null){
					asofDate = Calendar.getInstance();
					asofDate.setTime(effectiveDate);					
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
		logger.debug("ProductDAO: getAsOfDateByCurrentDateForCRE() ended: effectiveDate [{}]", effectiveDate);
		return asofDate;
	}
	
	
	public static void main(String[] args){
		List<Product> productList = null;
		String rateEndpoint = "https://stge-ws.bcbst.com/salesmarketing/individual/rateengine/v1.0/rateengine.asmx";
		Logger logger = LogManager.getLogger(RateDAO.class);
		QName qName = new QName("https://ws.bcbst.com/salesmarketing/individual/rateengine/v1.0/rateengine.asmx", "RateService");
		Service service = Service.create(qName); 
        RateEngine delegate = service.getPort(RateEngine.class, new AddressingFeature(true, false)); 
        BindingProvider bp = (BindingProvider) delegate;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, rateEndpoint);
        ArrayOfRateRequest reqArray = new ArrayOfRateRequest();
		List<RateRequest> rateReqList = reqArray.getRateRequest();
		
		
		
		ArrayOfRateResponse  response = delegate.getRateEngineSoap().getIndividualRatesMulti(reqArray);
		List<RateResponse> rateResp = response.getRateResponse();
		logger.debug("RateDAO: getIndividualRates() Success" );
		String jsonResp = new GsonBuilder().create().toJson(reqArray);
		logger.debug("RateDAO Resp:"+jsonResp);
		Product product = null;
		
		productList = new ArrayList<>();
		for (RateResponse rateObj: rateResp){
			product = new Product();
			product.setPlanId(rateObj.getPlanName());
			product.setTotalrate(rateObj.getTotalRate());
			productList.add(product);
		}
	}

}



