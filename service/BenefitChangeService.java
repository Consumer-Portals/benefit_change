package com.bcbst.benefitchange.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bcbst.benefitchange.dto.Applications;
import com.bcbst.benefitchange.dto.BenefitChangeInfo;
import com.bcbst.benefitchange.dto.BenefitChangeResponse;
import com.bcbst.benefitchange.dto.BooleanWrapper;
import com.bcbst.benefitchange.dto.CommissionId;
import com.bcbst.benefitchange.dto.Counties;
import com.bcbst.benefitchange.dto.EnrollmentDate;
import com.bcbst.benefitchange.dto.Plans;
import com.bcbst.benefitchange.dto.PlansRequest;
import com.bcbst.benefitchange.dto.Products;
import com.bcbst.benefitchange.dto.RegionCodes;
import com.bcbst.benefitchange.dto.StringWrapper;
import com.bcbst.benefitchange.dto.ZipCodes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;


/**
 * @Objective RESTful web service for Individual health form BenefitChange. This
 *            service is built using Jersey framework and supports both XML & JSON media types. It is a 
 *            secured web service which uses authentication and ssl.
 * @author Vijay Narsingoju 10/7/2015
 * 
 * 
 */
@Path("/resource")
@Api
@SwaggerDefinition(info = @Info(
			title = "Benefit Change Service",
			description = "Web services related to Benefit Change Service",
            version = "1.0", 
            contact = @Contact(
                    name = "Consumer Portals", 
                    email = "IS_ConsumerPortals_SupportTeam@bcbst.com"
                 ))
			)
public class BenefitChangeService {
	private Logger logger = LogManager.getLogger(BenefitChangeService.class);
	
	@Inject
	BenefitChangeServiceImpl benefitChangeServiceImpl;

	@POST
	@Path("/savebenefit")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Save Benefits Change Information",response= BenefitChangeResponse.class, tags = "Benefit Change Service")
	public BenefitChangeResponse saveBenefitChangeInfo(BenefitChangeInfo benefitChangeInfo) {	 	
		
		return benefitChangeServiceImpl.saveBenefitChangeInfo(benefitChangeInfo);
	}
	
	@PUT
	@Path("/updatebenefit")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Update Benefits Change Information",response= BenefitChangeResponse.class, tags = "Benefit Change Service")
	public BenefitChangeResponse updateBenefitChangeInfo(BenefitChangeInfo benefitChangeInfo) { 
		
		return benefitChangeServiceImpl.updateBenefitChangeInfo(benefitChangeInfo);
	}
	
	@PUT
	@Path("/deletebenefit")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Delete Benefits Change Information",response= BenefitChangeResponse.class, tags = "Benefit Change Service")
	public BenefitChangeResponse deleteBenefitChangeInfo(@ApiParam(value = "application ID", required = true,example="1234") String applicationID) { 
		
		return benefitChangeServiceImpl.deleteBenefitChangeInfo(applicationID);
	}

	@POST
	@Path("/getbenefit")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Get Benefits Change Information",response= BenefitChangeInfo.class, tags = "Benefit Change Service")
	public BenefitChangeInfo getBenefitChangeInfo(@ApiParam(value = "application ID", required = true,example="1234") String applicationID) {		
		
		return benefitChangeServiceImpl.getBenefitChangeInfo(applicationID);
	}
		
	@POST
	@Path("/counties")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Counties List", response= Counties.class,tags = "Counties")
	public Counties getCountyList(@ApiParam(value = "zip Code", required = true,example="1234") String zipcode) {
		
		return benefitChangeServiceImpl.getCountyList(zipcode);
	}

	@GET
	@Path("/regioncodes/{zipcode}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Region ZipCode List",response= RegionCodes.class, tags = "Region Code")
	public RegionCodes getRegionCodeList(@ApiParam(value = "zip Code", required = true,example="1234") @PathParam("zipcode")String zipcode) {
		
		return benefitChangeServiceImpl.getRegionCodeList(zipcode);
	}
	
	@GET
	@Path("/regioncode/{county}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Region Code For Country",response= StringWrapper.class, tags = "Region Code")
	public StringWrapper getRegionCode(@ApiParam(value = "county", required = true,example="1234") @PathParam("county")String county) {
		
		return benefitChangeServiceImpl.getRegionCode(county);
	}

	@POST
	@Path("/applications")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Application List",response= Applications.class, tags = "Application")
	public Applications getApplicationsList(@ApiParam(value = "subscriber Id", required = true,example="1234")String subscriberId) {
		
		return benefitChangeServiceImpl.getApplicationList(subscriberId);
	}
	
