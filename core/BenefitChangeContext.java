package com.bcbst.benefitchange.core;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

/**
 * @author Vijay Narsingoju
 *
 */
@ApplicationScoped
public class BenefitChangeContext {
	@Resource(name="EnrlbftchgDS")
	private DataSource enrlbftchgDS;
	
	@Resource(name="BnfChgASDBDS")
	private DataSource bnfChgASDBDS;

	@Resource(name="BnfChgFacetsDS")
	private DataSource facetsDBDS;
	      
    

    public DataSource getEnrlbftchgDS(){
    	return enrlbftchgDS;
    }
    
    public DataSource getBnfChgASDBDS(){
    	return bnfChgASDBDS;
    }
	
    public DataSource getFacetsDBDS(){
    	return facetsDBDS;
    }
}
