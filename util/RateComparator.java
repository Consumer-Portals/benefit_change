package com.bcbst.benefitchange.util;

import java.util.Comparator;

import com.bcbst.benefitchange.dto.Product;

/**
 * @objective This class will order the rates.
 * @author Vijay Narsingoju
 *
 */
public class RateComparator implements Comparator<Product> {

	@Override
	public int compare(Product object1, Product object2) {
		return Double.compare(object1.getTotalrate(), object2.getTotalrate());
	}

}
