/**
 *
 */
package com.bcbst.benefitchange.util;

/**
 * @author v82473n
 *
 */
public enum RTCTest{

	B("Bronze"), S("Silver"), G("Gold"), P("Platinum");

	private String type;

	private String y;
	RTCTest(String type) {
		this.type = type;
	}

	public String getType(){
		return type;
	}

}
