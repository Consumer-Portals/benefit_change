package com.bcbst.benefitchange.core;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;


/**
 * @author Vijay Narsingoju
 *
 */
public class BenefitChangeListener implements ServletContextListener {

	@Resource(name="EnrlbftchgDS")
	private static DataSource enrlbftchgDS;
	
	@Resource(name="BnfChgASDBDS")
	private static DataSource bnfChgASDBDS;

	@Resource(name="BnfChgFacetsDS")
	private static DataSource facetsDBDS;
	
    /**
     * Default constructor. 
     */
    public BenefitChangeListener() {
        // Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce) {
    	System.out.println("BenefitChangeListener: setting enrlbftchgDS/bnfChgASDBDS to null");
    	enrlbftchgDS = null;
    	bnfChgASDBDS = null;
    	facetsDBDS = null;
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
    	System.out.println("BenefitChangeListener initialized.");
    }
    
    public static DataSource getEnrlbftchgDS(){
    	return enrlbftchgDS;
    }
    
    public static DataSource getBnfChgASDBDS(){
    	return bnfChgASDBDS;
    }
	
    public static DataSource getFacetsDBDS(){
    	return facetsDBDS;
    }
}
