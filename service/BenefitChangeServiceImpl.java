package com.bcbst.benefitchange.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.bcbst.benefitchange.dao.BenefitChangeDAO;
import com.bcbst.benefitchange.dao.IndividualBrokerDAO;
import com.bcbst.benefitchange.dao.ProductDAO;
import com.bcbst.benefitchange.dao.RateDAO;
import com.bcbst.benefitchange.dto.Address;
import com.bcbst.benefitchange.dto.Application;
import com.bcbst.benefitchange.dto.Applications;
import com.bcbst.benefitchange.dto.BenefitChangeInfo;
import com.bcbst.benefitchange.dto.BenefitChangeResponse;
import com.bcbst.benefitchange.dto.BooleanWrapper;
import com.bcbst.benefitchange.dto.CommissionId;
import com.bcbst.benefitchange.dto.Counties;
import com.bcbst.benefitchange.dto.Dependent;
import com.bcbst.benefitchange.dto.EnrollmentDate;
import com.bcbst.benefitchange.dto.MemberInfo;
import com.bcbst.benefitchange.dto.PlanRateRequest;
import com.bcbst.benefitchange.dto.PlanRateRequestList;
import com.bcbst.benefitchange.dto.PlanSearchMember;
import com.bcbst.benefitchange.dto.Plans;
import com.bcbst.benefitchange.dto.PlansRequest;
import com.bcbst.benefitchange.dto.Product;
import com.bcbst.benefitchange.dto.Products;
import com.bcbst.benefitchange.dto.RegionCodes;
import com.bcbst.benefitchange.dto.ServiceError;
import com.bcbst.benefitchange.dto.StringWrapper;
import com.bcbst.benefitchange.dto.ZipCodes;
import com.bcbst.benefitchange.pdf.PDFGenerator;
import com.bcbst.benefitchange.util.BenefitChangeException;
import com.bcbst.benefitchange.util.BenefitChangeInfoValidator;
import com.bcbst.benefitchange.util.FieldsValidator;
import com.bcbst.benefitchange.util.RateComparator;
import com.bcbst.benefitchange.util.StringTool;
import com.google.gson.Gson;

/**
 * @objective This class has the implementation of RestService methods.
 * @author Vijay Narsingoju  10/12/2015
 *
 */

@RequestScoped
public class BenefitChangeServiceImpl {
	private static final String INVALID_INPUT = "Invalid input";

	private static final String GENERAL = "GENERAL";

	private static final String VALIDATION = "VALIDATION";

	@Inject
	IndividualBrokerDAO individualBrokerDAO;
	@Inject
	RateDAO rateDAO;
	@Inject
	BenefitChangeDAO benefitChangeDAO;
	@Inject
	ProductDAO productDAO;
	