	@POST
	@Path("/applicationsforbroker")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Application List For Broker",response= Applications.class, tags = "Application")
	public Applications getApplicationsListForBroker(@ApiParam(value = "subscriber Id", required = true,example="1234") @QueryParam("subscriberid")String subscriberId, @QueryParam("repid")List<String> repIds) {
		
		return benefitChangeServiceImpl.getApplicationListForBroker(subscriberId, repIds);
	}

	@GET
	@Path("/zipcodes")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET ZipCodes",response= ZipCodes.class, tags = "Region Code")
	public ZipCodes getZipCodes() {
		
		return benefitChangeServiceImpl.getZipCodes();
	}
	
	
	@POST
	@Path("/medicalplans")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Mediacal Plans List",response= Products.class, tags = "Medical Plans")
	public Products getMedicalPlansList(PlansRequest plansRequest) {
		
		return benefitChangeServiceImpl.getMedicalPlansList(plansRequest);
	}
	
	@POST
	@Path("/dentalvisionplans")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Dental Vision Plans List",response= Products.class, tags = "Medical Plans")
	public Products getDentalVisionPlansList(PlansRequest denvisplansRequest) {
		
		return benefitChangeServiceImpl.getDentalAndVisionPlans(denvisplansRequest);
	}
	
	
	@POST
	@Path("/duplicatepdf")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "GET Dental Vision Plans List",response= BenefitChangeResponse.class, tags = "Medical Plans")
	public BenefitChangeResponse submitDuplicatePDFRequest(String applicationID){
		
		return benefitChangeServiceImpl.submitDuplicatePDFRequest(applicationID);
	}
	
	@POST
	@Path("/effectivedate")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = "get Effective Date",response= EnrollmentDate.class, tags = "Effective Date")
	public EnrollmentDate getEffectiveDate(String groupId) {
		
		return benefitChangeServiceImpl.getEffectiveDateByGroupID(groupId);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/getcommissionids/{userid}")
	@ApiOperation(value = "Fetch all Commission Ids",response= CommissionId.class, tags = "Commission Id")
	public CommissionId findAllCommissionId(@PathParam("userid") String userid,
			@QueryParam("group") List<String> groupList) {
		return benefitChangeServiceImpl.getCommissionIdList(userid, groupList);	
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/isindividualbroker/{userid}")
	@ApiOperation(value = "is Individual Broker",response= BooleanWrapper.class, tags = "Broker")
	public BooleanWrapper isIndividualBroker(@PathParam("userid") String userid) {
		
		return benefitChangeServiceImpl.isIndividualBroker(userid);	
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/issubscriberrelated/{userid}/{subscriberid}")
	@ApiOperation(value = "GET Broker Name",response= StringWrapper.class, tags = "Broker")
	public BooleanWrapper isSubscriberRelatedToBroker(
			@PathParam("userid") String userid,
			@PathParam("subscriberid") String subscriberid,
			@QueryParam("group") List<String> groupList) {
		
		return benefitChangeServiceImpl.isSubscriberRelatedToBroker(userid, subscriberid, groupList);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/getbrokername/{userid}/{repid}")
	@ApiOperation(value = "GET Broker Email",response= StringWrapper.class,tags = "Broker")
	public StringWrapper getBrokerName(@ApiParam(value = "user Id", required = true,example="72985650") @PathParam("userid") String userid, @ApiParam(value = "user Id", required = true,example="72985650") @PathParam("repid") String repid) {
	
		return benefitChangeServiceImpl.getBrokerName(userid, repid);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/getbrokeremail/{userid}")
	public StringWrapper getBrokerEmail(@ApiParam(value = "user Id", required = true,example="72985650") @PathParam("userid") String userid) {

		return benefitChangeServiceImpl.getBrokerEmail(userid);	
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/getplans/{subscriberid}")
	@ApiOperation(value = "GET Plans List",response=Plans.class, tags = "Subscriber")
	public Plans getPlans(@ApiParam(value = "subscriber Id", required = true,example="m900353220") @PathParam("subscriberid") String subscriberId,
			@ApiParam(value = "product Type", required = true,example="Medical") @QueryParam("producttype") String productType, @ApiParam(value = "user Id", required = true) @QueryParam("eligdate") String eligDate) {

		return benefitChangeServiceImpl.getPlans(subscriberId, productType, eligDate);	
	}

}
