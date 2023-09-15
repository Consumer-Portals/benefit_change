/**
 * 
 */
package com.bcbst.benefitchange.util;

/**
 * @author v82473n
 * 
 */
public enum MetalLevel{

	B("Bronze"), S("Silver"), G("Gold"), P("Platinum");

	private String type; 

	MetalLevel(String type) {
		this.type = type;
	}
	
	public String getType(){
		return type;
	}

}
