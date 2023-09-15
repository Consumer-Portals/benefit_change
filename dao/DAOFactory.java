package com.bcbst.benefitchange.dao;

/**
 * @objective Factory for DAO classes.
 * @author v82473n
 *
 */
public class DAOFactory {

	private static final DAOFactory factory = new DAOFactory();
	private BenefitChangeDAO benefitDAO;
	private ProductDAO productDAO;
	private RateDAO rateDAO;

	private DAOFactory() {
	}

	public static DAOFactory getInstance() {
		return factory;
	}

	public BenefitChangeDAO getBenefitChangeDAO() {
		if (benefitDAO == null) {
			benefitDAO = new BenefitChangeDAO();
		}
		return benefitDAO;
	}

	public ProductDAO getProductDAO() {
		if (productDAO == null) {
			productDAO = new ProductDAO();
		}
		return productDAO;
	}

	public RateDAO getRateDAO() {
		if (rateDAO == null) {
			rateDAO = new RateDAO();
		}
		return rateDAO;
	}

}