	private Logger logger = LogManager.getLogger(BenefitChangeServiceImpl.class);
	
	
	/**
	 * @objective This method will save BenefitChangeInfo form data in the database. It will also create required files for Ondemand and Transform process if the 
	 * 				action variable is Submit. 
	 * @param benefitChangeInfoObj
	 * @return
	 */
	public BenefitChangeResponse saveBenefitChangeInfo(BenefitChangeInfo benefitChangeInfoObj) {

		BenefitChangeResponse bnftChgResponse = new BenefitChangeResponse();
		try{
			logger.debug("BenefitChangeServiceImpl: saveBenefitChangeInfo() invoked :");
			logger.debug("BenefitChangeServiceImpl: saveBenefitChangeInfo" + new Gson().toJson(benefitChangeInfoObj) );
			
			// validate input
				if (benefitChangeInfoObj == null || !BenefitChangeInfoValidator.validateBenefitChangeInfo(benefitChangeInfoObj).isEmpty()) {
					bnftChgResponse.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));
					return bnftChgResponse;
				}
				boolean updform= false;
				String applicationId = benefitChangeDAO.generateAppId();
				String subscriberId = StringEscapeUtils.escapeJava(benefitChangeInfoObj.getSubscriberId());
				if (FieldsValidator.isNotEmpty(applicationId)){
						updform = benefitChangeDAO.saveBenefitChangeFormDetails(benefitChangeInfoObj, applicationId);
						logger.debug("BenefitChangeServiceImpl: saveBenefitChangeInfo() invoked: {} subscriberId: {} action: ",StringEscapeUtils.escapeJava(applicationId), subscriberId, StringEscapeUtils.escapeJava(benefitChangeInfoObj.getAction()));
						bnftChgResponse.setUpdated(updform);
						bnftChgResponse.setApplicationId(applicationId);
						logger.debug("BenefitChangeServiceImpl: saveBenefitChangeInfo() ended*: {}",StringEscapeUtils.escapeJava(bnftChgResponse.getApplicationId()));	
				}else{
					logger.error("BenefitChangeServiceImpl: saveBenefitChangeInfo() ended*: applicationId is not generated for subscriberId: {}", subscriberId);
					bnftChgResponse.setServiceError(getServiceError(null, "ApplicationId not generated", GENERAL));
				}
				
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			bnftChgResponse.setServiceError(getServiceError(e, null , GENERAL));			
		}
		return bnftChgResponse;
	}
	
	
	/**
	 * @objective This method will update BenefitChangeInfo data in database and will also generate required files for on-demand and transform  if the 
	 * 				action variable is Submit. 
	 * @param benefitChangeInfoObj
	 * @return
	 */
	public BenefitChangeResponse updateBenefitChangeInfo(BenefitChangeInfo benefitChangeInfoObj) {
		BenefitChangeResponse bnftChgResponse = new BenefitChangeResponse();
		try{
				logger.debug("BenefitChangeServiceImpl: updateBenefitChangeInfo() invoked:");
				logger.debug("BenefitChangeServiceImpl: updateBenefitChangeInfo" + new Gson().toJson(benefitChangeInfoObj) );
				boolean updform= false;
				// validate input
				if (benefitChangeInfoObj == null || FieldsValidator.isEmpty(benefitChangeInfoObj.getApplicationId()) ||
						!BenefitChangeInfoValidator.validateBenefitChangeInfo(benefitChangeInfoObj).isEmpty()) {
					bnftChgResponse.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));
					return bnftChgResponse;
				}
				logger.debug("BenefitChangeServiceImpl: updateBenefitChangeInfo() invoked for subscriberId: {} action: {}", StringEscapeUtils.escapeJava(benefitChangeInfoObj.getSubscriberId()), StringEscapeUtils.escapeJava(benefitChangeInfoObj.getAction()));
				updform = benefitChangeDAO.updateBenefitChangeFormDetails(benefitChangeInfoObj);
				bnftChgResponse.setUpdated(updform);
				bnftChgResponse.setApplicationId(benefitChangeInfoObj.getApplicationId());
				logger.debug("BenefitChangeServiceImpl: updateBenefitChangeInfo() ended:{}", StringEscapeUtils.escapeJava(benefitChangeInfoObj.getApplicationId()));
		
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			bnftChgResponse.setServiceError(getServiceError(e, null, GENERAL));
			return bnftChgResponse;
		} 
		return bnftChgResponse;
	}
	
	/**
	 * @objective This method will mark the status of the application to delete.
	 * @param applicationID
	 * @return
	 */
	public BenefitChangeResponse deleteBenefitChangeInfo(String applicationID){
		boolean updated = false;
		BenefitChangeResponse bnftChgResponse = new BenefitChangeResponse();
		try{
			// validate input
			if (FieldsValidator.isEmpty(applicationID) || !FieldsValidator.isAllNumbers(applicationID)){
				bnftChgResponse.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));	
				return bnftChgResponse;
			}
			updated = benefitChangeDAO.deleteBenefitChangeFormDetails(applicationID);
			bnftChgResponse.setApplicationId(applicationID);
			bnftChgResponse.setUpdated(updated);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			bnftChgResponse.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));
		}
		return bnftChgResponse;
	}

	
	
	/**
	 * @objective This method will get the benefit change information for a given application id.
	 * @param applicationID
	 * @return
	 */
	public BenefitChangeInfo getBenefitChangeInfo(String applicationID) {
		BenefitChangeInfo bnftChgResponse = new BenefitChangeInfo();
		try{
			logger.debug("BenefitChangeServiceImpl: getBenefitChangeInfo() invoked for applicationID: [{}]", StringEscapeUtils.escapeJava(applicationID));
			// Validate input
			if (FieldsValidator.isEmpty(applicationID) || !FieldsValidator.isAllNumbers(applicationID)) {
				bnftChgResponse.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));
				return bnftChgResponse;
			}			
			bnftChgResponse = benefitChangeDAO.getBenefitChangeFormDetails(applicationID.trim());
			if (bnftChgResponse != null) { // retrieve information from child tables only if the data is found in the master table.
				List<Dependent> dependents = null;
				List<Address> addresses = null;
				List<PlanSearchMember> planMembers = null;
				dependents = benefitChangeDAO.getDependentDetails(applicationID.trim());
				addresses = benefitChangeDAO.getAddressList(applicationID.trim());
				planMembers = productDAO.getPlanSearchMembers(applicationID.trim());
				bnftChgResponse.setDependents(dependents);
				bnftChgResponse.setAddresses(addresses);
				bnftChgResponse.setPlanSearchMembers(planMembers);
			}else{
				logger.error("Application not found for {}", StringEscapeUtils.escapeJava(applicationID));
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);

			if(bnftChgResponse!=null) {
				bnftChgResponse.setServiceError(getServiceError(e, null, GENERAL));			
			}
		}
		
		logger.debug("BenefitChangeServiceImpl: getBenefitChangeInfo: ended for applicationID: [{}]", StringEscapeUtils.escapeJava(applicationID));
		return bnftChgResponse;
	}
	
	
	
	/**
	 * @objective This method will get the counties list for a given zipcode.
	 * @param zipcode
	 * @return
	 */
	public Counties getCountyList(String zipcode) {
		logger.debug("BenefitChangeServiceImpl: getCountyList() invoked:{}", StringEscapeUtils.escapeJava(zipcode));
		Counties counties = new Counties();
		try{
			// validate input
			if (FieldsValidator.isEmpty(zipcode) || !FieldsValidator.isAllNumbers(zipcode)){ 
				counties.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));				
				return counties;				
			}
			List<String> countyList = productDAO.getCountyList(zipcode.trim());
			if (countyList != null) {
				counties.setCountyList(countyList);
				logger.debug("BenefitChangeServiceImpl: getCountyList:  {}", StringEscapeUtils.escapeJava(countyList.toString()));
			}else{
				counties.setServiceError(getServiceError(null, "No counties found", GENERAL));				
			}			
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			counties.setServiceError(getServiceError(e, null , GENERAL));
		}
		logger.debug("BenefitChangeServiceImpl: getCountyList: ended:  ");
		return counties;
	}
	
	
	
	/**
	 * @objective This method will get list of all the available medical plans including rates by calling CRE web service for a given county.  
	 * 			 
	 * @param plansRequest
	 * @return
	 */
	public Products getMedicalPlansList(PlansRequest plansRequest) {
		logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() invoked:");
		Products products = new Products();
			try{// validate input
				long startTime = System.currentTimeMillis();
				if (plansRequest == null || FieldsValidator.isEmpty(plansRequest.getCounty()) || !BenefitChangeInfoValidator.validatePlansRequest(plansRequest).isEmpty()) {
					logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() validation error:");
					products.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));	
					return products;
				}
				String county = plansRequest.getCounty();
				logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() invoked for county: {}", StringEscapeUtils.escapeJava(county));
				//  Get region code for a given county. 	
				String region = productDAO.getRegionCode(county);
				logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() got regions: {}", StringEscapeUtils.escapeJava(region));
				// Get effective and term dates of plans to be used.
				EnrollmentDate enrollmentDates = getEffectiveAndTermDates(plansRequest.getEffectiveDate());
				if (enrollmentDates == null){
					throw new BenefitChangeException("EnrollmentDates not available.");
				}
				
				// Get plans based upon region code, effective and term dates from mapping table.
				List<Product> plans = productDAO.getPlansByRegionCodes(region, enrollmentDates.getPlanEffectiveDate(), enrollmentDates.getPlanTermDate());
				
				if (plans == null || plans.isEmpty()){
					throw new BenefitChangeException("Plans not available.");
				}					
					// add the planid's to list
					List<String> planIds = new ArrayList<>();
					for (Product product: plans){
						planIds.add(product.getPlanId());
					}
					
					java.sql.Date effDate = new java.sql.Date(enrollmentDates.getPlanEffectiveDate().getTime());
					java.sql.Date termDate = new java.sql.Date(enrollmentDates.getPlanTermDate().getTime());
					List<Product> productList = productDAO.getPlanDetailsByPlanIDs(planIds, effDate, termDate);
					if (productList == null || productList.isEmpty()){
						throw new BenefitChangeException("Plans not available");
					}
					 logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() got plan details: effective date: " + effDate);
					
					 // get rates. set the plan id's and rating area to the request object
					GregorianCalendar c = new GregorianCalendar();
					c.setTime(enrollmentDates.getPlanEffectiveDate());
					XMLGregorianCalendar ratedate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
					List<MemberInfo> memberInfoList = plansRequest.getMemberInfoList();
					
					PlanRateRequestList planRateRequestList = new PlanRateRequestList();
					List<PlanRateRequest> rateReqList = new ArrayList<>(planIds.size());
					int regionCode = Integer.parseInt(region);
					
					fillRateRequestList(productList, memberInfoList, ratedate, rateReqList, regionCode);
										
					planRateRequestList.setRateReqList(rateReqList);
					// Get Rates.
					List<Product> ratesList = rateDAO.getIndividualRates(planRateRequestList);
					if (ratesList == null || ratesList.isEmpty()){
						throw new BenefitChangeException("Rates not available.");
					}
					logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() got rates: ");
					
					setMedicalPlanRates(region, productList, ratesList);
					// sort by rate.
					Collections.sort(productList, new RateComparator());
					logger.debug("BenefitChangeServiceImpl: getMedicalPlansList() adding rates to the plans: ");
					products.setProducts(productList);
				
				long timeTaken = System.currentTimeMillis() - startTime;
				logger.debug("BenefitChangeServiceImpl: getMedicalPlansList: ended in msecs: "+ timeTaken);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			products.setServiceError(getServiceError(e, null, GENERAL));
		}		
		return products;
	}


	private void setMedicalPlanRates(String region, List<Product> productList, List<Product> ratesList) {
		for (Product product: productList){
			for (Product rate: ratesList){
				
				logger.debug("BenefitChangeServiceImpl: product.getPlanId(): {}", StringEscapeUtils.escapeJava(product.getPlanId()));
				logger.debug("BenefitChangeServiceImpl: rate.getPlanId(): {}", StringEscapeUtils.escapeJava(rate.getPlanId()));
				
				if (product.getPlanId().trim().equalsIgnoreCase(rate.getPlanId().trim())){
					product.setTotalrate(rate.getTotalrate());
					product.setRegion(region);
					break;
				}
			}
		}
	}
	
	/**
	 * @objective This method returns dental and vision plans along with rates.
	 * @param plansRequest
	 * @return
	 */
	public Products getDentalAndVisionPlans(PlansRequest plansRequest) {
		logger.debug("BenefitChangeServiceImpl: getDentalAndVisionPlans() invoked:");
		Products products = new Products();
		try{
				long starttime = System.currentTimeMillis();
				JAXBContext jaxbContext = JAXBContext.newInstance(PlansRequest.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
								
				StringWriter sw = new StringWriter();
				jaxbMarshaller.marshal(new JAXBElement<PlansRequest>(new QName("uri","local"), PlansRequest.class, plansRequest), sw);
				String xmlString = sw.toString();			

				logger.info(xmlString);

				// validate input
				if (plansRequest == null || FieldsValidator.isEmpty(plansRequest.getGroupId()) || plansRequest.getMemberInfoList() == null || plansRequest.getMemberInfoList().isEmpty()
						|| !BenefitChangeInfoValidator.validatePlansRequest(plansRequest).isEmpty()) {
					logger.debug("BenefitChangeServiceImpl: getDentalAndVisionPlans() validation error:");
					products.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));	
					return products;
				}
								
				GregorianCalendar c = null;
				String strEffDate = plansRequest.getEffectiveDate();
				java.sql.Date date = (strEffDate!=null && !strEffDate.isEmpty()) ? StringTool.getSQLDate(strEffDate, "MM/dd/yyyy") : null;
				
				List<Product> productList = productDAO.getDentalAndVisionPlans(plansRequest.getGroupId(), date);
				
				if (productList == null || productList.isEmpty()){
					throw new BenefitChangeException("Dental/Vision plans not available.");
				}
				
				List<MemberInfo> memberInfoList = plansRequest.getMemberInfoList();
				
					// Get effective and term dates of plans to be used.
					EnrollmentDate enrollmentDates = getEffectiveAndTermDates(plansRequest.getEffectiveDate());
					if (enrollmentDates == null){
						throw new BenefitChangeException("Enrollment dates not available.");
					}
					
					 // get rates. set the plan id's and rating area to the request object
					c = new GregorianCalendar();
					c.setTime(enrollmentDates.getPlanEffectiveDate());
					XMLGregorianCalendar ratedate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
					
					PlanRateRequestList planRateRequestList = new PlanRateRequestList();
					List<PlanRateRequest> rateReqList = new ArrayList<>(productList.size());
					
					fillRateRequestList(productList, memberInfoList, ratedate, rateReqList, 0);
					
					planRateRequestList.setRateReqList(rateReqList);
					// Get rates
					List<Product> ratesList = rateDAO.getIndividualRates(planRateRequestList);
					if (ratesList == null || ratesList.isEmpty()){
						throw new BenefitChangeException("Rates not available.");
					}
					logger.debug("BenefitChangeServiceImpl: getDentalAndVisionPlans() got rates: ");
					// Map the rates to the plans
					setDentalVisionRates(productList, ratesList);
					
					logger.debug("BenefitChangeServiceImpl: getDentalAndVisionPlans() adding rates to the plans: ");
					products.setProducts(productList);
					long timetaken = System.currentTimeMillis() - starttime;
				logger.debug("BenefitChangeServiceImpl: getDentalAndVisionPlans: ended in msecs:  " + timetaken);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			products.setServiceError(getServiceError(e, null, GENERAL));
		}		
		return products;
	}


	private void setDentalVisionRates(List<Product> productList, List<Product> ratesList) {
		for (Product product: productList){
			logger.debug("BenefitChangeServiceImpl: setDentalVisionRates() product: {}", StringEscapeUtils.escapeJava(product.getPlanId()));
			for (Product rate: ratesList){
				logger.debug("BenefitChangeServiceImpl: setDentalVisionRates() rate.product: {}", StringEscapeUtils.escapeJava(rate.getPlanId()));
				if (product.getPlanId().trim().equalsIgnoreCase(rate.getPlanId().trim())){
					logger.debug("BenefitChangeServiceImpl: setDentalVisionRates() rate: {}", rate.getTotalrate());
					product.setTotalrate(rate.getTotalrate());								
					break;
				}
			}
		}
	}


	private void fillRateRequestList(List<Product> productList, List<MemberInfo> memberInfoList,
			XMLGregorianCalendar ratedate, List<PlanRateRequest> rateReqList, int regionCode) {
		PlanRateRequest rateReq =  null;
		for (Product product: productList){
			rateReq =  new PlanRateRequest();
			rateReq.setMemberInfoList(memberInfoList);
			rateReq.setPlanName(product.getPlanId()); // This is planid for CRE
			rateReq.setRateDate(ratedate);
			rateReq.setRatingArea(regionCode);
			rateReqList.add(rateReq);
		}
	}
	
	/**
	 * @objective This method will get the list of the available applications for a given subscriberid. 
	 * @param subscriberId
	 * @return
	 */
	public Applications getApplicationList(String subscriberId) {
		Applications applications = new Applications();
		try{
			long starttime = System.currentTimeMillis();
			// validate input
			if (FieldsValidator.isEmpty(subscriberId) || !FieldsValidator.isAlphaNumeric(subscriberId)) {
				applications.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));				
				return applications;
			}
			List<Application> applicationsList = benefitChangeDAO.getBenefitChangeApplicationsList(subscriberId);
			applications.setApplicationList(applicationsList);
			long timetaken = System.currentTimeMillis() - starttime;
			logger.debug("getApplicationList() timetaken in msecs:  "+ timetaken);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			applications.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return applications;
	}
	
	/**
	 * @objective This method will get the list of the available applications for a given subscriberid. 
	 * @param subscriberId
	 * @return
	 */
	public Applications getApplicationListForBroker(String subscriberId, List<String> repIds) {
		Applications applications = new Applications();
		try{
			logger.debug("getApplicationListForBroker() subscriberId: {}", StringEscapeUtils.escapeJava(subscriberId));
			logger.debug("getApplicationListForBroker() repIds:  {}", StringEscapeUtils.escapeJava(repIds.toString()));
			long starttime = System.currentTimeMillis();
			// validate input
			if (FieldsValidator.isEmpty(subscriberId) || !FieldsValidator.isAlphaNumeric(subscriberId)) {
				applications.setServiceError(getServiceError(null, INVALID_INPUT + subscriberId, VALIDATION));				
				return applications;
			}
			List<Application> applicationsList = benefitChangeDAO.getBenefitChangeApplicationsListForBroker(subscriberId, repIds);
			applications.setApplicationList(applicationsList);
			long timetaken = System.currentTimeMillis() - starttime;
			logger.debug("getApplicationListForBroker() timetaken in msecs:  {}", timetaken);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			applications.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return applications;
	}

	/**
	 * @objective This method will get the list of the zipcodes in TN.
	 * @return
	 */
	public ZipCodes getZipCodes() {
		ZipCodes zipcodes = new ZipCodes();
		try{
			List<String> zipcodesList = productDAO.getZipCodes();
			if(zipcodesList == null || zipcodesList.isEmpty()){
				throw new BenefitChangeException("Zipcodes not available.");
			}
			zipcodes.setZipcodeList(zipcodesList);		
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			zipcodes.setServiceError(getServiceError(e, null, GENERAL));
		}
		return zipcodes;
	}
	
	
	
	/**
	 * @objective This method will get the effective and term dates for searching plans in SBC table. SBC table will have plans for several years.
	 * @return
	 */
	private EnrollmentDate getEffectiveAndTermDates(String effectiveDate){
		logger.debug("getEffectiveAndTermDates entered.");
		EnrollmentDate effTermDates = null;
		List<EnrollmentDate> list = null;
		try{
			list = productDAO.getEnrollmentDates();
			logger.debug("getEffectiveAndTermDates: enrollmentdate list found");
			logger.debug("getEffectiveAndTermDates: effectiveDate is {}", StringEscapeUtils.escapeJava(effectiveDate));
			String format = "MM/dd/yyyy";
			if(effectiveDate==null) {
				Date todaysDate = Calendar.getInstance().getTime();
				effectiveDate = StringTool.getDateString(todaysDate, format);
			}
			if (list != null && !list.isEmpty()){ 
				effTermDates = new EnrollmentDate();
				for(EnrollmentDate enrollmentDate: list){
					String startDateStr = StringTool.getDateString(enrollmentDate.getPlanEffectiveDate(), format);
					logger.debug("getEffectiveAndTermDates: enrollmentDate.getPlanEffectiveDate() " + startDateStr);
					String endDateStr = StringTool.getDateString(enrollmentDate.getPlanTermDate(), format);
					logger.debug("getEffectiveAndTermDates: enrollmentDate.getPlanTermDate() " + endDateStr);
					int startDateVal = StringTool.compareDates(effectiveDate, startDateStr, format);
					int endDateVal = StringTool.compareDates(effectiveDate, endDateStr, format);
					
					// if todays date is in between start date and end date then grab the corresponding effective/term dates.
					if ((startDateVal == 1 || startDateVal == 0) && (endDateVal == -1 || endDateVal ==0)){
						effTermDates.setPlanEffectiveDate(enrollmentDate.getPlanEffectiveDate());
						effTermDates.setPlanTermDate(enrollmentDate.getPlanTermDate());

						logger.debug("Dates were found - Effective date: "+ effTermDates.getPlanEffectiveDate() + " Term Date: "+ effTermDates.getPlanTermDate());
						break;
					}
					logger.debug(" Effective date: "+ effTermDates.getPlanEffectiveDate() + " Term Date: "+ effTermDates.getPlanTermDate());
				}
			}else{
				logger.debug("getEffectiveAndTermDates enrollment dates list is null."); 
				throw new BenefitChangeException("effective/termdates is empty.");
			}			
		}catch(BenefitChangeException e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new BenefitChangeException(e.getMessage());
		}
		logger.debug("getEffectiveAndTermDates ended."); 
		return effTermDates;
	}
	
	/**
	 * @objective This method generates files for On-Demand and Transform. These are an additional copy. Contact On-Demand and Transform team before invoking 
	 * 			  this method if it is going to be a conflict as the applicationId will be same.  
	 * @param applicationId
	 */
	public BenefitChangeResponse submitDuplicatePDFRequest(String applicationID){
		BenefitChangeResponse benefitChangeResponse = new BenefitChangeResponse();
		try{
			logger.error("*******submitDuplicatePDFRequest invoked********: {}", StringEscapeUtils.escapeJava(applicationID)); 
			// validate input
			if (FieldsValidator.isEmpty(applicationID) || !FieldsValidator.isAllNumbers(applicationID)) {
				benefitChangeResponse.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));				
				return benefitChangeResponse;
			}
			BenefitChangeInfo benefitChangeInfo = getBenefitChangeInfo(applicationID);
			if (benefitChangeInfo != null){
				benefitChangeInfo.setAction("Submit");
				PDFGenerator pdfGenerator = new PDFGenerator();
				pdfGenerator.submitRequestForPDFGeneration(benefitChangeInfo, applicationID);
				benefitChangeInfo.setUpdated(true);
			}
		
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			benefitChangeResponse.setServiceError(getServiceError(e, null, GENERAL));
		}
		logger.error("*******submitDuplicatePDFRequest ended********: {}", StringEscapeUtils.escapeJava(applicationID)); 
		return benefitChangeResponse;
	}
	
	/**
	 * @objective This method gets effectivedate based upon groupId and as of date.
	 * @param groupId
	 * @param asofDate
	 * @return
	 */
	public EnrollmentDate getEffectiveDateByGroupID(String groupId){
		EnrollmentDate enrollmentDate = new EnrollmentDate();
		try{
			Calendar calendar = rateDAO
					.getEffectiveDateByGroupID(new java.sql.Date(Calendar.getInstance().getTimeInMillis()), groupId);
			enrollmentDate.setEffectiveDate(calendar.getTime());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			enrollmentDate.setServiceError(getServiceError(e, null, GENERAL));
		}
		return enrollmentDate;
	}
	
	/**
	 * @objective This method constructs ServiceError and returns it.
	 * @author v82473n
	 * @param e
	 * @return
	 */
	private ServiceError getServiceError(Exception e, String message, String id) {
		ServiceError serviceError = new ServiceError();
		serviceError.setId(id);
		if(e!=null){
			serviceError.setDescription(e.getMessage());
		}else{
			serviceError.setDescription(message);
		}
		return serviceError;
	}


	public CommissionId getCommissionIdList(String userid, List<String> groupList) {
		CommissionId result = new CommissionId();
		
		try {
			result.setCommissionIdList(individualBrokerDAO.getCommissionIdList(userid, groupList));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return result;

	}


	public Plans getPlans(String subscriberId, String productType, String eligDate) {
		Plans result = new Plans();
		
		try {
			result.setPlans(individualBrokerDAO.getSubscriberPlans(subscriberId, productType, eligDate));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return result;
	}


	public BooleanWrapper isIndividualBroker(String userid) {
		BooleanWrapper result = new BooleanWrapper();
		
		try {
			result.setValue(individualBrokerDAO.isIndividualBroker(userid));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return result;
	}


	public BooleanWrapper isSubscriberRelatedToBroker(String userid, String subscriberid,
			List<String> groupList) {
		BooleanWrapper result = new BooleanWrapper();

		try {
			result.setValue(individualBrokerDAO.isSubscriberRelatedToBroker(userid, subscriberid, groupList));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return result;
	}


	public StringWrapper getBrokerName(String userid, String repid) {
		StringWrapper result = new StringWrapper();

		try {
			result.setValue(individualBrokerDAO.getBrokerName(userid, repid));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return result;
	}


	public StringWrapper getBrokerEmail(String userid) {
		StringWrapper result = new StringWrapper();

		try {
			result.setValue(individualBrokerDAO.getBrokerEmail(userid));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setServiceError(getServiceError(e, null, GENERAL));
		}
		
		return result;
	}


	public RegionCodes getRegionCodeList(String zipcode) {
		logger.debug("BenefitChangeServiceImpl: getRegionCodeList() invoked:"+ StringEscapeUtils.escapeJava(zipcode));
		RegionCodes regionCodes = new RegionCodes();
		try{
			// validate input
			if (FieldsValidator.isEmpty(zipcode) || !FieldsValidator.isAllNumbers(zipcode)){ 
				regionCodes.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));				
				return regionCodes;				
			}
			List<String> countyList = productDAO.getCountyList(zipcode.trim());
			
			if (countyList != null) {
				Set<String> regionCodeSet = new HashSet<>();
				
				for(String county : countyList) {
					regionCodeSet.add(productDAO.getRegionCode(county));
				}

				regionCodes.setRegionCodeList(new ArrayList<String>(regionCodeSet));

				logger.debug("BenefitChangeServiceImpl: getRegionCodeList:  {}",  StringEscapeUtils.escapeJava(countyList.toString()));
			}else{
				regionCodes.setServiceError(getServiceError(null, "No counties found", GENERAL));				
			}			
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			regionCodes.setServiceError(getServiceError(e, null , GENERAL));
		}
		logger.debug("BenefitChangeServiceImpl: getRegionCodeList: ended:  ");
		
		return regionCodes;
	}


	public StringWrapper getRegionCode(String county) {
		logger.debug("BenefitChangeServiceImpl: getRegionCode() invoked: {}", StringEscapeUtils.escapeJava(county));
		StringWrapper regionCode = new StringWrapper();
		try{
			// validate input
			if (FieldsValidator.isEmpty(county)){ 
				regionCode.setServiceError(getServiceError(null, INVALID_INPUT, VALIDATION));	
				
				return regionCode;				
			}
			regionCode.setValue(productDAO.getRegionCode(county));
			
			logger.debug("BenefitChangeServiceImpl: getRegionCode:  {}", StringEscapeUtils.escapeJava(regionCode.getValue()));
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			regionCode.setServiceError(getServiceError(e, null , GENERAL));
		}
		logger.debug("BenefitChangeServiceImpl: getRegionCode: ended:  ");
		
		return regionCode;
	}
}
